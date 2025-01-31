/**
 * Service List Collection
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var ServiceModel = require('models/trafficAnalysis/serviceModel');
	
	return Backbone.Collection.extend({
		model : ServiceModel
	});
	
});