/**
 * @author l k h  
 * @since 2014-12-02
 * 		  2016-07-28	
 * @description 상관분석 > 피해포트 팝업 컨텐츠, item view 
 */
define(function(require) {
	
	"use strict";

	var $ 							= require('jquery'),
		_ 							= require('underscore'),
		Backbone 					= require('backbone'),
		locale 						= require('i18n!nls/str'),
		dataExpression 				= require('utils/dataExpression'),
		VictimPortPopupCollection 	= require('collections/detectionAnalysis/victimPortPopupCollection'),
		Pagination 					= require('views/common/basicPagination');

	var Tpl 						= require('text!tpl/popup/portTabTemplate.html'),			// port 팝업 조회 상단부분
		ItemTpl 					= require('text!tpl/popup/portPopupItemTemplate.html'); 	// port 팝업 table list 부분
	
	var PortListItemView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click #lcode"			: "popupLCode",			// 추적 팝업을 띄우기 위해 table Item 클릭
			"click #srcIp"			: "popupSourceIp",
			"click #destIp"			: "popupDestinationIp",
			"click #sum"			: "popupOriginal"
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
					nProtocol: dataExpression.getProtocolName(this.model.get('nProtocol')),
					//nDestPort: this.model.get('nDestPort'),
					nDestPort: dataExpression.getPortFormat(this.model.get('nDestPort')),
					sLcodeCount: dataExpression.getFormatTrafficData(this.model.get('sLcodeCount')),
					srcIpCount: dataExpression.numberFormat(this.model.get('srcIpCount')),
					destIpCount: dataExpression.numberFormat(this.model.get('destIpCount')),
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
				nProtocol: this.model.get('nProtocol'),
				nProtocolView: dataExpression.getProtocolName(this.model.get('nProtocol')),
				destPortInput: this.model.get('nDestPort')
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
				nProtocol: this.model.get('nProtocol'),
				nProtocolView: dataExpression.getProtocolName(this.model.get('nProtocol')),
				destPortInput: this.model.get('nDestPort')
			});
			require(['views/popup/srcIpPopup'], function(SrcIpPopupView) {
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
				nProtocol: this.model.get('nProtocol'),
				nProtocolView: dataExpression.getProtocolName(this.model.get('nProtocol')),
				destPortInput: this.model.get('nDestPort')
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
				nProtocol: this.model.get('nProtocol'),
				nProtocolView: dataExpression.getProtocolName(this.model.get('nProtocol')),
				destPortInput: this.model.get('nDestPort')
			});
			require(['views/popup/originalLogPopup'], function(OriginalLogPopupView) {
				var originalLogPopup = new OriginalLogPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(originalLogPopup);
			});
		},
	});
	
	// 페이지가 한번 로딩된 후 실행
	return Backbone.View.extend({
		className: 'tab-pane padding-r15 padding-l15',
		template: _.template(Tpl),
		searchCondition : {},
		initialize: function(options) {
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;

			this.collection = new VictimPortPopupCollection();
			this.listenTo(this.collection, 'add', this.addOne);
			
			this.pagination = new Pagination({
				evt : this
			});
			this.bind('pagination', this.getPortPopupList, this);
			this.children = [];

			this.info = {
				key: this.cid,
				title: locale.victimPort
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
				url: 'api/victimPort/selectVictimPortPopupList',
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
						$('#totalCount', thisView.el).text(firstModel.get('totalRowSize'));
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
