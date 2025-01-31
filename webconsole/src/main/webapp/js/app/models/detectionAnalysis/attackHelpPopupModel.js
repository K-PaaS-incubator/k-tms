define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');

	return Backbone.Model.extend({
		defaults : {
			// 등록정보 (탐지정보에서 공통으로 사용되는 변수가 있음) 
			lCode: null,
			strTitle: null, 
			strAttackType: null,
			bSeverity: 0,
			strSummary: null,
			strDescription: null, 
			strFalsePositive: null,
			strSolution: null,
			strReference: null,
			strCveId: null,
			strbId: null,
			strVul: null,
			strNotVul: null,
			strAddrspoof: null,
			strDetect: null,
			signatureRule: null,
			rev: null,
			
			// 탐지정보 
			lIndex: null,
			tmStart: null,
			tmEnd: null,
			dwSourceIp: null,
			deDestinationIp: null,
			nProtocol: null,
			bType: null,
			lvsensorIndex: null,
			vsensorName: null,
			strSourceMac: null,
			strDestinationMac: null,
			nSourcePort: null,
			nDestinationPort: null,
			dwPacketCounter: null,
			deSrcPortCounter: null,
			dwDstPortCounter: null,
			dwSrcIpCounter: null,
			dwDstIpCounter: null,
			nTtl: null,
			lAlertResponse1: null,
			lResetResponse1: null,
			lEmailResponse1: null,
			lSnmpResponse1: null,
			lIwResponse1: null,
			LIwResponse2: null,
			lFwResponse1: null,
			lFwResponse2: null,
			dwEventNum: null,
			wInbound: null,
			ucCreateLogType: null,
			wVlanInfo: null,
			dwPktSize: null,
			dwMaliciousSrvFrame: null,
			dwMaliciousCliFrame: null,
			dwMaliciousSrvByte: null,
			dwMaliciousCliByte: null,
			ucIntrusionDir: null,
			ucAccessDir: null,
			lSrcNetIndex: null,
			srcNetworkName: null,
			lDstNetIndex: null,
			dstNetworkName: null,
			lSrcUserIndex: null,
			lDstUserIndex: null,
			lUrlIndex: null,
			strSrcNationIso: null,
			strDestNationIso: null,
			lSensorIndex: null,
			sensorName: null,
			tmDbTime: null
		}
	});

});
