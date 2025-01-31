/**
 * @description 서비스 팝업 item
 */
define(function(require) {
	
	"use strict";
	
	// require library
	var $ 			= require('jquery'),
		_ 			= require('underscore'),
		Backbone 	= require('backbone'),
		locale 		= require('i18n!nls/str');
	
	// require template
	var tpl 		= require('text!tpl/trafficAnalysis/ipServicePopupItem.html');
	
	return Backbone.View.extend({ 
		template : _.template(tpl),
		tagName : 'tr',
		render: function(model) {
			this.$el.html(this.template({
				locale: locale,
				model: this.model.toJSON()
			}));
			
			return this;
		}
	});
});