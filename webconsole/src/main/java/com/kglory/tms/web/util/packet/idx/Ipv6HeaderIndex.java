/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.packet.idx;

/**
 *
 * @author leecjong
 */
public class Ipv6HeaderIndex {

    private short ipv6HeaderStartIdx;
    private short ipv6HeaderEndIdx;
    private short ipv6VersionStartIdx;
    private short ipv6VersionEndIdx;
    private short trafficStartIdx;
    private short trafficEndIdx;
    private short flowLabelStartIdx;
    private short flowLabelEndIdx;
    private short payloadLengthStartIdx;
    private short payloadLengthEndIdx;
    private short nextHeaderStartIdx;
    private short nextHeaderEndIdx;
    private short hopLimitStartIdx;
    private short hopLimitEndIdx;
    private short srcAddressStartIdx;
    private short srcAddressSumEndIdx;
    private short destAddressStartIdx;
    private short destAddressEndIdx;

    public Ipv6HeaderIndex() {
        this.setValue();
    }

    public short getIpv6HeaderStartIdx() {
        return ipv6HeaderStartIdx;
    }

    public void setIpv6HeaderStartIdx(short ipv6HeaderStartIdx) {
        this.ipv6HeaderStartIdx = ipv6HeaderStartIdx;
    }

    public short getIpv6HeaderEndIdx() {
        return ipv6HeaderEndIdx;
    }

    public void setIpv6HeaderEndIdx(short ipv6HeaderEndIdx) {
        this.ipv6HeaderEndIdx = ipv6HeaderEndIdx;
    }

    public short getIpv6VersionStartIdx() {
        return ipv6VersionStartIdx;
    }

    public void setIpv6VersionStartIdx(short ipv6VersionStartIdx) {
        this.ipv6VersionStartIdx = ipv6VersionStartIdx;
    }

    public short getIpv6VersionEndIdx() {
        return ipv6VersionEndIdx;
    }

    public void setIpv6VersionEndIdx(short ipv6VersionEndIdx) {
        this.ipv6VersionEndIdx = ipv6VersionEndIdx;
    }

    public short getTrafficStartIdx() {
        return trafficStartIdx;
    }

    public void setTrafficStartIdx(short trafficStartIdx) {
        this.trafficStartIdx = trafficStartIdx;
    }

    public short getTrafficEndIdx() {
        return trafficEndIdx;
    }

    public void setTrafficEndIdx(short trafficEndIdx) {
        this.trafficEndIdx = trafficEndIdx;
    }

    public short getFlowLabelStartIdx() {
        return flowLabelStartIdx;
    }

    public void setFlowLabelStartIdx(short flowLabelStartIdx) {
        this.flowLabelStartIdx = flowLabelStartIdx;
    }

    public short getFlowLabelEndIdx() {
        return flowLabelEndIdx;
    }

    public void setFlowLabelEndIdx(short flowLabelEndIdx) {
        this.flowLabelEndIdx = flowLabelEndIdx;
    }

    public short getPayloadLengthStartIdx() {
        return payloadLengthStartIdx;
    }

    public void setPayloadLengthStartIdx(short payloadLengthStartIdx) {
        this.payloadLengthStartIdx = payloadLengthStartIdx;
    }

    public short getPayloadLengthEndIdx() {
        return payloadLengthEndIdx;
    }

    public void setPayloadLengthEndIdx(short payloadLengthEndIdx) {
        this.payloadLengthEndIdx = payloadLengthEndIdx;
    }

    public short getNextHeaderStartIdx() {
        return nextHeaderStartIdx;
    }

    public void setNextHeaderStartIdx(short nextHeaderStartIdx) {
        this.nextHeaderStartIdx = nextHeaderStartIdx;
    }

    public short getNextHeaderEndIdx() {
        return nextHeaderEndIdx;
    }

    public void setNextHeaderEndIdx(short nextHeaderEndIdx) {
        this.nextHeaderEndIdx = nextHeaderEndIdx;
    }

    public short getHopLimitStartIdx() {
        return hopLimitStartIdx;
    }

    public void setHopLimitStartIdx(short hopLimitStartIdx) {
        this.hopLimitStartIdx = hopLimitStartIdx;
    }

    public short getHopLimitEndIdx() {
        return hopLimitEndIdx;
    }

    public void setHopLimitEndIdx(short hopLimitEndIdx) {
        this.hopLimitEndIdx = hopLimitEndIdx;
    }

    public short getSrcAddressStartIdx() {
        return srcAddressStartIdx;
    }

    public void setSrcAddressStartIdx(short srcAddressStartIdx) {
        this.srcAddressStartIdx = srcAddressStartIdx;
    }

    public short getSrcAddressSumEndIdx() {
        return srcAddressSumEndIdx;
    }

    public void setSrcAddressSumEndIdx(short srcAddressSumEndIdx) {
        this.srcAddressSumEndIdx = srcAddressSumEndIdx;
    }

    public short getDestAddressStartIdx() {
        return destAddressStartIdx;
    }

    public void setDestAddressStartIdx(short destAddressStartIdx) {
        this.destAddressStartIdx = destAddressStartIdx;
    }

    public short getDestAddressEndIdx() {
        return destAddressEndIdx;
    }

    public void setDestAddressEndIdx(short destAddressEndIdx) {
        this.destAddressEndIdx = destAddressEndIdx;
    }

    private void setValue() {
        this.setIpv6HeaderStartIdx((short) 14);
        this.setIpv6HeaderEndIdx((short) 53);
        this.setIpv6VersionStartIdx((short) 14);
        this.setIpv6VersionEndIdx((short) 14);
        this.setTrafficStartIdx((short) 15);
        this.setTrafficEndIdx((short) 17);
        this.setFlowLabelStartIdx((short) 15);
        this.setFlowLabelEndIdx((short) 17);
        this.setPayloadLengthStartIdx((short) 18);
        this.setPayloadLengthEndIdx((short) 19);
        this.setNextHeaderStartIdx((short) 20);
        this.setNextHeaderEndIdx((short) 20);
        this.setHopLimitStartIdx((short) 21);
        this.setHopLimitEndIdx((short) 21);
        this.setSrcAddressStartIdx((short) 22);
        this.setSrcAddressSumEndIdx((short) 37);
        this.setDestAddressStartIdx((short) 38);
        this.setDestAddressEndIdx((short) 53);
    }
}
