package org.findzach.trader.scheduled;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.findzach.trader.model.Disclosure;
import org.findzach.trader.model.Member;
import org.findzach.trader.model.Transaction;
import org.findzach.trader.model.xml.DisclosureMember;
import org.findzach.trader.model.xml.FinancialDisclosure;
import org.findzach.trader.repository.DisclosureRepository;
import org.findzach.trader.service.member.MemberService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Zach Smith
 * @since 10/21/2025
 *
 * Scheduled downloader for financial disclosures. Handles ZIP download/extraction, XML parsing,
 * and entity persistence with scraping. Structured for easy extraction to services (e.g., DownloadService, ParserService)
 * by keeping core logic in protected methods.
 */
@Component
public class FinancialDisclosureDownloader {

    private static final Logger log = LoggerFactory.getLogger(FinancialDisclosureDownloader.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private DisclosureRepository disclosureRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    String apiBaseUrl = "https://scanner.nojoke.dev";

    private static final String ZIP_URL = "https://disclosures-clerk.house.gov/public_disc/financial-pdfs/2025FD.zip";
    private static final String XML_FILE_NAME = "2025FD.xml";
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir"); // System temp directory
    private static final String TEMP_ZIP_PATH = Paths.get(TEMP_DIR, "2025FD.zip").toString();
    private static final String EXTRACT_DIR = Paths.get(TEMP_DIR, "extracted").toString();

    /**
     * Entry point for scheduled execution. Orchestrates download, extract, and process.
     * Can be called by @Scheduled or manually.
     */
    @Transactional  // Wraps the whole flow in a tx if needed; propagates to sub-methods
    public void downloadAndExtractDaily() {
        try {
            log.info("Starting Financial Disclosure Downloader...");

            downloadZip();
            extractZip();
            processXml();
            log.info("Financial Disclosure Downloader completed successfully.");

        } catch (Exception e) {
            log.error("Error in download/extract process", e);
            // Optional: Add retry logic or notification here (e.g., via EmailService)
            throw new RuntimeException("Download process failed", e);  // Re-throw for scheduler error handling
        }
    }

    private void downloadZip() throws IOException {
        byte[] zipBytes = restTemplate.getForObject(ZIP_URL, byte[].class);
        if (zipBytes == null || zipBytes.length == 0) {
            throw new IOException("Failed to download ZIP or empty response from " + ZIP_URL);
        }

        Path zipPath = Paths.get(TEMP_ZIP_PATH);
        Files.createDirectories(zipPath.getParent());
        Files.write(zipPath, zipBytes);
        log.info("ZIP downloaded to: {}", TEMP_ZIP_PATH);
    }

    private void extractZip() throws IOException {
        Path extractPath = Paths.get(EXTRACT_DIR);
        Files.createDirectories(extractPath);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(TEMP_ZIP_PATH))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = extractPath.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
            log.info("ZIP extracted to: {}", EXTRACT_DIR);
        }
    }

    public static int counter = 0;

    protected void processXml() throws Exception {
        Path xmlPath = Paths.get(EXTRACT_DIR, XML_FILE_NAME);
        if (!Files.exists(xmlPath)) {
            log.warn("XML file not found at: {}", xmlPath);
            return;
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(FinancialDisclosure.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        FinancialDisclosure financialDisclosure = (FinancialDisclosure) unmarshaller.unmarshal(xmlPath.toFile());

        if (financialDisclosure.getMembers() != null) {
            log.info("Processing {} members from XML.", financialDisclosure.getMembers().size());
            for (DisclosureMember entry : financialDisclosure.getMembers()) {
                if (counter > 20) break;

                processMember(entry);
                counter++;
            }
        } else {
            log.warn("No members found in XML.");
        }
    }

    /**
     * Processes a single member: Loads/creates Member, checks for duplicates, scrapes if needed, and persists.
     * @Transactional ensures isolation per member (good for failure isolation in loops).
     */
    @Transactional(timeout = 300)  // 5 min timeout for scraping; adjust as needed
    protected void processMember(DisclosureMember entry) {
        String firstName = entry.getFirst();
        String lastName = entry.getLast();
        String stateDistrict = entry.getStateDst();

        log.debug("Processing member: {} {} ({})", firstName, lastName, stateDistrict);

        Optional<Member> loadedMember = memberService.getMemberByNameAndDistrict(firstName, lastName, stateDistrict);

        Member member;
        if (loadedMember.isEmpty()) {
            member = memberService.save(new Member(entry.getPrefix(), firstName, lastName, entry.getSuffix(), stateDistrict));
            log.info("Created new member: {}", member.getMemberDetails());
        } else {
            member = loadedMember.get();
        }

        // Re-attach to ensure managed state (fixes LazyInitializationException)
        member = entityManager.merge(member);

        // Debug: Optional; remove after verification
        log.debug("Member {}: managed={}, disclosures initialized={}", member.getMemberDetails(),
                entityManager.contains(member), Hibernate.isInitialized(member.getDisclosures()));

        Optional<Disclosure> optionalDisclosure = disclosureRepository.findByDocId(entry.getDocID());
        if (optionalDisclosure.isPresent()) {
            log.info("Disclosure {} already exists for member {}, skipping.", entry.getDocID(), member.getMemberDetails());
            return;
        }

        if ("P".equalsIgnoreCase(entry.getFilingType())) {
            try {
                String pdfUrl = String.format("https://disclosures-clerk.house.gov/public_disc/ptr-pdfs/%d/%s.pdf",
                        entry.getYear(), entry.getDocID());
                JsonNode jsonResponse = callScraper(pdfUrl);

                if (jsonResponse == null || !"success".equals(jsonResponse.get("status").asText())) {
                    log.warn("Failed to scrape {}", pdfUrl);
                    return;
                }

                Disclosure disclosure = new Disclosure(entry.getFilingType(), entry.getYear(),
                        parseDate(entry.getFilingDate()), entry.getDocID());

                JsonNode filerInfo = jsonResponse.get("filerInfo");
                if (filerInfo != null) {
                    disclosure.setFilerStatus(filerInfo.get("filerStatus").asText());
                }
                disclosure.setTransactionCount(jsonResponse.get("transaction_count").asInt(0));
                disclosure.setUrl(jsonResponse.get("url").asText(""));
                disclosure.setMember(member);

                JsonNode transactionsNode = jsonResponse.get("transactions");
                if (transactionsNode != null && transactionsNode.isArray()) {
                    for (JsonNode txNode : transactionsNode) {
                        Transaction tx = new Transaction();
                        tx.setAmountRange(getTextOrEmpty(txNode, "amountRange"));
                        tx.setAsset(getTextOrEmpty(txNode, "asset"));
                        tx.setComments(getTextOrEmpty(txNode, "comments"));
                        tx.setDescription(getTextOrEmpty(txNode, "description"));
                        tx.setFilingId(getTextOrEmpty(txNode, "filingId"));
                        tx.setFilingStatus(getTextOrEmpty(txNode, "filingStatus"));
                        tx.setLocation(getTextOrEmpty(txNode, "location"));
                        tx.setNotificationDate(parseDateSafely(getTextOrEmpty(txNode, "notificationDate")));
                        tx.setOwner(getTextOrEmpty(txNode, "owner"));
                        tx.setTicker(getTextOrEmpty(txNode, "ticker"));
                        tx.setTransactionDate(parseDateSafely(getTextOrEmpty(txNode, "transactionDate")));
                        tx.setTransactionType(getTextOrEmpty(txNode, "transactionType"));
                        disclosure.addTransaction(tx);
                    }
                }

                member.addDisclosure(disclosure);
                memberService.save(member);
                log.info("Saved disclosure {} for member {}", entry.getDocID(), member.getMemberDetails());

            } catch (Exception scrapeEx) {
                log.error("Scrape/processing error for disclosure {}: {}", member.getMemberDetails(), scrapeEx.getMessage());
                // Tx will rollback this member; others unaffected
            }
        } else {
            log.debug("Skipping non-P filing type: {}", entry.getFilingType());
        }
    }

    // Helper methods (inline for now; easy to extract to utils class later)
    private String getTextOrEmpty(JsonNode node, String field) {
        return (node != null && node.has(field)) ? node.get(field).asText("") : "";
    }

    private Date parseDateSafely(String dateStr) {
        try {
            return parseDate(dateStr);
        } catch (Exception e) {
            log.warn("Failed to parse date: {} – skipping", dateStr);
            return null;
        }
    }

    private JsonNode callScraper(String pdfUrl) {
        try {
            String url = apiBaseUrl + "/parse/url";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("url", pdfUrl);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return objectMapper.readTree(response.getBody());
            }
        } catch (Exception e) {
            log.error("Scraper call failed for {}: {}", pdfUrl, e.getMessage());
        }
        return null;
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty() || "N/A".equals(dateStr)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            log.warn("Date parse failed: {}", dateStr);
            return null;
        }
    }
}