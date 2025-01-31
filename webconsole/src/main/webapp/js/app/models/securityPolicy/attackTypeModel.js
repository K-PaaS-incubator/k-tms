/** 
 * 공격유형 select box 목록   
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			nClassType: '',
			strName: ''
		}
	});
});