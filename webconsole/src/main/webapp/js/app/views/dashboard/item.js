/**
 * Item View
 */
define(function(require) {
	
	"use strict";
	
	var $ 						= require('jquery'),
		Backbone 				= require('backbone'),
		locale					= require('i18n!nls/str'),
		dataExpression 			= require('utils/dataExpression'),
		tplService				= require('text!tpl/dashboard/itemService.html'),
		tplEvent				= require('text!tpl/dashboard/itemEvent.html'),
		tplVictimIp				= require('text!tpl/dashboard/itemVictimIp.html'),
		tplAudit    			= require('text!tpl/dashboard/itemAudit.html'),
		tplApplication			= require('text!tpl/dashboard/itemApplication.html');
	
	var TabView 				= require('views/popup/tab'),
		OriginalLogPopupView 	= require('views/popup/originalLogPopup'),
		AttackHelpPopupView 	= require('views/popup/attackHelpPopup'),
		AttackTrendView 		= require('views/detectionAnalysis/attack/attackTrend'),
		VictimIpTrendView 		= require('views/detectionAnalysis/victimIp/victimIpTrend'),
		ServiceTrend 			= require('views/trafficAnalysis/service/serviceTrend'),
		TbIpRetrieve 			= require('views/tools/ipRetrievePopup');
	
	var ItemView =  Backbone.View.extend({
		
		tagName				: 'tr',
		templateService 	: _.template(tplService),
		templateEvent 		: _.template(tplEvent),
		templateVictimIp 	: _.template(tplVictimIp),
		templateAudit 		: _.template(tplAudit),
		templateApplication : _.template(tplApplication),
		
		initialize: function(options) {
			var type 		= options.type || '';
			var headerId 	= options.headerId || '';
			var model 		= options.model || '';
			this.type 		= type;
			this.headerId 	= headerId;
			this.model 		= model;	
		},
		
		events: {
			"click #originalLogPopup"			: "originalLogPopup",
			"click #attackName"					: "attackHelpPopup",
			'click .attackCountPopup'			: 'attackCountPopup',
			'click .victimIpAttackCountPopup' 	: "victimIpAttackCountPopup",
			'click .bpsPopup' 					: 'bpsPopup',
			"click .victimIpBpsPopup" 			: "victimIpBpsPopup",
			"click .ipRetrievePopupLink"		: 'showIpRetrievePopup'
				
		},
		
		
		serviceItemRender: function() {		
			var outputTable = this.templateService({
				model : this.model
			});

			this.$el.html(outputTable);
			return this;
		},
		
		eventItemRender: function() {
			this.model.set({
				lCode : this.model.get("lcode")
			});
				
			
			var outputTable = this.templateEvent({
				model : this.model
			});

			this.$el.html(outputTable);
			return this;
		},
		
		victimIpItemRender: function() {
			this.model.set({
				dwDestinationIp : this.model.get("dwDestinationIp")
			});
				
			
			var outputTable = this.templateVictimIp({
				model : this.model
			});

			this.$el.html(outputTable);
			return this;
		},

		auditItemRender: function() {		
			var outputTable =  "<li><span class='"+this.model.get("setType")+"'></span><a class='overflow-ellipsis' title='"+this.model.get("strContent")+"'>"+this.model.get("strContent")+"</a><span class='date'>"+this.model.get("tmOccur")+"</span></li>";
			return outputTable;
		},
//		auditItemRender: function() {
//			this.model.set({
//				strContent : this.model.get("strContent"),
//				tmOccur : this.model.get("tmOccur")
//			});
//			
//			var outputTable = this.templateAudit({
//				model : this.model
//			});
//			this.$el.html(outputTable);
//			return this;
//		},
		
		applicationItemRender: function() {
			var outputTable = this.templateApplication({
				model : this.model
			});

			this.$el.html(outputTable);
			return this;
		},
		
		originalLogPopup: function() {
			var searchCondition = {};
			
			_.extend(searchCondition, {
				destIp: this.model.get('dwDestinationIp'),
				startDateInput : this.model.get('startDateInput'),
				endDateInput : this.model.get('endDateInput'),
				attackNameInput: this.model.get('strTitle'),
				lCode: this.model.get('lcode'),
				lnetgroupIndex : this.model.get('lnetgroupIndex'), 
				lnetworkIndex : this.model.get('lnetworkIndex'), 
				lvsensorIndex : this.model.get('lvsensorIndex'), 
				lsensorIndex : this.model.get('lsensorIndex'),
				pathName : this.model.get('pathName'),
				targetType : 1,
				winBoundSelect : 0
			});	

			var tabView = new TabView();
			Backbone.ModalView.msg({
				size: 'large',
				type: 'search',
				title: locale.correlationAnalysis,
				body: tabView
			});
			var originalLogPopup = new OriginalLogPopupView({
				searchCondition: searchCondition,
				tabView: tabView 
			});
			tabView.addTab(originalLogPopup);
		},
		
		attackHelpPopup: function() {
			Backbone.ModalView.msg({
				size: 'medium-large',
				type: 'info',
				title: locale.attack+locale.detailInfo,// 공격등록정보
				body: new AttackHelpPopupView({
					searchType: 'attackHelp',
					model: this.model,
//                    menuType : 'correlation',
					searchCondition: {'lCode' : +this.model.get('lcode')}
				})
			});
		},
		
		attackCountPopup: function() {
			var searchCondition = {};
			_.extend(searchCondition, {
				startDateInput : this.model.get('startDateGraphInput'),
				endDateInput : this.model.get('endDateGraphInput'),
				lCode: this.model.get('lcode'),
				lnetgroupIndex : this.model.get('lnetgroupIndex'), 
				lnetworkIndex : this.model.get('lnetworkIndex'), 
				lvsensorIndex : this.model.get('lvsensorIndex'), 
				lsensorIndex : this.model.get('lsensorIndex'),
				pathName : this.model.get('pathName'),
				simpleTimeSelect : "12h", 
				targetTypeSelect : 0, 
				listViewInput : "defaultList",
				winBoundSelect : "0"
			});

			Backbone.ModalView.msg({
				size: 'medium-large',
				type: 'search',
				title: this.model.get('strTitle')+' '+locale.occurrenceTrend, //공격명 발생추이
				body: new AttackTrendView({
					searchType: 'attackCountTrafficChart',
					model: this.model,
					searchCondition: searchCondition
				})
			});
		},
		
		victimIpAttackCountPopup: function() {
			var searchCondition = {};
			_.extend(searchCondition, {
				startDateInput : this.model.get('startDateGraphInput'),
				endDateInput : this.model.get('endDateGraphInput'),
				destIp: this.model.get('dwDestinationIp'),
				lnetgroupIndex : this.model.get('lnetgroupIndex'), 
				lnetworkIndex : this.model.get('lnetworkIndex'), 
				lvsensorIndex : this.model.get('lvsensorIndex'), 
				lsensorIndex : this.model.get('lsensorIndex'),
				pathName : this.model.get('pathName'),
				simpleTimeSelect : "12h", targetTypeSelect : 0, listViewInput : "defaultList",
				winBoundSelect : "0"
			});
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: this.model.get('dwDestinationIp')+" " + locale.occurrenceTrend, // IP 발생추이
				body: new VictimIpTrendView({
					searchType: 'attackCountTrafficChart',
					model: this.model,
					searchCondition: searchCondition
				})
			});
		},
		
		bpsPopup: function() {
			var searchCondition = {};
			var nProtocolNum;
			nProtocolNum = dataExpression.getProtocolName(this.model.get('nprotocol'));

			_.extend(searchCondition, {
				startDateInput : this.model.get('startDateGraphInput'),
				endDateInput : this.model.get('endDateGraphInput'),
				nProtocol : this.model.get('nprotocol'),
				wService : this.model.get('wservice'),
				lnetgroupIndex : this.model.get('lnetgroupIndex'), 
				lnetworkIndex : this.model.get('lnetworkIndex'), 
				lvsensorIndex : this.model.get('lvsensorIndex'), 
				lsensorIndex : this.model.get('lsensorIndex'),
				pathName : this.model.get('pathName'),
				simpleTimeSelect : "12h", 
				targetTypeSelect : 0, 
				listViewInput : "defaultList",
				winBoundSelect : "0"
			});

			Backbone.ModalView.msg({
				size: 'medium-large',
				title: nProtocolNum+' '+this.model.get('wservice')+' '+locale.occurrenceTrend, // 프로토콜명 포트값 발생추이
				body: new ServiceTrend({
					searchType: 'bpsTrafficChart',
					model: this.model,
					searchCondition: searchCondition
				})
			});
		},
		
		victimIpBpsPopup: function() {
			var searchCondition = {};
			_.extend(searchCondition, {
				startDateInput : this.model.get('startDateGraphInput'),
				endDateInput : this.model.get('endDateGraphInput'),
				destIp: this.model.get('dwDestinationIp'),
				lnetgroupIndex : this.model.get('lnetgroupIndex'), 
				lnetworkIndex : this.model.get('lnetworkIndex'), 
				lvsensorIndex : this.model.get('lvsensorIndex'), 
				lsensorIndex : this.model.get('lsensorIndex'),
				pathName : this.model.get('pathName'),
				simpleTimeSelect : "12h", 
				targetTypeSelect : 0, 
				listViewInput : "defaultList",
				winBoundSelect : "0"
			});
			
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: this.model.get('dwDestinationIp')+" " + locale.occurrenceTrend, // IP 발생추이
				body: new VictimIpTrendView({
					searchType: 'bpsTrafficChart',
					model: this.model,
					searchCondition: searchCondition
				})
			});
		},
		
		showIpRetrievePopup: function() {
			var ip = $('.ipRetrievePopupLink', this.el).text();
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ip
				})
			});
		}
	});
	
	return ItemView;
});