/** 
 * 계정 그룹 
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var AccountGroupModel = require('models/preferences/accountGroupModel');
	
	return Backbone.Collection.extend({
		
		model : AccountGroupModel
	});
	
});