/**
 * 
 */
define(function(require) {

	"use strict";

	// require library
	var Backbone = require('backbone');
	
	var LoginStatusModel = require('models/loginStatus');
	
	return Backbone.Collection.extend({
		
		model: LoginStatusModel
		
	});
});