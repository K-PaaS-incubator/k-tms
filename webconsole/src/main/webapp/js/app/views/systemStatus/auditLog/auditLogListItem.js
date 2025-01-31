/**
 * 감사로그 서브 리스트 
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 		 = require('jquery'),
		Backbone = require('backbone'),
		dataExpression = require('utils/dataExpression');
		
	// require template
	var	tpl  = require('text!tpl/systemStatus/auditLogListItem.html');
	
	return  Backbone.View.extend({
		
		tagName : 'tr',
		
		template : _.template(tpl),
		
		initialize: function(options) {
			this.auditLogSearchModel = options.searchModel;
			this.listenTo(this.model, "change", this.render());
		},
		render: function() {
//			this.$el.html(this.template(this.model.toJSON()));
			this.$el.html(this.template({
				model: this.model.toJSON()
//				dataFormatter: dataExpression.auditTypeCheck
			}));
			
			return this;
		}
	});
});