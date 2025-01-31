/**
 * @author l k h 
 * @since 2014-12-02
 * 		  2016-07-28	
 * @description 상관분석 > 공격자
 */
define(function(require) {
	
	"use strict";

	var $ 								= require('jquery'),
		_ 								= require('underscore'),
		Backbone 						= require('backbone'),
		locale 							= require('i18n!nls/str'),
		dataExpression 					= require('utils/dataExpression'),
		AttackIpListCollection 			= require('collections/detectionAnalysis/attackIpListCollection'),
		Pagination 						= require('views/common/basicPagination'),
		TbIpRetrieve 					= require('views/tools/ipRetrievePopup'),
		TabView 						= require('views/popup/tab');

	var Tpl 							= require('text!tpl/popup/srcIpTabTemplate.html'),				// 공격팝업 리스트 template		
		ItemTpl 						= require('text!tpl/popup/srcIpPopupItemTemplate.html');	
	
	var SrcIpListItemView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click #lcode"					: "popupLCode",
			"click #destPort"				: "popupDestinationPort",
			"click #destIp"					: "popupDestinationIp",
			"click #sum"					: "popupOriginal",
			'click .ipRetrievePopupLink' 	: 'showIpRetrievePopup'
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
					dwSourceIp: this.model.get('dwSourceIp'),
					sLcodeCount: dataExpression.getFormatTrafficData(this.model.get('sLcodeCount')),
					sPortCount: dataExpression.getFormatTrafficData(this.model.get('sPortCount')),
					destIpCount: dataExpression.getFormatTrafficData(this.model.get('destIpCount')),
					nSum: dataExpression.getFormatTrafficData(this.model.get('nSum')),
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
				srcIp: this.model.get('dwSourceIp')
			});
			
			require(['views/popup/attackPopup'], function(AttackPopupView) {
				var attackPopupView = new AttackPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(attackPopupView);
			});
		},
		popupDestinationPort: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				srcIp: this.model.get('dwSourceIp')
			});
			
			require(['views/popup/portPopup'], function(PortPopupView) {
				var portPopup = new PortPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(portPopup);
			});
		},
		popupDestinationIp: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				srcIp: this.model.get('dwSourceIp')
			});
			
			require(['views/popup/destIpPopup'], function(DestIpPopupView) {
				var destIpPopup = new DestIpPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(destIpPopup);
			});
		},
		popupOriginal: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				srcIp: this.model.get('dwSourceIp')
			});
			
			require(['views/popup/originalLogPopup'], function(OriginalLogPopupView) {
				var originalLogPopup = new OriginalLogPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(originalLogPopup);
			});
		},
		showIpRetrievePopup: function(){
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
			options 				= options || {};
			this.searchCondition 	= options.searchCondition;
			this.tabView 			= options.tabView;

			this.collection = new AttackIpListCollection();
			this.listenTo(this.collection, 'add', this.addOne);
			
			this.pagination = new Pagination({
				evt : this
			});
			this.bind('pagination', this.getSourceIpCorrelationAnalysis, this);
			this.children = [];

			this.info = {
				key: this.cid,
				title: locale.attackerIp
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

			this.getSourceIpCorrelationAnalysis();
			
			return this;
		},
		getSourceIpCorrelationAnalysis: function(paging) {
			var thisView = this;
			var pagination = thisView.pagination.getPaginationRowValue();
			var rowSize = _.isUndefined(paging) ? pagination : paging;
			
			_.extend(thisView.searchCondition, rowSize, {
				srcIp: $('#srcIp', thisView.el).val()
			});
		
			this.$tbody.empty();
			
			this.collection.fetch({
				method: 'POST',
				url: 'api/selectAttackerIpPopupList',
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
						$('#totalCount', thisView.el).text(0);
						thisView.pagination.setTotalRowSize(0);
						thisView.$tbody.parent().addClass("nodata");
					}
				},
				complete: function() {
					Backbone.Loading.removeModalLoading($('.progressbar', thisView.el));
					$('.more-option', thisView.el).show();
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
