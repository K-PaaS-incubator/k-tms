/** 
 * 서비스 타겟 데이터 
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			sessionIndex: '',
			strVsensorName: '',
			nPort: '',
			nRenewOption: '',
			sessionVsensorIndex: '',
			nCheck:''
		}
	});
});