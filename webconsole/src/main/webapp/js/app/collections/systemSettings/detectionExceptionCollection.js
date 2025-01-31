/** 
 * 탐지예외 
 */
define(function(require) {
	
	"use strict";
	
	// require library
	var $ 						= require('jquery'),
		Backbone 				= require('backbone');
	
	var DetectionExceptionModel = require('models/systemSettings/detectionExceptionModel');
	
	return Backbone.Collection.extend({
		model : DetectionExceptionModel,
		
		completed: function() {
			return this.where({completed: true});
		}
	});
	
});