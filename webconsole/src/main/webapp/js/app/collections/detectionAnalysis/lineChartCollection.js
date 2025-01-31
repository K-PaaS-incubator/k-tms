define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone');
	
	// require i18n
	var locale = require('i18n!nls/str');
	
	var LineChartModel = require('models/detectionAnalysis/lineChartModel');
	
	return Backbone.Collection.extend({
		model : LineChartModel
	});
});