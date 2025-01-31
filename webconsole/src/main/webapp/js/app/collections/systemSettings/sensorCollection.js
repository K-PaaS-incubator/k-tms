/** 
 * sensor information Collection
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var SensorModel = require('models/systemSettings/sensorModel');
	
	return Backbone.Collection.extend({
		model : SensorModel,
		comparator: 'lIndex'
	});
	
});