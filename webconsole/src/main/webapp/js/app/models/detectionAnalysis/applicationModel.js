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
			bType: '',
			nSourcePort: '',
			nDestinationPort: '',
			nSum: 0,
			bps : 0,
			pps : 0,
			perNsum : 0,
			totalRowSize : '0',
			nProtocol: '',
			startDate:'',
			endDate:''
		}
	});
});