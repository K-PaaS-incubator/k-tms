/** 
 * 계정 그룹 정보  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex: '',
			strName: '',
			nacIndexCount: '',
			strAccIndex: '',
			checked: false
		}
	});
});