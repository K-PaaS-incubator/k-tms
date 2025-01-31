define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var DetectionModel = require('models/preferences/detectionModel');
	
	return Backbone.Collection.extend({
		
		model : DetectionModel,
		comparator: 'strFilterViewName'
	});
	
});