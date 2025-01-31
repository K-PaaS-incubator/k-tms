/** 
 * sensor information Collection
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var BackupFileModel = require('models/systemSettings/backupFileModel');
	
	return Backbone.Collection.extend({
		model : BackupFileModel,
	});
	
});