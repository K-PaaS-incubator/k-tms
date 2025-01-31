/** 
 * 계정 관리 
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var AccountModel = require('models/preferences/accountModel');
	
	return Backbone.Collection.extend({
		
		model : AccountModel
	});
	
});