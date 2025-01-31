define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone'),
		LoginStatusModel = require('models/loginStatus'),
		tpl = require('text!tpl/presence.html'),
		locale = require('i18n!nls/str');

	return Backbone.View.extend({
		tagName: "li",
		template: _.template(tpl),
		initialize: function() {
			this.listenTo(this.model, 'change', this.render);
			this.listenTo(this.model, 'destroy', this.remove);
		},
		render: function() {
			var controller = 'glyphicon-user threat-bg-lv1';
			if (this.model.get('controller')) {
				controller = 'glyphicon-cog threat-bg-lv4';
			}
			var loginDate = new Date(this.model.get('loginDate'));
			this.$el.html(this.template({
				userName: this.model.get('userName'),
				loginDate: loginDate.toLocaleString(),
				controller: controller,
				strController: locale.controller
			}));
			return this;
		}
	});
});
