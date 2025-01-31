package com.kglory.tms.web.util.packet.idx;

public class Ipv4HeaderIndex {

	private short ipv4HeaderStartIdx;
	private short ipv4HeaderEndIdx;
	private short ipv4VersionStartIdx;
	private short ipv4VersionEndIdx;
	private short ipHdrLengthStartIdx;
	private short ipHdrLengthEndIdx;
	private short typeOfServiceStartIdx;
	private short typeOfServiceEndIdx;
	private short totalLengthStartIdx;
	private short totalLengthEndIdx;
	private short identificationStartIdx;
	private short identificationEndIdx;
	private short ipFlagsStartIdx;
	private short ipFlagsEndIdx;
	private short fragOffsetStartIdx;
	private short fragOffsetEndIdx;
	private short timeToliveStartIdx;
	private short timeToliveEndIdx;
	private short protocolStartIdx;
	private short protocolEndIdx;
	private short checkSumStartIdx;
	private short checkSumEndIdx;
	private short srcAddressStartIdx;
	private short srcAddressSumEndIdx;
	private short destAddressStartIdx;
	private short destAddressEndIdx;
	
	public Ipv4HeaderIndex(){
		this.setValue();
	}
	
	public short getIpv4HeaderStartIdx() {
		return ipv4HeaderStartIdx;
	}
	public void setIpv4HeaderStartIdx(short ipv4HeaderStartIdx) {
		this.ipv4HeaderStartIdx = ipv4HeaderStartIdx;
	}
	public short getIpv4HeaderEndIdx() {
		return ipv4HeaderEndIdx;
	}
	public void setIpv4HeaderEndIdx(short ipv4HeaderEndIdx) {
		this.ipv4HeaderEndIdx = ipv4HeaderEndIdx;
	}
	public short getIpv4VersionStartIdx() {
		return ipv4VersionStartIdx;
	}
	public void setIpv4VersionStartIdx(short ipv4VersionStartIdx) {
		this.ipv4VersionStartIdx = ipv4VersionStartIdx;
	}
	public short getIpv4VersionEndIdx() {
		return ipv4VersionEndIdx;
	}
	public void setIpv4VersionEndIdx(short ipv4VersionEndIdx) {
		this.ipv4VersionEndIdx = ipv4VersionEndIdx;
	}
	public short getIpHdrLengthStartIdx() {
		return ipHdrLengthStartIdx;
	}
	public void setIpHdrLengthStartIdx(short ipHdrLengthStartIdx) {
		this.ipHdrLengthStartIdx = ipHdrLengthStartIdx;
	}
	public short getIpHdrLengthEndIdx() {
		return ipHdrLengthEndIdx;
	}
	public void setIpHdrLengthEndIdx(short ipHdrLengthEndIdx) {
		this.ipHdrLengthEndIdx = ipHdrLengthEndIdx;
	}
	public short getTypeOfServiceStartIdx() {
		return typeOfServiceStartIdx;
	}
	public void setTypeOfServiceStartIdx(short typeOfServiceStartIdx) {
		this.typeOfServiceStartIdx = typeOfServiceStartIdx;
	}
	public short getTypeOfServiceEndIdx() {
		return typeOfServiceEndIdx;
	}
	public void setTypeOfServiceEndIdx(short typeOfServiceEndIdx) {
		this.typeOfServiceEndIdx = typeOfServiceEndIdx;
	}
	public short getTotalLengthStartIdx() {
		return totalLengthStartIdx;
	}
	public void setTotalLengthStartIdx(short totalLengthStartIdx) {
		this.totalLengthStartIdx = totalLengthStartIdx;
	}
	public short getTotalLengthEndIdx() {
		return totalLengthEndIdx;
	}
	public void setTotalLengthEndIdx(short totalLengthEndIdx) {
		this.totalLengthEndIdx = totalLengthEndIdx;
	}
	public short getIdentificationStartIdx() {
		return identificationStartIdx;
	}
	public void setIdentificationStartIdx(short identificationStartIdx) {
		this.identificationStartIdx = identificationStartIdx;
	}
	public short getIdentificationEndIdx() {
		return identificationEndIdx;
	}
	public void setIdentificationEndIdx(short identificationEndIdx) {
		this.identificationEndIdx = identificationEndIdx;
	}
	public short getIpFlagsStartIdx() {
		return ipFlagsStartIdx;
	}
	public void setIpFlagsStartIdx(short ipFlagsStartIdx) {
		this.ipFlagsStartIdx = ipFlagsStartIdx;
	}
	public short getIpFlagsEndIdx() {
		return ipFlagsEndIdx;
	}
	public void setIpFlagsEndIdx(short ipFlagsEndIdx) {
		this.ipFlagsEndIdx = ipFlagsEndIdx;
	}
	public short getFragOffsetStartIdx() {
		return fragOffsetStartIdx;
	}
	public void setFragOffsetStartIdx(short fragOffsetStartIdx) {
		this.fragOffsetStartIdx = fragOffsetStartIdx;
	}
	public short getFragOffsetEndIdx() {
		return fragOffsetEndIdx;
	}
	public void setFragOffsetEndIdx(short fragOffsetEndIdx) {
		this.fragOffsetEndIdx = fragOffsetEndIdx;
	}
	public short getTimeToliveStartIdx() {
		return timeToliveStartIdx;
	}
	public void setTimeToliveStartIdx(short timeToliveStartIdx) {
		this.timeToliveStartIdx = timeToliveStartIdx;
	}
	public short getTimeToliveEndIdx() {
		return timeToliveEndIdx;
	}
	public void setTimeToliveEndIdx(short timeToliveEndIdx) {
		this.timeToliveEndIdx = timeToliveEndIdx;
	}
	public short getProtocolStartIdx() {
		return protocolStartIdx;
	}
	public void setProtocolStartIdx(short protocolStartIdx) {
		this.protocolStartIdx = protocolStartIdx;
	}
	public short getProtocolEndIdx() {
		return protocolEndIdx;
	}
	public void setProtocolEndIdx(short protocolEndIdx) {
		this.protocolEndIdx = protocolEndIdx;
	}
	public short getCheckSumStartIdx() {
		return checkSumStartIdx;
	}
	public void setCheckSumStartIdx(short checkSumStartIdx) {
		this.checkSumStartIdx = checkSumStartIdx;
	}
	public short getCheckSumEndIdx() {
		return checkSumEndIdx;
	}
	public void setCheckSumEndIdx(short checkSumEndIdx) {
		this.checkSumEndIdx = checkSumEndIdx;
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

	private void setValue(){
		
		this.setIpv4HeaderStartIdx(		(short)14 );
		this.setIpv4HeaderEndIdx(		(short)33 );
		this.setIpv4VersionStartIdx(	(short)14 );
		this.setIpv4VersionEndIdx(		(short)14 );
		this.setIpHdrLengthStartIdx(	(short)14 );
		this.setIpHdrLengthEndIdx(		(short)14 );
		this.setTypeOfServiceStartIdx(	(short)15 );
		this.setTypeOfServiceEndIdx(	(short)15 );
		this.setTotalLengthStartIdx(	(short)16 );
		this.setTotalLengthEndIdx(		(short)17 );
		this.setIdentificationStartIdx(	(short)18 );
		this.setIdentificationEndIdx(	(short)19 );
		this.setIpFlagsStartIdx(		(short)20 );
		this.setIpFlagsEndIdx(			(short)20 );
		this.setFragOffsetStartIdx(		(short)20 );
		this.setFragOffsetEndIdx(		(short)21 );
		this.setTimeToliveStartIdx(		(short)22 );
		this.setTimeToliveEndIdx(		(short)22 );
		this.setProtocolStartIdx(		(short)23 );
		this.setProtocolEndIdx(			(short)23 );
		this.setCheckSumStartIdx(		(short)24 );
		this.setCheckSumEndIdx(			(short)25 );
		this.setSrcAddressStartIdx(		(short)26 );
		this.setSrcAddressSumEndIdx( 	(short)29 );
		this.setDestAddressStartIdx(	(short)30 );
		this.setDestAddressEndIdx(		(short)33 );	
	}
}
