/** 
 * sensor information Collection
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var SystemConfModel = require('models/systemSettings/systemConfModel');
	
	return Backbone.Collection.extend({
		model : SystemConfModel,
		comparator: 'lIndex'
	});
	
});