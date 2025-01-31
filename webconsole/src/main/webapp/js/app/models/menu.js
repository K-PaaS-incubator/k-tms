define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	// require i18n
	var locale = require('i18n!nls/str');

	var MenuModel = Backbone.Model.extend({
		defaults: {
			menuNo: '',
			menuKey: '',
			menuName: '',
			menuNameEng: '',
			displayOrder: '',
			enabled: '',
			upperMenuNo: '',
			roleNo: '',
			url: ''
		}
	});

	return MenuModel;

});