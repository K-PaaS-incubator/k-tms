package com.kglory.tms.web.util.packet.idx;

public class TcpHeaderIndex {

	private int tcpHeaderStartIdx;
	private int tcpHeaderEndIdx;
	private int tcpSrcPortStartIdx;
	private int tcpSrcPortEndIdx;	
	private int tcpDstPortStartIdx;
	private int tcpDstPortEndIdx;		
	private int seqNoStartIdx;
	private int seqNoEndIdx;
	private int ackNoStartIdx;
	private int ackNoEndIdx;	
	private int tcpHeaderLenStartIdx;
	private int tcpHeaderLenEndIdx;	
	private int reservedStartIdx;
	private int reservedEndIdx;
	private int tcpFlagsStartIdx;
	private int tcpFlagsEndIdx;
	private int urgFlagStartIdx;
	private int urgFlagEndIdx;
	private int ackFlagStartIdx;
	private int ackFlagEndIdx;
	private int pshFlagStartIdx;
	private int pshFlagEndIdx;
	private int rstFlagStartIdx;
	private int rstFlagEndIdx;
	private int synFlagStartIdx;
	private int synFlagEndIdx;
	private int finFlagStartIdx;
	private int finFlagEndIdx;
	private int windowSizeStartIdx;
	private int windowSizeEndIdx;
	private int tcpChecksumStartIdx;
	private int tcpChecksumEndIdx;
	private int urgentPtrStartIdx;
	private int urgentPtrEndIdx;
	private int optionsStartIdx;
	private int optionsEndIdx;
	
	public TcpHeaderIndex(int offset, short options){
		this.setValue(offset, options);
	}
	
