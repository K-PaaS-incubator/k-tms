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
			rNum: '',
			lAuditLogIndex: '',
			tmOccur: '',
			strContent: '',
			strOperator:'',
			lType1: '',
			lType2: '',
			strComment:'',
			totalRowSize : '0'
		}
	});
});