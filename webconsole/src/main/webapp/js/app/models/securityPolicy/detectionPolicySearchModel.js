/** 
 *
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		
		defaults: {
			// 검색어
			attackNameInput : '',
			attackTypeSelect : null,
			useSignature: '',
			unUseSignature: ''
			
		},
	});
});