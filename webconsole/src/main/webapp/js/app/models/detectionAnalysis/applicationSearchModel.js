define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		
		defaults: {
			// 검색어
			lnetgroupIndex: 0,
			lnetworkIndex: 0,
			lvsensorIndex: 0,
			lsensorIndex: null,
			pathName: '',
			startDateInput: '',
			endDateInput: '',
			bType: '',  			// 타입 http, dns, tls
			simpleTimeSelect: '',
			destPortInput: null, 	// 출발지 포트
			attackPortInput: null, 	// 도착지 포트
			toIpInput: null,
			fromIpInput: null
		},
	});
});