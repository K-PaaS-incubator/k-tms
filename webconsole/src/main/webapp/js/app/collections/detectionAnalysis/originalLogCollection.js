define(function(require) {
	
	"use strict";
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	// require model, collection, view
	var OriginalLogModel = require('models/detectionAnalysis/originalLogModel'); 
	
	return Backbone.Collection.extend({
		
		model : OriginalLogModel
	});
	
});