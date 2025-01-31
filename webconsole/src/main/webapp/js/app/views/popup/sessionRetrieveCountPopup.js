/**
 * sessionRetrieveCountPopup
 * session - client List
 * 
 * @author bhchoi84
 * @since 2015-07-22
 * @description Traffic > session > count
 */
define(function(require) {
	"use strict";

	var $ 								= require('jquery'),
		_ 								= require('underscore'),
		Backbone 						= require('backbone'),
		locale 							= require('i18n!nls/str'),
		dataExpression 					= require('utils/dataExpression'),
		Pagination 						= require('views/common/basicPagination'),
		TbIpRetrieve 					= require('views/tools/ipRetrievePopup');

	var SessionRetrieveCountCollection 	= require('collections/trafficAnalysis/sessionRetrieveCollection');
	
	var Tpl 							= require('text!tpl/popup/sessionRetrieveCountPopupTemplate.html'),			// tab 전체 template
		ItemTpl 						= require('text!tpl/popup/sessionRetrieveCountPopupItemTemplate.html');		// tab item template
	
	var SessionRetrieveCountPopupView = Backbone.View.extend({
		tagName: 'tr',
		template: _.template(ItemTpl),
		events: {
			"click .ipRetrievePopupLink" : "ipRetrievePopup"
		},
		initialize: function(options) {
			options = options || {};
			this.model = options.model;
			this.searchCondition = options.searchCondition;
			this.tabView = options.tabView;
		},
		render: function() {
			this.$el.html(this.template({
				data: {
					rNum: this.model.get('rNum'),
					client: this.model.get('client'),
					server: this.model.get('server'),
					clientBytes: dataExpression.getFormatTrafficData(this.model.get('clientBytes')),
					serverBytes: dataExpression.getFormatTrafficData(this.model.get('serverBytes')),
					totalBytes: dataExpression.getFormatTrafficData(this.model.get('totalBytes')),
					tmstart: this.model.get('tmstart')
				},
				condition: this.searchCondition,
				locale: locale
			}));
			return this;
		},
		ipRetrievePopup:function(){
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
			this.model 				= options.model;
			this.tabView 			= options.tabView;
			this.collection 		= new SessionRetrieveCountCollection();
			this.listenTo(this.collection, 'add', this.addOne);
			
			var sessionDirection = this.searchCondition.sessionDirection;
			var direction = sessionDirection == 0 ? this.model.get("client") : this.model.get("server"); 
			_.extend(this.searchCondition, { 
				"strIp2": direction,
				"serverPort": this.searchCondition.serverPort,
				"serverOrgPort": this.searchCondition.session,
				"session": this.searchCondition.session
			});
			
//			if(this.searchCondition.sessionDirection == 0){
//				_.extend(this.searchCondition, { "ip2": Backbone.Utils.Casting.convertIpToLong(this.model.get("client")),
//			/**
//			 * 수정자: leekyunghee
//			 * searchCon으로 넘겨받은 조건 보여주도록 변경								 
//			 */									//"serverOrgPort":this.model.get("serverOrgPort"),
//												//"serverPort":this.model.get("serverPort")});
//												"serverPort": this.searchCondition.serverPort,
//												"serverOrgPort": this.searchCondition.serverOrgPort,
//			});
//			}else{
//				_.extend(this.searchCondition, { "ip2": Backbone.Utils.Casting.convertIpToLong(this.model.get("server")),
//												 //"serverOrgPort":this.model.get("serverOrgPort"),
//												 //"serverPort":this.model.get("serverPort")});
//					 							"serverOrgPort":this.searchCondition.serverOrgPort,
//					 							"serverPort":this.searchCondition.serverPort
//			});
//			}
			
			this.pagination = new Pagination({
				evt : this
			});
			this.bind('pagination', this.search, this);
			this.children = [];
			
			var title = sessionDirection == 0 ? "Session - Client List" : "Session - Server List";

			this.info = {
				key: this.cid,
				title: title
			};
		},
		render: function() {
			this.$el.html(this.template({
				condition: this.searchCondition,
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
			var pagination = thisView.pagination.getPaginationRowValue();
			var rowSize = _.isUndefined(paging) ? pagination : paging;
			
			_.extend(thisView.searchCondition, rowSize);	
			
			thisView.$tbody.empty();
			thisView.collection.fetch({
				method: 'POST',
				url: 'api/trafficAnalysis/selectSessionClientCountPopupList',
				contentType: 'application/json',
				data: JSON.stringify(thisView.searchCondition),
				beforeSend: function() {
					Backbone.Loading.setModalLoading($('.progressbar', thisView.el));
					$('.more-option', thisView.el).hide();
				},
				success: function(collection) {
					if (collection.length > 0) {
						var firstModel = collection.at(0);
						$('#totalCount', thisView.el).text(firstModel.get('totalRowSize'));
						thisView.pagination.setTotalRowSize(firstModel.get('totalRowSize'));
					} else {
						$('#totalCount', thisView.el).text(0);
						thisView.pagination.setTotalRowSize(0);
                        $(".table", thisView.el).addClass("nodata");
					}
				},
				error: function(e) {
					console.log('error', e);
				},
				complete: function() {
					Backbone.Loading.removeModalLoading($('.progressbar', thisView.el));
					$('.more-option', thisView.el).show();
				}
			});
		},
		addOne: function(model) {
			var itemView = new SessionRetrieveCountPopupView({
				model: model,
				searchCondition: this.searchCondition,
				tabView: this.tabView
			});
			this.$tbody.append(itemView.el);
			itemView.render();
			this.children.push(itemView);
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
