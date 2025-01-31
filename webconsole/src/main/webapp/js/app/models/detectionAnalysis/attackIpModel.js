/**
 * 
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults: {
			// 리스트 
			rNum 			: 0, 	//LINDEX		순위
			prevRNum 		: 0, 	// 공격 IP 		순위  (exprot위해 추가)
			prevBps 		: 0, 	// 트래픽(bps) 	변화량 (exprot위해 추가)	
			bSeverity 		: 0,	//BSEVERITY 	위험도
			dwSourceIp 		: null,	//DWSOURCEIP	// 공격자IP
			strNationIso 	: null,	//STRNATIONISO	// 국가
			sLcodeCount 	: 0,	//LCODE			// 공격
			sPortCount		: 0,	//NDESTPORT		// 피해포트
			bps 			: 0,	//DBLBPS		// bps
			pps 			: 0,
			nSum 			: 0,	//NSUM			// 공격횟수
			victimPort 		: 0,	//NPROTOCOL		// 피해자??
			perNsum 		: 0,
			totalRowSize 	: 0
		}
	});
	
});