/** 
 * sensor information Collection
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var SessionModel = require('models/preferences/sessionModel');
	
	return Backbone.Collection.extend({
		model : SessionModel,
		comparator: 'strFilterViewName'
	});
	
});

