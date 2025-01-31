/**
 * application 출발 및 대상 IP 조회 
 */

define(function(require) {

	"use strict";
	
	var $ 						= require('jquery'),
		Backbone 				= require('backbone'),
		locale 					= require('i18n!nls/str'),
		dataExpression 			= require('utils/dataExpression'),
		ApplicationCollection 	= require('collections/detectionAnalysis/applicationCollection'),
		TbIpRetrieve 			= require('views/tools/ipRetrievePopup'),
		Pagination 				= require('views/common/basicPagination');
		
	var	Tpl 					= require('text!tpl/popup/applicationDstIpListPopup.html'),
		ItemTpl 				= require('text!tpl/popup/applicationDstIpListPopupItem.html');	// UI template
	
	var DestIpListItemView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click #destIpPopup"				: "popupDestinationList",
			'click .ipRetrievePopupLink' 	: 'showIpRetrievePopup'
		},
		initialize: function(options) {
			options 				= options || {};
			this.searchCondition 	= options.searchCondition;
			this.tabView 			= options.tabView;
		},
		render: function() {
			this.$el.html(this.template({
				model: this.model.toJSON(),
				condition: this.searchCondition,
				//deDestinationIp: this.model.get('bIpType') == 4 ? this.model.get('deDestinationIp') : this.model.get('strDestinationIp'),
				deDestinationIp: this.model.get('deDestinationIp'),
				strDestinationIp: this.model.get('strDestinationIp'),
				ipType: this.searchCondition.ipType,
				locale: locale
			}));
			return this;
		},
		popupDestinationList: function() {
			//2018.03.06 대상클릭이벤트시(대상IP수 클릭) 조회 조건에 ip정보 조회하도록 값을 설정!!!
			this.ipType = 4;
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				destIp: this.ipType == 4 ? this.model.get('deDestinationIp') : this.model.get('strDestinationIp'),
				nDestinationPort: this.model.get('nDestinationPort')
			});
			
			require(['views/popup/applicationLogPopup'], function(ApplicationLogPopupView) {
				var applicationSrcPortPopup = new ApplicationLogPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(applicationSrcPortPopup);
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
	
	return Backbone.View.extend({
		className: 'tab-pane padding-r15 padding-l15',
		template: _.template(Tpl),
		initialize: function(options) {
			this.model = options.model;
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;

			this.collection = new ApplicationCollection();
			this.listenTo(this.collection, 'add', this.addOne);
			this.pagination = new Pagination({
				evt : this
			});
			this.bind('pagination', this.getApplicationDestIpCorrelationAnalysis, this);
			this.children = [];
			this.info = {
				key: this.cid,
				title: locale.destIpList
			};
		},
		render: function() {
			this.$el.html(this.template({
				condition: this.searchCondition,
				//model: this.model.toJSON(),
				model: this.model,
				pathName: decodeURIComponent(this.searchCondition.pathName),
				//bType: dataExpression.getbTypeName(this.model.get('bType')),
				bType: dataExpression.getbTypeName(this.searchCondition.bType),
				locale: locale
			}));
			this.$el.prop('id', this.info.key);
			this.$tbody = $('.table tbody', this.el);
			
			$('.more-option', this.el).append(this.pagination.render().el);
			
			this.getApplicationDestIpCorrelationAnalysis();
			
			return this;
		},
		getApplicationDestIpCorrelationAnalysis: function(paging) {
			var self = this;
			// 상관분석 팝업에서 입력받은 값으로 검색 하는 방식 -> 탐지 분석 화면에서 검색 조건으로 담은 모델을 전달 받아서 조회 
			var bType = this.searchCondition.bType;	
			var pagination = self.pagination.getPaginationRowValue();
			var rowSize = paging == undefined ? pagination : paging;
			
			_.extend(self.searchCondition, rowSize, {bType: bType});
			
			this.$tbody.empty();
			
			this.collection.fetch({
				method: 'POST',
				url: 'api/selectApplicationDestinationIpList',
				contentType: 'application/json',
				data: JSON.stringify(self.searchCondition),
				beforeSend: function() {
					Backbone.Loading.setLoading($('.progressbar', self.el));
					$('.more-option', self.el).hide();
				},
				success: function(collection) {
					if (collection.length > 0) {
						self.$tbody.parent().removeClass("nodata");
						var firstModel = collection.at(0);
						$('#totalCount', self.el).text(dataExpression.numberFormat2(firstModel.get('totalRowSize')));
						self.pagination.setTotalRowSize(firstModel.get('totalRowSize'));
					} else {
						self.$tbody.parent().addClass("nodata");
						$('#totalCount', self.el).text(0);
						self.pagination.setTotalRowSize(0);
					}
				},
				complete: function() {
					Backbone.Loading.removeLoading($('.progressbar', self.el));
					$('.more-option', self.el).show();
				}
			});
		},
		addOne: function(model) {
			var item = new DestIpListItemView({
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