/**
 * @author l k h
 * @since 2014-12-02
 * 		  2016-07-28
 * @description 상관분석 > 피해자 IP 팝업 
 */
define(function(require) {
	"use strict";

	var $ 					= require('jquery'),
		_ 					= require('underscore'),
		Backbone 			= require('backbone'),
		locale 				= require('i18n!nls/str'),
		dataExpression 		= require('utils/dataExpression'),
		VictimIpCollection 	= require('collections/detectionAnalysis/victimIpCollection'),
		Pagination 			= require('views/common/basicPagination'),
		TbIpRetrieve 		= require('views/tools/ipRetrievePopup');

	var Tpl 				= require('text!tpl/popup/destIpTabTemplate.html'),
		ItemTpl 			= require('text!tpl/popup/destIpPopupItemTemplate.html');
	
	var SrcIpListItemView 	= Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click #lcode"					: "popupLCode",
			"click #srcIp"					: "popupSourceIp",
			"click #destPort"				: "popupDestinationPort",
			"click #sum"					: "popupOriginal",
			"click .ipRetrievePopupLink" 	: "showIpRetrievePopup"	// ip조회 ( whois )
		},
		initialize: function(options) {
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;
		},
		render: function() {
			this.$el.html(this.template({
				data: {
					rNum: this.model.get('rNum'),
					dwDestinationIp: this.model.get('dwDestinationIp'),
					sLcodeCount: dataExpression.numberFormat(this.model.get('sLcodeCount')),
					sPortCount: dataExpression.numberFormat(this.model.get('sPortCount')),
					srcIpCount: dataExpression.numberFormat(this.model.get('srcIpCount')),
					nSum: dataExpression.numberFormat(this.model.get('nSum')),
					totalRate: dataExpression.getFormatPercentData(this.model.get('nSum'), this.model.get('totalNSum')),
					bps: dataExpression.getFormatTrafficData(this.model.get('bps'))
				},
				condition: this.searchCondition,
				locale: locale
			}));
			return this;
		},
		popupLCode: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				destIp: this.model.get('dwDestinationIp')
			});
			
			require(['views/popup/attackPopup'], function(AttackPopupView) {
				var attackPopupView = new AttackPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(attackPopupView);
			});
		},
		popupSourceIp: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				destIp: this.model.get('dwDestinationIp')
			});
			
			require(['views/popup/srcIpPopup'], function(SrcIpPopupView) {
				var srcIpPopup = new SrcIpPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(srcIpPopup);
			});
		},
		popupDestinationPort: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				destIp: this.model.get('dwDestinationIp')
			});
			
			require(['views/popup/portPopup'], function(PortPopupView) {
				var portPopup = new PortPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(portPopup);
			});
		},
		popupOriginal: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				destIp: this.model.get('dwDestinationIp')
			});
			
			require(['views/popup/originalLogPopup'], function(OriginalLogPopupView) {
				var originalLogPopup = new OriginalLogPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(originalLogPopup);
			});
		},
		showIpRetrievePopup:function() {
			var ip = $('.ipRetrievePopupLink', this.el).text();
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ip
				})
			});
		},
	});

	return Backbone.View.extend({
		className: 'tab-pane padding-r15 padding-l15',
		template: _.template(Tpl),
		initialize: function(options) {
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;

			this.collection = new VictimIpCollection();
			this.listenTo(this.collection, 'add', this.addOne);
			
			this.pagination = new Pagination({
				evt : this
			});
			this.bind('pagination', this.getDestinationIpCorrelationAnalysis, this);
			this.children = [];

			this.info = {
				key: this.cid,
				title: locale.victimIp
			};
		},
		render: function() {
			this.$el.html(this.template({
				condition: this.searchCondition,
				pathName: decodeURIComponent(this.searchCondition.pathName),
				locale: locale
			}));
			this.$el.prop('id', this.info.key);
			this.$tbody = $('.table tbody', this.el);
			
			$('.more-option', this.el).append(this.pagination.render().el);

			this.getDestinationIpCorrelationAnalysis();
			
			return this;
		},
		getDestinationIpCorrelationAnalysis: function(paging) {
			var thisView = this;
			var pagination = thisView.pagination.getPaginationRowValue();
			var rowSize = _.isUndefined(paging) ? pagination : paging;
			_.extend(thisView.searchCondition, rowSize, {
				destIp: $('#destIp', thisView.el).val()
			});
			this.$tbody.empty();
			this.collection.fetch({
				method: 'POST',
				url: 'api/selectVictimIpPopupList',
				contentType: 'application/json',
				data: JSON.stringify(thisView.searchCondition),
				beforeSend: function() {
					Backbone.Loading.setModalLoading($('.progressbar', thisView.el));
					$('.more-option', thisView.el).hide();
				},
				success: function(collection) {
					if (collection.length > 0) {
						thisView.$tbody.parent().removeClass("nodata");
						var firstModel = collection.at(0);
						$('#totalCount', thisView.el).text(firstModel.get('totalRowSize'));
						thisView.pagination.setTotalRowSize(firstModel.get('totalRowSize'));
					} else {
						thisView.$tbody.parent().addClass("nodata");
						$('#totalCount', thisView.el).text(0);
						thisView.pagination.setTotalRowSize(0);
					}
				},
				complete: function() {
					$('.more-option', thisView.el).show();
					Backbone.Loading.removeModalLoading($('.progressbar', thisView.el));
				}
			});
		},
		addOne: function(model) {
			var item = new SrcIpListItemView({
				model: model,
				searchCondition: this.searchCondition,
				tabView: this.tabView
			});
			this.$tbody.append(item.el);
			item.render();
			this.children.push(item);
		},
		onClose: function() {
			if (this.children.length > 0) {
				_.each(this.children, function(child) {
					child.close();
				});
			}
			this.pagination.close();
		},
		getInfo: function() {
			return this.info;
		}
	});
});
