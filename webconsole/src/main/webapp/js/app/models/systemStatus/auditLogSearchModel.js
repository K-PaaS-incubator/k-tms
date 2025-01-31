/** 
 * @author 
 * @since 
 * @description 
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			auditType: null,
			simpleTimeSelect: '',
			startDateInput : '',
			endDateInput : '',
			strContent: '',
			strOperator:''
		}
	});
});