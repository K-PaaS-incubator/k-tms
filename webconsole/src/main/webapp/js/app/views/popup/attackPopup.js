/**
 * @author 	l k h 
 * @since  	2014-12-02
 *         	2015-07-10  
 *          2016-07-28      
 * @description 상관분석 > 공격 건수 팝업 컨텐츠, item view 
 */
define(function(require) {
	
	"use strict";

	var $ 							= require('jquery'),
		_ 							= require('underscore'),
		Backbone 					= require('backbone'),
		locale 						= require('i18n!nls/str'),
		dataExpression 				= require('utils/dataExpression'),
		VictimIpCollection 			= require('collections/detectionAnalysis/victimIpCollection'),
		Pagination 					= require('views/common/basicPagination'),
		AttackHelpPopupView 		= require('views/popup/attackHelpPopup');

	var Tpl 						= require('text!tpl/popup/attackTabTemplate.html'),
		ItemTpl 					= require('text!tpl/popup/attackPopupItemTemplate.html');	// UI template
	
	var SrcIpListItemView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click #srcIp"			: "popupSourceIp",
			"click #destIp"			: "popupDestinationIp",
			"click #destPort"		: "popupDestinationPort",
			"click #sum"			: "popupOriginal",
			"click .attackName"		: "attackHelpPopup"		// 공격명 정보 팝업
		},
		initialize: function(options) {
			options 				= options || {};
			this.searchCondition 	= options.searchCondition;
			this.tabView 			= options.tabView;
		},
		render: function() {			
			this.$el.html(this.template({
				data: {
					rNum: this.model.get('rNum'),
					attackName: this.model.get('strTitle'),
					srcIpCount: dataExpression.getFormatTrafficData(this.model.get('srcIpCount')),
					destIpCount: dataExpression.getFormatTrafficData(this.model.get('destIpCount')),
					sPortCount: dataExpression.getFormatTrafficData(this.model.get('sPortCount')),
					nSum: dataExpression.getFormatTrafficData(this.model.get('nSum')),
					totalRate: dataExpression.getFormatPercentData(this.model.get('nSum'), this.model.get('totalNSum')),
					bps: dataExpression.getFormatTrafficData(this.model.get('bps'))
				},
				condition: this.searchCondition,
				locale: locale
			}));
			return this;
		},
		attackHelpPopup: function() {
			Backbone.ModalView.msg({
				size: 'medium-large',
				type: 'info',
				title: locale.attack+locale.detailInfo,	// 공격등록정보
				body: new AttackHelpPopupView({
					searchType: 'attackHelp',
					model: this.model,
					searchCondition: {'lCode' : +this.model.get('lCode')}
				})
			});
		},
		popupDestinationIp: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				attackNameInput: this.model.get('strTitle'),
				lCode: this.model.get('lCode')
			});
			
			require(['views/popup/destIpPopup'], function(DestIpPopupView) {
				var destIpPopup = new DestIpPopupView({
					searchCondition: searchCon,
					tabView: thisView.tabView 
				});
				thisView.tabView.addTab(destIpPopup);
			});
		},
		popupSourceIp: function() {
			var thisView = this;
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, {
				attackNameInput: this.model.get('strTitle'),
				lCode: this.model.get('lCode')
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
				attackNameInput: this.model.get('strTitle'),
				lCode: this.model.get('lCode')
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
				attackNameInput: this.model.get('strTitle'),
				lCode: this.model.get('lCode')
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
			this.bind('pagination', this.getAttackCorrelationAnalysis, this);
			this.children = [];

			this.info = {
				key: this.cid,
				title: locale.attackName
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

			this.getAttackCorrelationAnalysis();
			
			return this;
		},
		getAttackCorrelationAnalysis: function(paging) {
			var self = this;
			// 상관분석 팝업에서 입력받은 값으로 검색 하는 방식 -> 탐지 분석 화면에서 검색 조건으로 담은 모델을 전달 받아서 조회 
			var attackVal = this.searchCondition.attackNameInput;	// 공격자(ip)
			var pagination = self.pagination.getPaginationRowValue();
			var rowSize = paging == undefined ? pagination : paging;
			
			_.extend(self.searchCondition, rowSize, { attackNameInput: attackVal });		
		
			this.$tbody.empty();
			
			this.collection.fetch({
				method: 'POST',
				url: 'api/selectAttackPopupList',
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
						$('#totalCount', self.el).text(firstModel.get('totalRowSize'));
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
