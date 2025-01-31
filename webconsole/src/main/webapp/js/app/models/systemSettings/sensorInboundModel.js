define(function(require) {
	
	"use strict";
	
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
	
		defaults: {
			lsensorIndex: '',
			strEnable: '',
			strType: '',
			strNicInfo: '',
			strIpInfo: '',
			ip:'',
			nic:''
		},
		validate: function(attrs, options) {
			if(options.attribute == "nic") {
				this.set({
					nic : attrs.nic,
					changing: true
				});
				var result = Backbone.Utils.validation.validateIsNull2(attrs.nic);
				if(result == true) {
					return Backbone.Utils.validation.maxLength(attrs.nic, 32);
				}
				return result;
			}
		}
	});
});