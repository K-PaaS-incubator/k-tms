/**
 * DB사용량  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 		 		= require('jquery'),
		Backbone 		= require('backbone');
		
	// require utils
	var dataExpression 	= require('utils/dataExpression');
	var locale 			= require('i18n!nls/str');
	// require template
	var	tpl  			= require('text!tpl/systemStatus/dbUsageListItem.html');
	
	return Backbone.View.extend({
		
		template : _.template(tpl),
		
		render: function() {
			var thisView = this;
			
			$.when(
				thisView.$el.html(thisView.template({
					model : thisView.model.toJSON(),
					dataExpression : dataExpression,
					locale: locale
				}))
			).done(function() {
				thisView.chart(thisView.model);
			});
			
			return this;
		},
		chart: function(model){
			var thisView = this;
			//차트그리기
			this.dblPercent = dataExpression.getFormatPercent(model.get("dblUsed"), model.get("dblTotal"));
			
			this.chartView = Backbone.ChartView.MakeChart(thisView.$('#pieChart_'+model.get("strName")), {
				type: "PieDonut",
				chartName: 'pieChart_'+thisView.model.get("strName"),
				collection: [[model.get("dblUsed"),model.get("dblSpace")]],
				percentValue : thisView.dblPercent
			});
		}
	});
});