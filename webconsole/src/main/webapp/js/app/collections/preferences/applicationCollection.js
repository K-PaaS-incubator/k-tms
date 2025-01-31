define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var ApplicationModel = require('models/preferences/applicationModel');
	
	return Backbone.Collection.extend({
		
		initialize : function(){
		},
		model : ApplicationModel,
		comparator: 'strFilterViewName'
	});
	
});