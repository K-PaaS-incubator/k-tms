/** 
 * yara rule 정책 등록정보  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex: '',
			groupIndex: '',
			groupName: ''
		}
	});
});