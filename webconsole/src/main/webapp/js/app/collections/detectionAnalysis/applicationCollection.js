define(function(require) {
	
	"use strict";
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	// require model, collection, view
	var ApplicationModel = require('models/detectionAnalysis/applicationModel'); 
	
	return Backbone.Collection.extend({
		model : ApplicationModel
	});
	
});