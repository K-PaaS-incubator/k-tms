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

	var Tpl 							= require('text!tpl/popup/applicationLogPopup.html'),			// port 팝업 조회 상단부분
		ItemTpl 						= require('text!tpl/popup/applicationLogPopupItem.html'); 		// port 팝업 table list 부분
	
	var ListItemView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			
		},
		initialize: function(options) {
			this.model = options.model;
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;
		},
		render: function() {
                        var vsensorName = this.model.get('vsensorName') ? this.model.get('vsensorName') : '-';
			var sensorName = this.model.get('sensorName') ? this.model.get('sensorName') : '-';
			
			this.$el.html(this.template({
				//model: this.model.toJSON(),
				rNum: this.model.get('rNum'),
				lIndex: this.model.get('lIndex'),
				nType: dataExpression.getbTypeName(this.model.get('nType')),
				bTypeCode: this.model.get('nType'),
				totalCount: dataExpression.getFormatTrafficData(this.model.get('lTotCount')),
				percentCount: dataExpression.getFormatPercentData(this.model.get('lTotCount'), this.model.get('sumCount')),
				tmLogTime : this.model.get('tmLogTime'),
				dwSourceIp: this.model.get('bIpType') == 4 ? this.model.get('dwSourceIp') : this.model.get('strSourceIp'),
				nSourcePort: this.model.get('nSourcePort'),
				nProtocol: dataExpression.getProtocolName(this.model.get('nProtocol')),
				deDestinationIp: this.model.get('bIpType') == 4 ? this.model.get('deDestinationIp') : this.model.get('strDestinationIp'),
				nDestinationPort: this.model.get('nDestinationPort'),
				destIpCount: this.model.get('destIpCount'),
				sourceIpCount: this.model.get('sourceIpCount'),
				vsensorName: vsensorName,
				sensorName: sensorName
			}));
			return this;
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
			this.bind('pagination', this.getPopupList, this);
			this.children = [];

			this.info = {
				key: this.cid,
				title: locale.applicationLog
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
			
			this.getPopupList();
			
			return this;
		},
		getPopupList: function(paging) {
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
				url: 'api/selectApplicationPopupList',
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
			var item = new ListItemView({
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
