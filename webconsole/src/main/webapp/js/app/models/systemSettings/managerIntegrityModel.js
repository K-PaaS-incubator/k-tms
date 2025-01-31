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
			nStartIntegrity: '',
			nAutoIntegrity: '',
			nAutoIntegrityMin: '',
			changeInfo: false,
            managerIntegrityRtn:'' //저장 결과값
		}
	});
});