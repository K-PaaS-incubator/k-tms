define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var FileMetaModel = require('models/preferences/fileMetaModel');
	
	return Backbone.Collection.extend({
		
		initialize : function(){
		},
		model : FileMetaModel,
		comparator: 'strFilterViewName'
	});

});