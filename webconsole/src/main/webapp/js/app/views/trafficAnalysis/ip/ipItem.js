/**
 * 트래픽분석 > IP
 * table 내의 값을 append해주는 js
 * 
 */
define(function(require) {
	"use strict";
	
	// require library
	var $ 				= require('jquery'),
		_ 				= require('underscore'),
		Backbone 		= require('backbone'),
		locale 			= require('i18n!nls/str'),
		alertMessage 	= require('utils/AlertMessage');
		
	var ServicePopup 	= require('views/trafficAnalysis/ip/servicePopup'),
		TbIpRetrieve 	= require('views/tools/ipRetrievePopup');
	
	// require template
	var tpl 			= require('text!tpl/trafficAnalysis/ipItem.html');
	
	return Backbone.View.extend({ 
		
		tagName : 'tr',
		template : _.template(tpl),
		events: {
			'click #graphItemCheck'			: 'checkGraphItemCount',
			'click .servicePopupBtn'		: 'showServicePopup',
			'click .ipRetrievePopupLink' 	: 'showIpRetrievePopup'
		},
		initialize : function(options) {
			this.parentEl = options.parentEl;
			this.searchModel = options.searchModel;
//			console.log('item>>>>>>>>>> '+ JSON.stringify(this.searchModel));
//			console.log('item>>>>>>>>>> '+ JSON.stringify(this.searchModel.get('ipType')));
		},
		render : function() {
			this.$el.append(this.template({
				model: this.model.toJSON()
			}));
			return this;
		},
		showServicePopup: function() {
			var searchCondition = [];
			
			var longIp = Backbone.Utils.Casting.convertIpToLong(this.model.get('ip'));
			
			this.model.set({
				longIp : longIp
			});
			searchCondition = _.extend(searchCondition, this.model);
			
			Backbone.ModalView.msg({
				size: 'medium-large',
				type: 'info',
				title: this.model.get('ip')+' Top5 '+ locale.service,
				body: new ServicePopup({
					startDate: this.model.get('startDateInput'),
					endDate: this.model.get('endDateInput'),
					ip: this.model.get('ip'),
					searchCondition: searchCondition,
					ipType: this.searchModel.get('ipType'),
					lvsensorIndex: this.searchModel.get('lvsensorIndex')
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