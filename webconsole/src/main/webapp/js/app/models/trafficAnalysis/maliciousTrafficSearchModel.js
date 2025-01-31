define(function(require) {
	
	"use strict";
	
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
			sortSelect: null
		}
	});
	
});