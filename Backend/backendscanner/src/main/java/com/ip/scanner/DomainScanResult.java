package com.ip.scanner;

import java.util.List;
import java.util.Map;

public class DomainScanResult {
    private String domain;
    private int reputation;
    private Stats stats;
    private boolean is_flagged;
    private String registrar;
    private Long creation_date;
    private Map<String, String> categories;
    private List<DnsRecord> dns_records;

    public String getDomain() { return domain; }
    public int getReputation() { return reputation; }
    public Stats getStats() { return stats; }
    public boolean isFlagged() { return is_flagged; }
    public String getRegistrar() { return registrar; }
    public Long getCreationDate() { return creation_date; }
    public Map<String, String> getCategories() { return categories; }
    public List<DnsRecord> getDnsRecords() { return dns_records; }

    public static class Stats {
        private int malicious;
        private int suspicious;
        private int harmless;
        private int undetected;

        public int getMalicious() { return malicious; }
        public int getSuspicious() { return suspicious; }
        public int getHarmless() { return harmless; }
        public int getUndetected() { return undetected; }
    }

    public static class DnsRecord {
        private String type;
        private String value;
        private int ttl;

        public String getType() { return type; }
        public String getValue() { return value; }
        public int getTtl() { return ttl; }
    }
}

