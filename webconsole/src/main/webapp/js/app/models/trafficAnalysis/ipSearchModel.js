/**
 * ip Search model
 */
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
			
			startDateInput: null,
			endDateInput: null,
			
			toIpInput: null,
			fromIpInput: null
		}
	});
	
});