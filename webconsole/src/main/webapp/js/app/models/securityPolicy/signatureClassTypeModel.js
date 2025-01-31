/** 
 * 사용자정의 시그니처 유형 
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