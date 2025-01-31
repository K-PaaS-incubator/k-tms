/**
 * service Search model
 */
define(function(require) {
	
	"use strict";
	
	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');

	return Backbone.Model.extend({
		defaults: {
			lnetgroupIndex: 0,
			lnetworkIndex: 0,
			lvsensorIndex: 0,
			lsensorIndex: null,
			
			startDateInput: null,
			endDateInput: null,
			
			destPortInput: null,
			protocolSelect: null,
			winBoundSelect: null,
			thresholdSelect: null,
			thresholdNumInput: null,
			sortSelect: null
		}
	});
	
});