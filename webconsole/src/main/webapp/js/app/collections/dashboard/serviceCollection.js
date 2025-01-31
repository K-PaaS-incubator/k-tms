define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone');
	
	// require i18n
	var locale = require('i18n!nls/str');
	
	var ServiceModel = require('models/dashboard/serviceModel');
	
	return Backbone.Collection.extend({
		
		url 	: 'api/dashboard/selectServiceTopN',
		
		model : ServiceModel
		
	});
});