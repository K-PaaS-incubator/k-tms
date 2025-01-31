define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone');
	
	// require i18n
	var locale = require('i18n!nls/str');

	return Backbone.Model.extend({
		defaults : {
			'time' : '',
			'data':0,
			'dataIn':0,
			'dataOut':0,
			'startDate' : '',
			'endDate' : ''	
		}
	});
});