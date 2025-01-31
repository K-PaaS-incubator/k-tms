/** 
 * @author leekyunghee
 * @since 2014-12-09
 * @description 원본로그 조회를 위한 모델
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults: {
			// 리스트 
			rNum : 0, 
			bSeverity : 0,	// 위험도
			deDestinationIp : '', // 피해호스트 
			dwSourceIp: '',	// 침해호스트 
			strTitle : '',  // 공격명
			nSourcePort: '',
			nDestinationPort: '',
			dwpktsize: '',
			dweventnum: '',
			nSum: 0,
			bps : 0,
			pps : 0,
			perNsum : 0,
			totalRowSize : '0',
			dwpacketcounter: '',
			nProtocol: '',
			startDate:'',
			endDate:''
		}
	});
});