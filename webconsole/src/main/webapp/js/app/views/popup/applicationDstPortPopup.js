/**
 * @author l k h  
 * @since 2017-04-17
 * @description 상관분석 > 대상 포트 팝업 컨텐츠, item view 
 */
define(function(require) {
	
	"use strict";

	var $ 								= require('jquery'),
		_ 								= require('underscore'),
		Backbone 						= require('backbone'),
		locale 							= require('i18n!nls/str'),
		dataExpression 					= require('utils/dataExpression'),
		ApplicationCollection 			= require('collections/detectionAnalysis/applicationCollection'),
		Pagination 						= require('views/common/basicPagination');

	var Tpl 							= require('text!tpl/popup/applicationDstPortPopup.html'),			// port 팝업 조회 상단부분
		ItemTpl 						= require('text!tpl/popup/applicationDstPortPopupItem.html'); 		// port 팝업 table list 부분
	
	var PortListItemView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click #destIp"			: "popupDestinationIp"
		},
		initialize: function(options) {
			this.model = options.model;
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;
		},
		render: function() {
			this.$el.html(this.template({
				model: this.model.toJSON(),
				data: {
					rNum: this.model.get('rNum'),
					nProtocol: dataExpression.getProtocolName(this.model.get('nProtocol')),
					//nDestPort: this.model.get('nDestPort'),
					//nDestPort: dataExpression.getPortFormat(this.model.get('nDestPort')),
					//nDestinationPort
					nDestinationPort: dataExpression.getPortFormat(this.model.get('nDestinationPort')),
					dPortCount: dataExpression.numberFormat(this.model.get('dPortCount')),
					//destIpCount: dataExpression.numberFormat(this.model.get('destIpCount')),
					dstIpCount: dataExpression.numberFormat(this.model.get('dstIpCount'))
				},
				condition: this.searchCondition,
				locale: locale
			}));
			return this;
		},
		popupSourceIp: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
//				nProtocol: this.model.get('nProtocol'),
//				nProtocolView: dataExpression.getProtocolName(this.model.get('nProtocol')),
//				destPortInput: this.model.get('nDestPort')
				srcIp: this.model.get('dwSourceIp'),
				nSourcePort: this.model.get('nSourcePort')
			});
			require(['views/popup/applicationSrcIpListPopup'], function(SrcIpPopupView) {
				var srcIpPopup = new SrcIpPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(srcIpPopup);
			});
		},
		popupDestinationIp: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
//				nProtocol: this.model.get('nProtocol'),
//				nProtocolView: dataExpression.getProtocolName(this.model.get('nProtocol')),
//				destPortInput: this.model.get('nDestPort')
				destIp: this.model.get('deDestinationIp'),
				nDestinationPort: this.model.get('nDestinationPort')
			});
			require(['views/popup/applicationDstIpListPopup'], function(DestIpPopupView) {
				var destIpPopup = new DestIpPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(destIpPopup);
			});
		},
	});
	
	// 페이지가 한번 로딩된 후 실행
	return Backbone.View.extend({
		className: 'tab-pane padding-r15 padding-l15',
		template: _.template(Tpl),
		searchCondition : {},
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
			this.bind('pagination', this.getPortPopupList, this);
			this.children = [];

			this.info = {
				key: this.cid,
				title: locale.destinationPort
			};
		},
		render: function() {
			this.$el.html(this.template({
				//model: this.model.toJSON(),
				model: this.model,
				pathName: decodeURIComponent(this.searchCondition.pathName),
				//bType: dataExpression.getbTypeName(this.model.get('bType')),
				bType: dataExpression.getbTypeName(this.searchCondition.bType),
				condition: this.searchCondition,
				locale: locale
			}));
			this.$el.prop('id', this.info.key);
			this.$tbody = $('.table tbody', this.el);
			
			$('.more-option', this.el).append(this.pagination.render().el);
			
			this.getPortPopupList();
			
			return this;
		},
		getPortPopupList: function(paging) {
			var thisView = this;
			// 분석 화면에서 검색 조건으로 담은 모델을 전달 받아서 조회
			var destportVal  = this.searchCondition.destPortInput;
			// 최초 팝업을 로드할 경우 pagination 모듈에서 getPaginationRowValue를 조회 
			// 이후 페이징 넘버 클릭시 다음 startRow 와 endRow를 가져온다.
			var pagination = thisView.pagination.getPaginationRowValue();
			var rowSize = paging == undefined ? pagination : paging;

			_.extend(thisView.searchCondition, rowSize, { destPortInput: destportVal });
			thisView.$tbody.empty();
			
			thisView.collection.fetch({
				method: 'POST',
				url: 'api/selectApplicationDstPortPopupList',
				contentType: 'application/json',
				data: JSON.stringify(thisView.searchCondition),
				beforeSend: function() {
					Backbone.Loading.setLoading($('.progressbar', thisView.el));
					$('.more-option', thisView.el).hide();
				},
				success: function(collection) {
					if (collection.length > 0) {
						thisView.$tbody.parent().removeClass("nodata");
						var firstModel = collection.at(0);
						$('#totalCount', thisView.el).text(dataExpression.numberFormat2(firstModel.get('totalRowSize')));
						thisView.pagination.setTotalRowSize(firstModel.get('totalRowSize'));
					} else {
						$('#totalCount', thisView.el).text(0);
						thisView.pagination.setTotalRowSize(0);
						thisView.$tbody.parent().addClass("nodata");
					}
				},
				complete: function() {
					Backbone.Loading.removeLoading($('.progressbar', thisView.el));
					$('.more-option', thisView.el).show();
				}
			});
		},
		addOne: function(model) {
			var item = new PortListItemView({
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
