/**
 * ip Search model
 */
define(function(require){
	
	//"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults: {
			ip: 0,
			bps: 0,
			pps: 0,
			sumBps: 0,
			sumPps: 0,
			totalRowSize: 0,
			rNum : 0,
			transformativeBps: 0,
			transformativePps: 0,
			// service popup 조회 조건
			longIp : 0,
			startDateInput:'',
			endDateInput:'',
			
			// servicePopup 리스트
			protocol: '',
			port:0,
			srcBps:0,
			dstBps:0,
			srcPps:0,
			dstPps:0,
			
		}
	});
	
});