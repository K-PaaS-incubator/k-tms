package com.kglory.tms.web.model.dto;

public class TrafficProtocolDto extends SearchDto {

    private Integer startRowSize;
    private Integer endRowSize;

    private String sortSelect;

    private String protocolSelect;
    private Integer ucType;
    private Integer nProtocol;
    private String protocolName;

    private Long graphItem;

    public Integer getStartRowSize() {
        return startRowSize;
    }

    public void setStartRowSize(Integer startRowSize) {
        this.startRowSize = startRowSize;
    }

    public Integer getEndRowSize() {
        return endRowSize;
    }

    public void setEndRowSize(Integer endRowSize) {
        this.endRowSize = endRowSize;
    }

    public String getSortSelect() {
        return sortSelect;
    }

    public void setSortSelect(String sortSelect) {
        this.sortSelect = sortSelect;
    }

    public String getProtocolSelect() {
        return protocolSelect;
    }

    public void setProtocolSelect(String protocolSelect) {
        this.protocolSelect = protocolSelect;
    }

    public Integer getUcType() {
        return ucType;
    }

    public void setUcType(Integer ucType) {
        this.ucType = ucType;
    }

    public Integer getnProtocol() {
        return nProtocol;
    }

    public void setnProtocol(Integer nProtocol) {
        this.nProtocol = nProtocol;
    }

    public Long getGraphItem() {
        return graphItem;
    }

    public void setGraphItem(Long graphItem) {
        this.graphItem = graphItem;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }
    
    public static String getTrafficProtocolName(int protocol) {
    	String result = "N/A";
        switch(protocol) {
            case 1:
            	result =  "ICMP";
            	break;
            case 2:
            	result =  "ARP";
            	break;
            case 3:
            	result =  "RARP";
            	break;
            case 4:
            	result =  "LLC";
            	break;
            case 6:
            	result =  "TCP";
            	break;
            case 17:
            	result =  "UDP";
            	break;
            case 255:
            	result =  "Others";
            	break;
            default:
            	result =  "N/A";
        }
        return result;
    }

    public static String getTrafficProtocolName(int type, int protocol) {
    	String result = "N/A";
        switch (type) {
            case 10: // Total bandwidth
            	result = "Total";
            	break;
            case 11: // 프로토콜
                switch (protocol) {
                    case 1: // IP
                    	result = "IP";
                    	break;
                    case 2: // ARP
                    	result = "ARP";
                    	break;
                    case 3: // RARP
                    	result = "RARP";
                    	break;
                    case 4: // LLC
                    	result = "LLC";
                    	break;
                    case 255: // others
                    	result = "Others";
                    	break;
                    default:
                    	result = "N/A";
                }
                break;
            case 12: // Frame size
                switch (protocol) {
                    case 1: // 64 >=
                    	result = "<= 64";
                    	break;
                    case 2: // 65-128
                    	result = "65-128";
                    	break;
                    case 3: // 129-256
                    	result = "129-256";
                    	break;
                    case 4: // 257-512
                    	result = "257-512";
                    	break;
                    case 5: // 513-1024
                    	result = "513-1024";
                    	break;
                    case 6: // 1024 <
                    	result = "> 1024";
                    	break;
                    default:
                    	result = "N/A";
                }
                break;
            case 20: // IP 전체
            	result = "IP";
            	break;
            case 21: // IP protocol
            	switch (protocol) {
                    case 1: // ICMP
                    	result = "ICMP";
                    	break;
                    case 6: // TCP
                    	result = "TCP";
                    	break;
                    case 17:// UDP
                    	result = "UDP";
                    	break;
                    default:
                    	result = "N/A";
                }
            	break;
            case 30: // ICMP 전체
            	result = "ICMP";
            	break;
            case 50: // TCP session activity
                switch (protocol) {
                    case 1: // TCP Syn
                    	result = "Syn";
                    	break;
                    case 2: // TCP Syn+Ack
                    	result = "Syn+Ack";
                    	break;
                    case 3: // TCP Reset
                    	result = "Reset";
                    	break;
                    case 4: // TCP Fin
                    	result = "Fin";
                    	break;
                    default:
                    	result = "N/A";
                }
                break;
            case 60: // 유해트래픽 
            	result = "Malicious";
            	break;
            default:
                return "N/A";
        }
        return result;

    }
}
