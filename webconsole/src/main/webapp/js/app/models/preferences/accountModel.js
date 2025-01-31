/** 
 * 계정 정보  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			luserIndex: '',
			strId: '',
			strName: '',
			strPassport: '',
			sgroupType: '',
			strDescription: '',
			strCompany: '',
			strTelephone: '',
			strMobile: '',
			strEmail: '',
			bAccountStatus: '',
			nCategory: '',
			nRefIndex: '',
			nRole: '',
			nLogin: '',
			nLockout: '',
			nFailedLogin: '',
			tmLogin: '',
			role1 : '',
			role2 : '',
			role3 : ''
				
		}
	});
});