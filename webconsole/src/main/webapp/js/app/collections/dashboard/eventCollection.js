define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone');
	
	// require i18n
	var locale = require('i18n!nls/str');
	
	var EventModel = require('models/dashboard/eventModel');
	
	return Backbone.Collection.extend({
		
		url 	: 'api/dashboard/selectEventTopN',
		
		model : EventModel
		
	});
});