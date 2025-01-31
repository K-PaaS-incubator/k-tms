/**
 * @author lee kyunghee
 * @since 2015-12-24
 * @description FileMeta collection 
*/
define(function(require) {
	
	"use strict";
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	// require model, collection, view
	var FileMetaModel = require('models/detectionAnalysis/fileMetaModel'); 
	
	return Backbone.Collection.extend({
		model : FileMetaModel
	});
	
});