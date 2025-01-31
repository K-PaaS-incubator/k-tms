/**
 * 트래픽분석 > 프로토콜 
 * table 내의 값을 append해주는 js
 * 
 */
define(function(require){
	"use strict";
	
	// require library
	var $ 								= require('jquery'),
		_ 								= require('underscore'),
		Backbone 						= require('backbone'),
		locale 							= require('i18n!nls/str'),
		dataExpression 					= require('utils/dataExpression'),
		TrafficAnalysisPopupView 		= require('views/popup/trafficAnalysisPopup'),
		alertMessage 					= require('utils/AlertMessage');
	
	// require template
	var tpl 							= require('text!tpl/trafficAnalysis/protocolItem.html');
	
	return Backbone.View.extend({ 
		
		tagName : 'tr',
		
		template : _.template(tpl),
		
		events : {
			'click .bpsPopup' 			: 'bpsPopup',
			'click .ppsPopup' 			: 'ppsPopup',
			'click #graphItemCheck'		: 'checkGraphItemCount'
		},
		
		initialize : function(options) {
			this.listType = options.listType;

			this.model.set({
				bpsView: dataExpression.getFormatTrafficData(this.model.get("bps")),
				ingressBpsView: dataExpression.getFormatTrafficData(this.model.get("ingressBps")),
				egressBpsView: dataExpression.getFormatTrafficData(this.model.get("egressBps")),
				ppsView: dataExpression.getFormatTrafficData(this.model.get("pps")),
				ingressPpsView: dataExpression.getFormatTrafficData(this.model.get("ingressPps")),
				egressPpsView: dataExpression.getFormatTrafficData(this.model.get("egressPps")),
			});
			
			this.protocolSearchModel = options.searchModel;
			this.listenTo(this.model, "change", this.render());
			
			this.parentEl = options.parentEl;
		},
		render: function() {
			this.$el.html(this.template({
				locale: locale,
				model: this.model.toJSON(),
				highLightEl: this.protocolSearchModel.get('sortSelect'),
				isHighLight: Backbone.TemplateHelper.isHighLight
			}));
			$('span', this.el).tooltip();
			
			return this;
		},
		
		bpsPopup: function() {
			var searchCondition = {};
			_.extend(searchCondition, this.protocolSearchModel.toJSON(), {
				nProtocol : this.model.get('nProtocol'),
				ucType : this.model.get('ucType'),
			});
			
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: locale.protocol+" "+this.model.get('strName')+" " +locale.traffic+" "+ locale.occurrenceTrend, // 프로토콜 TCP(프로토콜종류) 트래픽 발생추이
				body: new TrafficAnalysisPopupView({
					searchType: 'bpsTrafficChart',
					model: this.model,
					searchCondition: searchCondition
				})
			});
		},
		ppsPopup: function() {
			var searchCondition = {};
			_.extend(searchCondition, this.protocolSearchModel.toJSON(), {
				nProtocol : this.model.get('nProtocol'),
				ucType : this.model.get('ucType'),
			});
			
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: locale.protocol+" "+this.model.get('strName')+" " +locale.traffic+" "+ locale.occurrenceTrend, // 프로토콜 TCP(프로토콜종류) 트래픽 발생추이
				body: new TrafficAnalysisPopupView({
					searchType: 'ppsTrafficChart',
					model: this.model,
					searchCondition: searchCondition
				})
			});
		},
		checkGraphItemCount: function(event) {
			var checkedItem = [];
			$("#graphItemCheck:checked", this.parentEl).each(function(i) {
				checkedItem[i] = ($(this).val());
			});
			
			if(checkedItem.length > 5) {
				alertMessage.infoMessage(locale.ExceededTheNumberOfItems, 'info', '', 'small');
				$(event.currentTarget).prop('checked', false);
			}
		}
	});
});