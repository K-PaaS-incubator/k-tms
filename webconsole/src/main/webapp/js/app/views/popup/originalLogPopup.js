define(function(require) {
	
//	"use strict";
	
	var $ 						= require('jquery'),
		Backbone 				= require('backbone'),
		locale 					= require('i18n!nls/str'),
		dataExpression 			= require('utils/dataExpression'),
		OriginalLogCollection 	= require('collections/detectionAnalysis/originalLogCollection'),
		PacketModel 			= require('models/detectionAnalysis/originalLogModel'),
		Pagination 				= require('views/common/basicPagination'),
		TbIpRetrieve 			= require('views/tools/ipRetrievePopup'),
		AttackHelpPopupView 	= require('views/popup/attackHelpPopup'),
		RawPacketPopupView 		= require('views/popup/rawPacketPopup');
	
	var Tpl 					= require('text!tpl/popup/originalLogTabTemplate.html'),
		ItemTpl 				= require('text!tpl/popup/originalLogPopupItemTemplate.html');
	
	var OriginalLogListItemView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click #lcode"						: "popupLCode",
			"click #destPort"					: "popupDestinationPort",
			"click #destIp"						: "popupDestinationIp",
			"click #sum"						: "popupOriginal" ,
			"click #helpTd"						: "popupHelp",
			'click .ipRetrieveAttackPopupLink' 	: 'showAttackIpRetrievePopup',
			'click .ipRetrieveVictimPopupLink' 	: 'showVictimIpRetrievePopup',
			'click #helpTd' 					: "attackHelpPopup",
			"click .packetRawTd"				: "rawPacketPopup"
		},
		initialize: function(options) {
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;
		},
		render: function() {
                    var nSourcePort = this.model.get('nSourcePort');
                    if (parseInt(nSourcePort) == 0) {
                        nSourcePort = "-";
                    }
                    var nDestinationPort = this.model.get('nDestinationPort');
                    if (parseInt(nDestinationPort) == 0) {
                        nDestinationPort = "-";
                    }
			this.$el.html(this.template({
				data: {
					//bSeverity: dataExpression.severityData(this.model.get('bSeverity')),
					bSeverity: this.model.get('bSeverity'),
					rNum: this.model.get('rNum'),
					strTitle: this.model.get('strTitle'),
					dwSourceIp: this.model.get('dwSourceIp'),
					nSourcePort: nSourcePort,
					deDestinationIp: this.model.get('deDestinationIp'),
					nDestinationPort: nDestinationPort,
					dweventnum : this.model.get('dweventnum'),
					dwpktsize : dataExpression.getFormatTrafficData(this.model.get('dwpktsize')),
					endDate : this.model.get('endDate'),
					rawdata: this.model.get('rawdata')
				},
				condition: this.searchCondition,
				locale: locale
			}));
			
			return this;
		},
		showAttackIpRetrievePopup: function() {
			var ip = $('.ipRetrieveAttackPopupLink', this.el).text();
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ip
				})
			});
		},
		showVictimIpRetrievePopup: function() {
			var ip = $('.ipRetrieveVictimPopupLink', this.el).text();
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ip
				})
			});
		},
		attackHelpPopup : function() {
			Backbone.ModalView.msg({
				size: 'medium-large',
				type: 'info',
				title: locale.attack+locale.detailInfo,	// 공격등록정보
				body: new AttackHelpPopupView({
					menuType: 'correlation', 						// 상관분석 메뉴에서 공격 상세 정보 조회시 필요함.
					searchType: 'attackHelp',
					model: this.model,
					searchCondition: {'lCode' : +this.model.get('lCode')}
				})
			});
		},
		rawPacketPopup: function() {
			var searchCondition = {};
			_.extend(searchCondition, this.model.toJSON(), {
				startDateInput: this.model.get('startDate'),
				endDateInput: this.model.get('endDate'),
				lCode : this.model.get('lCode')
			});
			Backbone.ModalView.msg({
				size: 'medium-large',
				type: 'info',
				title: locale.rawPacket,
				body: new RawPacketPopupView({
					model: this.model,
					searchCondition: searchCondition
				})
			});
		}
	});
	
	return Backbone.View.extend({
		className: 'tab-pane padding-r15 padding-l15',
		template: _.template(Tpl),
		events: {
		},
		initialize: function(options) {
			options = options || {};
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;

			this.collection = new OriginalLogCollection();
			this.listenTo(this.collection, 'add', this.addOne);
			
			this.pagination = new Pagination({
				evt : this
			});
			this.bind('pagination', this.search, this);
			this.children = [];
			this.packetModel = new PacketModel();

			this.info = {
				key: this.cid,
				title: locale.originalLog
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

			this.search();
			
			return this;
		},
		search: function(paging) {
			var thisView = this;
			this.paging = paging;
			if (this.paging == undefined || this.paging.startRowSize == undefined || this.paging.rowSize == undefined) {
				this.paging =  this.pagination.getPaginationRowValue();
			}
			
			this.$tbody.empty();
			var searchCon = {};
			_.extend(searchCon, this.searchCondition, this.paging, {});
			
			this.collection.fetch({
				method: 'POST',
				url: 'api/selectOriginalLogPopupList',
				contentType: 'application/json',
				data: JSON.stringify(searchCon),
				beforeSend: function() {
					Backbone.Loading.setModalLoading($('.progressbar', thisView.el));
					$('.more-option', self.el).hide();
				},
				success: function(collection) {
					if (collection.length > 0) {
						thisView.$tbody.parent().removeClass("nodata");
						var firstModel = collection.at(0);
						$('#totalCount', thisView.el).text(firstModel.get('totalRowSize'));
						thisView.pagination.setTotalRowSize(firstModel.get('totalRowSize'));
						$('#totalCount', thisView.el).digits();
					} else {
						$('#totalCount', thisView.el).text(0);
						thisView.pagination.setTotalRowSize(0);
						thisView.$tbody.parent().addClass("nodata");
					}
				},
				complete: function() {
					Backbone.Loading.removeModalLoading($('.progressbar', thisView.el));
					$('.more-option', self.el).show();
				}
			});
		},
		addOne: function(model) {
			var item = new OriginalLogListItemView({
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