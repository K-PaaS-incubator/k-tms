package com.kglory.tms.web.util.packet.idx;

public class IcmpHeaderIndex {

    private int icmpHeaderStartIdx;
    private int icmpHeaderEndIdx;
    private int icmpTypeStartIdx;
    private int icmpTypeEndIdx;
    private int icmpCodeStartIdx;
    private int icmpCodeEndIdx;
    private int icmpChecksumStartIdx;
    private int icmpChecksumEndIdx;
    private int icmpIdentifierStartIdx;
    private int icmpIdentifierEndIdx;
    private int icmpSequenceStartIdx;
    private int icmpSequenceEndIdx;

    public IcmpHeaderIndex(int offset) {
        this.setValue(offset);
    }

    public int getIcmpHeaderStartIdx() {
        return icmpHeaderStartIdx;
    }

    public void setIcmpHeaderStartIdx(int icmpHeaderStartIdx) {
        this.icmpHeaderStartIdx = icmpHeaderStartIdx;
    }

    public int getIcmpHeaderEndIdx() {
        return icmpHeaderEndIdx;
    }

    public void setIcmpHeaderEndIdx(int icmpHeaderEndIdx) {
        this.icmpHeaderEndIdx = icmpHeaderEndIdx;
    }

    public int getIcmpTypeStartIdx() {
        return icmpTypeStartIdx;
    }

    public void setIcmpTypeStartIdx(int icmpTypeStartIdx) {
        this.icmpTypeStartIdx = icmpTypeStartIdx;
    }

    public int getIcmpTypeEndIdx() {
        return icmpTypeEndIdx;
    }

    public void setIcmpTypeEndIdx(int icmpTypeEndIdx) {
        this.icmpTypeEndIdx = icmpTypeEndIdx;
    }

    public int getIcmpCodeStartIdx() {
        return icmpCodeStartIdx;
    }

    public void setIcmpCodeStartIdx(int icmpCodeStartIdx) {
        this.icmpCodeStartIdx = icmpCodeStartIdx;
    }

    public int getIcmpCodeEndIdx() {
        return icmpCodeEndIdx;
    }

    public void setIcmpCodeEndIdx(int icmpCodeEndIdx) {
        this.icmpCodeEndIdx = icmpCodeEndIdx;
    }

    public int getIcmpChecksumStartIdx() {
        return icmpChecksumStartIdx;
    }

    public void setIcmpChecksumStartIdx(int icmpChecksumStartIdx) {
        this.icmpChecksumStartIdx = icmpChecksumStartIdx;
    }

    public int getIcmpChecksumEndIdx() {
        return icmpChecksumEndIdx;
    }

    public void setIcmpChecksumEndIdx(int icmpChecksumEndIdx) {
        this.icmpChecksumEndIdx = icmpChecksumEndIdx;
    }

    public int getIcmpIdentifierStartIdx() {
        return icmpIdentifierStartIdx;
    }

    public void setIcmpIdentifierStartIdx(int icmpIdentifierStartIdx) {
        this.icmpIdentifierStartIdx = icmpIdentifierStartIdx;
    }

    public int getIcmpIdentifierEndIdx() {
        return icmpIdentifierEndIdx;
    }

    public void setIcmpIdentifierEndIdx(int icmpIdentifierEndIdx) {
        this.icmpIdentifierEndIdx = icmpIdentifierEndIdx;
    }

    public int getIcmpSequenceStartIdx() {
        return icmpSequenceStartIdx;
    }

    public void setIcmpSequenceStartIdx(int icmpSequenceStartIdx) {
        this.icmpSequenceStartIdx = icmpSequenceStartIdx;
    }

    public int getIcmpSequenceEndIdx() {
        return icmpSequenceEndIdx;
    }

    public void setIcmpSequenceEndIdx(int icmpSequenceEndIdx) {
        this.icmpSequenceEndIdx = icmpSequenceEndIdx;
    }

    private void setValue(int offset) {

        this.setIcmpHeaderStartIdx(offset);
        this.setIcmpHeaderEndIdx(offset + 7);
        this.setIcmpTypeStartIdx(offset);
        this.setIcmpTypeEndIdx(offset);
        this.setIcmpCodeStartIdx(offset + 1);
        this.setIcmpCodeEndIdx(offset + 1);
        this.setIcmpChecksumStartIdx(offset + 2);
        this.setIcmpChecksumEndIdx(offset + 3);
        this.setIcmpIdentifierStartIdx(offset + 4);
        this.setIcmpIdentifierEndIdx(offset + 5);
        this.setIcmpSequenceStartIdx(offset + 6);
        this.setIcmpSequenceEndIdx(offset + 7);
    }

}
