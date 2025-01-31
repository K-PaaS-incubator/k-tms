define(function(require) {

	"use strict";

	// require library
	var $ 			= require('jquery'),
		_ 			= require('underscore'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		
		url : 'api/selectOrg',
		
		defaults: {
			'strName': '',
			'lvsensorIndex': 0,
			'eventCount': 0,
			'eventDblbps': 0,
			'dblbps': 0
		}
	});
});