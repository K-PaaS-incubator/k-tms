define(function(require) {
	
	"use strict";
	
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var SensorInboundModel = require('models/systemSettings/sensorInboundModel');
	
	return Backbone.Collection.extend({
		
		model: SensorInboundModel
	
	});
});