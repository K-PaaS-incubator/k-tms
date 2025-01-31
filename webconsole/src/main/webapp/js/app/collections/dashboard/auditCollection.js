define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone');
	
	// require i18n
	var locale = require('i18n!nls/str');
	
	var AuditModel = require('models/dashboard/auditModel');
	
	return Backbone.Collection.extend({
		
		url 	: 'api/selectAuditTopN',
		
		model : AuditModel
		
	});
});