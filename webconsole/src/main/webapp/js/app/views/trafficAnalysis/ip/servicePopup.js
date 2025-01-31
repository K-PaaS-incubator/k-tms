define(function(require) {
	
	"use strict";

	// require library
	var $ 							= require('jquery'),
		_ 							= require('underscore'),
		Backbone 					= require('backbone'),
		locale 						= require('i18n!nls/str'),
		dataExpression 				= require('utils/dataExpression'),
		Collection 					= require('collections/trafficAnalysis/ipCollection'),
		ServicePopupItem 			= require('views/trafficAnalysis/ip/servicePopupItem');

	// require template
	var Tpl 						= require('text!tpl/trafficAnalysis/ipServicePopup.html');

	return Backbone.View.extend({
		className: 'tab-content',
		template: _.template(Tpl),
		initialize: function(options) {
			options 				= options || {};
			this.ip 				= options.ip,
			this.startDate 			= options.startDate,
			this.endDate 			= options.endDate,
			this.searchCondition 	= options.searchCondition;
			this.collection 		= new Collection();
			this.listenTo(this.collection, 'add', this.addOne);
			this.ipType 			= options.ipType;
			this.lvsensorIndex 			= options.lvsensorIndex;
		},
		render: function() {
			this.$el.html(this.template({
				ip: this.ip,
				startDate: this.searchCondition.get('startDateInput'),
				endDate: this.searchCondition.get('endDateInput'),
				locale: locale,
				transformativeBps: this.searchCondition.get('transformativeBps'),
				transformativePps: this.searchCondition.get('transformativePps')
			}));
			this.getTop5List();
			
			return this;
		},
		getTop5List: function() {
//			var searchCondition = [];
//			searchCondition = _.extend(this.searchCondition, this.ipType);
			this.searchCondition.set({
				ipType: this.ipType,
                                lvsensorIndex: this.lvsensorIndex
			});
			var thisView = this;
			this.collection.fetch({
				method: 'POST',
				data: JSON.stringify(this.searchCondition),
				url: 'api/trafficAnalysis/selectTop5ServicePopup',
//				async : false,
				contentType: 'application/json',
				beforeSend: function() {
                                    Backbone.Loading.setModalLoading($('body'));
//					if (!$('.servicePopupProgressbar', thisView.el).hasClass('loading')) {
//						Backbone.Loading.setLoading($('.servicePopupProgressbar', thisView.el));
//					}
				},
				success: function(collection) {
					if(thisView.collection.length == 0) {
						$(".table", thisView.el).addClass("nodata");
					}
				},
				complete: function() {
                                    Backbone.Loading.removeModalLoading($('body'));
//					Backbone.Loading.removeLoading($('.servicePopupProgressbar', thisView.el));
				}
			});
		},
		addOne: function(model) {
			var self = this;
			model.set({
				protocol : dataExpression.getProtocolName(parseInt(model.get('protocol'))),
				bps : dataExpression.getFormatTrafficData(model.get('bps')),
				srcBps : dataExpression.getFormatTrafficData(model.get('srcBps')),
				dstBps : dataExpression.getFormatTrafficData(model.get('dstBps')),
				pps : dataExpression.getFormatTrafficData(model.get('pps')),
				srcPps : dataExpression.getFormatTrafficData(model.get('srcPps')),
				dstPps : dataExpression.getFormatTrafficData(model.get('dstPps')),
				shareBps: dataExpression.getFormatPercent(model.get('bps'), self.searchCondition.get('bps')),
				sharePps: dataExpression.getFormatPercent(model.get('pps'), self.searchCondition.get('pps'))
			});
			
			var servicePopupItem = new ServicePopupItem({
				model : model
			});
			this.$('#servicePopupTbody').append(servicePopupItem.render().el);
		},
	});
	
});