	public int getTcpHeaderStartIdx() {
		return tcpHeaderStartIdx;
	}
	public void setTcpHeaderStartIdx(int tcpHeaderStartIdx) {
		this.tcpHeaderStartIdx = tcpHeaderStartIdx;
	}
	public int getTcpHeaderEndIdx() {
		return tcpHeaderEndIdx;
	}
	public void setTcpHeaderEndIdx(int tcpHeaderEndIdx) {
		this.tcpHeaderEndIdx = tcpHeaderEndIdx;
	}
	public int getTcpSrcPortStartIdx() {
		return tcpSrcPortStartIdx;
	}
	public void setTcpSrcPortStartIdx(int tcpSrcPortStartIdx) {
		this.tcpSrcPortStartIdx = tcpSrcPortStartIdx;
	}
	public int getTcpSrcPortEndIdx() {
		return tcpSrcPortEndIdx;
	}
	public void setTcpSrcPortEndIdx(int tcpSrcPortEndIdx) {
		this.tcpSrcPortEndIdx = tcpSrcPortEndIdx;
	}
	public int getTcpDstPortStartIdx() {
		return tcpDstPortStartIdx;
	}
	public void setTcpDstPortStartIdx(int tcpDstPortStartIdx) {
		this.tcpDstPortStartIdx = tcpDstPortStartIdx;
	}
	public int getTcpDstPortEndIdx() {
		return tcpDstPortEndIdx;
	}
	public void setTcpDstPortEndIdx(int tcpDstPortEndIdx) {
		this.tcpDstPortEndIdx = tcpDstPortEndIdx;
	}
	public int getSeqNoStartIdx() {
		return seqNoStartIdx;
	}
	public void setSeqNoStartIdx(int seqNoStartIdx) {
		this.seqNoStartIdx = seqNoStartIdx;
	}
	public int getSeqNoEndIdx() {
		return seqNoEndIdx;
	}
	public void setSeqNoEndIdx(int seqNoEndIdx) {
		this.seqNoEndIdx = seqNoEndIdx;
	}
	public int getAckNoStartIdx() {
		return ackNoStartIdx;
	}
	public void setAckNoStartIdx(int ackNoStartIdx) {
		this.ackNoStartIdx = ackNoStartIdx;
	}
	public int getAckNoEndIdx() {
		return ackNoEndIdx;
	}
	public void setAckNoEndIdx(int ackNoEndIdx) {
		this.ackNoEndIdx = ackNoEndIdx;
	}
	public int getTcpHeaderLenStartIdx() {
		return tcpHeaderLenStartIdx;
	}
	public void setTcpHeaderLenStartIdx(int tcpHeaderLenStartIdx) {
		this.tcpHeaderLenStartIdx = tcpHeaderLenStartIdx;
	}
	public int getTcpHeaderLenEndIdx() {
		return tcpHeaderLenEndIdx;
	}
	public void setTcpHeaderLenEndIdx(int tcpHeaderLenEndIdx) {
		this.tcpHeaderLenEndIdx = tcpHeaderLenEndIdx;
	}
	public int getReservedStartIdx() {
		return reservedStartIdx;
	}
	public void setReservedStartIdx(int reservedStartIdx) {
		this.reservedStartIdx = reservedStartIdx;
	}
	public int getReservedEndIdx() {
		return reservedEndIdx;
	}
	public void setReservedEndIdx(int reservedEndIdx) {
		this.reservedEndIdx = reservedEndIdx;
	}
	public int getTcpFlagsStartIdx() {
		return tcpFlagsStartIdx;
	}
	public void setTcpFlagsStartIdx(int tcpFlagsStartIdx) {
		this.tcpFlagsStartIdx = tcpFlagsStartIdx;
	}
	public int getTcpFlagsEndIdx() {
		return tcpFlagsEndIdx;
	}
	public void setTcpFlagsEndIdx(int tcpFlagsEndIdx) {
		this.tcpFlagsEndIdx = tcpFlagsEndIdx;
	}
	public int getUrgFlagStartIdx() {
		return urgFlagStartIdx;
	}
	public void setUrgFlagStartIdx(int urgFlagStartIdx) {
		this.urgFlagStartIdx = urgFlagStartIdx;
	}
	public int getUrgFlagEndIdx() {
		return urgFlagEndIdx;
	}
	public void setUrgFlagEndIdx(int urgFlagEndIdx) {
		this.urgFlagEndIdx = urgFlagEndIdx;
	}
	public int getAckFlagStartIdx() {
		return ackFlagStartIdx;
	}
	public void setAckFlagStartIdx(int ackFlagStartIdx) {
		this.ackFlagStartIdx = ackFlagStartIdx;
	}
	public int getAckFlagEndIdx() {
		return ackFlagEndIdx;
	}
	public void setAckFlagEndIdx(int ackFlagEndIdx) {
		this.ackFlagEndIdx = ackFlagEndIdx;
	}
	public int getPshFlagStartIdx() {
		return pshFlagStartIdx;
	}
	public void setPshFlagStartIdx(int pshFlagStartIdx) {
		this.pshFlagStartIdx = pshFlagStartIdx;
	}
	public int getPshFlagEndIdx() {
		return pshFlagEndIdx;
	}
	public void setPshFlagEndIdx(int pshFlagEndIdx) {
		this.pshFlagEndIdx = pshFlagEndIdx;
	}
	public int getRstFlagStartIdx() {
		return rstFlagStartIdx;
	}
	public void setRstFlagStartIdx(int rstFlagStartIdx) {
		this.rstFlagStartIdx = rstFlagStartIdx;
	}
	public int getRstFlagEndIdx() {
		return rstFlagEndIdx;
	}
	public void setRstFlagEndIdx(int rstFlagEndIdx) {
		this.rstFlagEndIdx = rstFlagEndIdx;
	}
	public int getSynFlagStartIdx() {
		return synFlagStartIdx;
	}
	public void setSynFlagStartIdx(int synFlagStartIdx) {
		this.synFlagStartIdx = synFlagStartIdx;
	}
	public int getSynFlagEndIdx() {
		return synFlagEndIdx;
	}
	public void setSynFlagEndIdx(int synFlagEndIdx) {
		this.synFlagEndIdx = synFlagEndIdx;
	}
	public int getFinFlagStartIdx() {
		return finFlagStartIdx;
	}
	public void setFinFlagStartIdx(int finFlagStartIdx) {
		this.finFlagStartIdx = finFlagStartIdx;
	}
	public int getFinFlagEndIdx() {
		return finFlagEndIdx;
	}
	public void setFinFlagEndIdx(int finFlagEndIdx) {
		this.finFlagEndIdx = finFlagEndIdx;
	}
	public int getWindowSizeStartIdx() {
		return windowSizeStartIdx;
	}
	public void setWindowSizeStartIdx(int windowSizeStartIdx) {
		this.windowSizeStartIdx = windowSizeStartIdx;
	}
	public int getWindowSizeEndIdx() {
		return windowSizeEndIdx;
	}
	public void setWindowSizeEndIdx(int windowSizeEndIdx) {
		this.windowSizeEndIdx = windowSizeEndIdx;
	}
	
