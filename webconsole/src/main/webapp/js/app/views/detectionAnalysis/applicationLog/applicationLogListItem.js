define(function(require) {
	
	"use strict";

	// require library
	var $ 		 					= require('jquery'),
		Backbone 					= require('backbone'),
		locale 						= require('i18n!nls/str'),
		dataExpression 				= require('utils/dataExpression');
	
	var ApplicationHelpPopupView 	= require('views/popup/applicationHelpPopup'),
		TbIpRetrieve 				= require('views/tools/ipRetrievePopup');
		
	// require template
	var	tpl 						= require('text!tpl/detectionAnalysis/applicationLogListItem.html');
	
	return  Backbone.View.extend({
		
		tagName : 'tr',
		
		template : _.template(tpl),
		
		initialize: function(options) {
			this.searchCondition 	= options.searchCondition;
			this.listenTo(this.model, "change", this.render());
		},
		events : {
			'click .applicationName' 			: 'applicationHelpPopup',
			'click .ipRetrieveAttackPopupLink' 	: 'showAttackIpRetrievePopup',
			'click .ipRetrieveVictimPopupLink' 	: 'showVictimIpRetrievePopup'
		},
		render: function() {
			
			var vsensorName = this.model.get('vsensorName') ? this.model.get('vsensorName') : '-';
			var sensorName = this.model.get('sensorName') ? this.model.get('sensorName') : '-';
			
			this.$el.html(this.template({
				rNum: this.model.get('rNum'),
				lIndex: this.model.get('lIndex'),
				bType: dataExpression.getbTypeName(this.model.get('bType')), 
				tmLogTime : this.model.get('tmLogTime'),
				dwSourceIp: this.model.get('bIpType') == 4 ? this.model.get('dwSourceIp') : this.model.get('strSourceIp'),
				nSourcePort: this.model.get('nSourcePort'),
				nProtocol: dataExpression.getProtocolName(this.model.get('nProtocol')),
				deDestinationIp: this.model.get('bIpType') == 4 ? this.model.get('deDestinationIp') : this.model.get('strDestinationIp'),
				nDestinationPort: this.model.get('nDestinationPort'),
				vsensorName: vsensorName,
				sensorName: sensorName
			}));
			
			return this;
		},
		applicationHelpPopup: function() {
			
			_.extend(this.searchCondition, {
				'lIndex' : this.model.get('lIndex')
			});
			
			Backbone.ModalView.msg({
				size: 'medium-large',
				type: 'info',
				title: locale.applicationName+locale.detailInfo,	// 애플리케이션 등록정보
				body: new ApplicationHelpPopupView({
					menuType : "ApplicationDetection",
					//model: this.model,
					monitoringModel: this.model,
					searchCondition: this.searchCondition
				})
			});
		},
		showAttackIpRetrievePopup: function() {
			var ip = $('.ipRetrieveAttackPopupLink', this.el).text();
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ip
				})
			});
		},
		showVictimIpRetrievePopup: function() {
			var ip = $('.ipRetrieveVictimPopupLink', this.el).text();
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ip
				})
			});
		}
	});
});