	public int getTcpChecksumStartIdx() {
		return tcpChecksumStartIdx;
	}

	public void setTcpChecksumStartIdx(int tcpChecksumStartIdx) {
		this.tcpChecksumStartIdx = tcpChecksumStartIdx;
	}

	public int getTcpChecksumEndIdx() {
		return tcpChecksumEndIdx;
	}

	public void setTcpChecksumEndIdx(int tcpChecksumEndIdx) {
		this.tcpChecksumEndIdx = tcpChecksumEndIdx;
	}

	public int getUrgentPtrStartIdx() {
		return urgentPtrStartIdx;
	}
	public void setUrgentPtrStartIdx(int urgentPtrStartIdx) {
		this.urgentPtrStartIdx = urgentPtrStartIdx;
	}
	public int getUrgentPtrEndIdx() {
		return urgentPtrEndIdx;
	}
	public void setUrgentPtrEndIdx(int urgentPtrEndIdx) {
		this.urgentPtrEndIdx = urgentPtrEndIdx;
	}	
	
	public int getOptionsStartIdx() {
		return optionsStartIdx;
	}

	public void setOptionsStartIdx(int optionsStartIdx) {
		this.optionsStartIdx = optionsStartIdx;
	}

	public int getOptionsEndIdx() {
		return optionsEndIdx;
	}

	public void setOptionsEndIdx(int optionsEndIdx) {
		this.optionsEndIdx = optionsEndIdx;
	}

	private void setValue(int offset, short options){	

		this.setTcpHeaderStartIdx(offset);
		
		this.setTcpSrcPortStartIdx(offset);
		this.setTcpSrcPortEndIdx(offset 	+ 1 );	
		this.setTcpDstPortStartIdx(offset	+ 2 );
		this.setTcpDstPortEndIdx(offset		+ 3 );		
		this.setSeqNoStartIdx(offset+       + 4 );
		this.setSeqNoEndIdx(offset+         + 7 );
		this.setAckNoStartIdx(offset+       + 8 );
		this.setAckNoEndIdx(offset+         + 11 );	
		this.setTcpHeaderLenStartIdx(offset + 12 );
		this.setTcpHeaderLenEndIdx(offset	+ 12 );	
		this.setReservedStartIdx(offset	    + 12 );
		this.setReservedEndIdx(offset		+ 13 );
		this.setTcpFlagsStartIdx(offset		+ 13 );
		this.setTcpFlagsEndIdx(offset		+ 13 );
		this.setUrgFlagStartIdx(offset	    + 13 );
		this.setUrgFlagEndIdx(offset	    + 13 );
		this.setAckFlagStartIdx(offset	    + 13 );
		this.setAckFlagEndIdx(offset   	    + 13 );
		this.setPshFlagStartIdx(offset	    + 13 );
		this.setPshFlagEndIdx(offset		+ 13 );
		this.setRstFlagStartIdx(offset      + 13 );
		this.setRstFlagEndIdx(offset        + 13 );
		this.setSynFlagStartIdx(offset      + 13 );
		this.setSynFlagEndIdx(offset        + 13 );
		this.setFinFlagStartIdx(offset      + 13 );
		this.setFinFlagEndIdx(offset        + 13 );
		this.setWindowSizeStartIdx(offset   + 14 );
		this.setWindowSizeEndIdx(offset		+ 15 );
		this.setTcpChecksumStartIdx(offset  + 16 );
		this.setTcpChecksumEndIdx(offset	+ 17 );		
		this.setUrgentPtrStartIdx(offset	+ 18 );
		this.setUrgentPtrEndIdx(offset		+ 19 );
		
		if( options > 0){
			this.setOptionsStartIdx((offset	+ 19));	
			this.setOptionsEndIdx((offset 	+ 19 + options ));
			this.setTcpHeaderEndIdx(offset 	+ 19 + options );
		}else{
			this.setTcpHeaderEndIdx(offset 	+ 19 );
		}
			
	}
	
}
