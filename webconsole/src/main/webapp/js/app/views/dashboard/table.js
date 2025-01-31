/**
 * @description dashboard Table View
 */
define(function(require) {

	"use strict";

	// require library
	var $ 				= require('jquery'),
		_ 				= require('underscore'),
		Backbone 		= require('backbone'),
		bootstrap 		= require('bootstrap'),
		d3 				= require('d3');
	
	var locale 			= require('i18n!nls/str');
	
	var ItemView 		= require('views/dashboard/item');
	var dataExpression 	= require('utils/dataExpression');
	
	// require template
	var tplService 		= require('text!tpl/dashboard/tableService.html');
	var tplEvent 		= require('text!tpl/dashboard/tableEvent.html');
	var tplVictimIp		= require('text!tpl/dashboard/tableVictimIp.html');
	var tplAudit 		= require('text!tpl/dashboard/tableAudit.html');
	//var tplAudit 		= require('text!tpl/dashboard/tableAudit3.html');		// suricata 제외용 대시보드 
	var tplApplication  = require('text!tpl/dashboard/tableApplication.html');

	var TableView = Backbone.View.extend({
		types 				: [ 'Service', 'Event', 'Audit', 'VictimIp', 'Application' ],
		templateService 	: _.template(tplService),
		templateEvent 		: _.template(tplEvent),
		templateVictimIp 	: _.template(tplVictimIp),
		templateAudit 		: _.template(tplAudit),
		templateApplication : _.template(tplApplication),

		initialize : function(options) {
			this.type 					= options.hasOwnProperty('type') ? options.type : 'Service';
			this.title 					= options.title || '';
			this.titleSub 				= options.titleSub || '';		
			this.headerName 			= options.headerName || '';
			this.collection 			= options.collection || '';
			this.startDateInput 		= options.startDateInput || '';
			this.endDateInput 			= options.endDateInput || '';
			this.startDateGraphInput 	= options.startDateGraphInput || '';
			this.endDateGraphInput 		= options.endDateGraphInput || '';
			this.lnetgroupIndex 		= options.lnetgroupIndex,
			this.lnetworkIndex 			= options.lnetworkIndex,
			this.lvsensorIndex 			= options.lvsensorIndex,
			this.lsensorIndex 			= options.lsensorIndex,
			this.pathName 				= options.pathName;
			
			this.etcSumValue;
			this.etcBpsValue;
		},
		events: {
			"click #auditAction" 		: "goAuditAction",
			"click #auditWarning" 		: "goAuditWarning",
			"click #auditError" 		: "goAuditError"
		},
		
		makeServiceTable: function() {
			var collection = this.collection;
			var tableClass = "";
		
			if (collection.length == 0) {
				tableClass = "nodata-dashboard";
			}
			
			var outputTable = this.templateService({
				headerName : this.headerName,
				tableClass : tableClass
			});

			this.$el.html(outputTable);
			
			collection.each(this.serviceItemRender, this);
			
			if (collection.length < 5 && collection.length > 0) {
				for (var i = 0; i < 5-collection.length; i++) {
					this.noItemRender();
				}
			}
			return this;
		},
		
		serviceItemRender: function(model) {
			model.set({
				strProtocol: dataExpression.getProtocolName(model.get('nprotocol')),
				strDblbps: dataExpression.getFormatTrafficData(model.get('dblbps')),
				//percentbps: dataExpression.getFormatPercentData(model.get('dblbps'), model.get('totalbps')),
				percentbps: dataExpression.getFormatPercentData(model.get('sumDblbps'), model.get('totalbps')),
				startDateInput : this.startDateInput,
				endDateInput : this.endDateInput,
				startDateGraphInput : this.startDateGraphInput,
				endDateGraphInput : this.endDateGraphInput,
				lnetgroupIndex: this.lnetgroupIndex,
				lnetworkIndex: this.lnetworkIndex,
				lvsensorIndex: this.lvsensorIndex,
				lsensorIndex: this.lsensorIndex,
				pathName: this.pathName
			});

			var itemView = new ItemView({model: model});

			$('#tbody', this.el).append(itemView.serviceItemRender().el);
		},
		
		makeEventTable: function() {
			var collection = this.collection;
			var tableClass = "";
			
			if (collection.length == 0) {
				tableClass = "nodata-dashboard";
			}
			var outputTable = this.templateEvent({
				headerName : this.headerName,
				tableClass : tableClass
			});

			this.$el.html(outputTable);
			collection.each(this.eventItemRender, this);
			
			if (collection.length < 5  && collection.length > 0) {
				for(var i = 0; i < 5-collection.length; i++) {
					this.noItemRender();
				}
			}
			return this;
		},
		
		eventItemRender: function(model) {
			model.set({
				strEventCount: dataExpression.getFormatTrafficData(model.get('eventCount')),
				percentCount: dataExpression.getFormatPercentData(model.get('eventCount'), model.get('totalCount')),
				startDateInput : this.startDateInput,
				endDateInput : this.endDateInput,
				startDateGraphInput : this.startDateGraphInput,
				endDateGraphInput : this.endDateGraphInput,
				lnetgroupIndex: this.lnetgroupIndex,
				lnetworkIndex: this.lnetworkIndex,
				lvsensorIndex: this.lvsensorIndex,
				lsensorIndex: this.lsensorIndex,
				pathName: this.pathName
			});

			var itemView = new ItemView({model: model});
		
			$('#tbody', this.el).append(itemView.eventItemRender().el);
		}, 
		
		makeVictimIpTable: function() {
			var collection = this.collection;
			var tableClass = "";
			
			this.etcNo = collection.length + 1;
			
			if (collection.length == 0) {
				tableClass = "nodata-dashboard";
			} else {
				this.totSumValue = collection.at(0).get('totalNSum');
				this.etcSumValue = collection.at(0).get('totalNSum');
				
				this.totBpsValue = collection.at(0).get('totalBps');
				this.etcBpsValue = collection.at(0).get('totalBps');
			}
			var outputTable = this.templateVictimIp({
				headerName : this.headerName,
				tableClass : tableClass
			});

			this.$el.html(outputTable);
			collection.each(this.victimIpItemRender, this);
			
			if (collection.length < 5  && collection.length > 0) {
				for (var i = 0; i < 5-collection.length; i++) {
					this.noItemPlusRender();
				}
			}
			// 기타 주석
//			if(collection.length > 0){
//				this.etcVictimIpItemRender();
//			}
			
			return this;
		},
		
		victimIpItemRender: function(model) {
			model.set({
				strEventCount: dataExpression.getFormatTrafficData(model.get('nSum')),
				percentCount: dataExpression.getFormatPercentData(model.get('nSum'), model.get('totalNSum')),
				strDblbps: dataExpression.getFormatTrafficData(model.get('bps')),
				percentbps: dataExpression.getFormatPercentData(model.get('bps'), model.get('totalBps')),
				rNum: model.get('rNum'),
				startDateInput : this.startDateInput,
				endDateInput : this.endDateInput,
				startDateGraphInput : this.startDateGraphInput,
				endDateGraphInput : this.endDateGraphInput,
				lnetgroupIndex: this.lnetgroupIndex,
				lnetworkIndex: this.lnetworkIndex,
				lvsensorIndex: this.lvsensorIndex,
				lsensorIndex: this.lsensorIndex,
				pathName: this.pathName
			});
			
			this.etcSumValue -= model.get('nSum');
			this.etcBpsValue -= model.get('bps');

			var itemView = new ItemView({model: model});
		
			$('#tbody', this.el).append(itemView.victimIpItemRender().el);
		},
		
		etcVictimIpItemRender: function() {
			$('#tbody', this.el).append('<tr><td class="overflow-ellipsis"><table class="padding-l5 pull-left"><tr><td width=10 height=10 class="level-narrow top-bg-lv'+this.etcNo+'"></td></tr></table>&nbsp;<span>기타</span></td><td height="35"></td><td class="align-right">'+dataExpression.getFormatTrafficData(this.etcSumValue)+'</td><td class="align-right chart-text-5 font-size-11">'+dataExpression.getFormatPercentData(this.etcSumValue, this.totSumValue)+'</td><td></td><td class="align-right">'+dataExpression.getFormatTrafficData(this.etcBpsValue)+'</td><td class="align-right chart-text-5 font-size-11">'+dataExpression.getFormatPercentData(this.etcBpsValue, this.totBpsValue)+'</td></tr>');
		},
		
		makeAuditTable: function() {
			var collection = this.collection;
			var tableClass = "";
			
			if (collection.length < 1) {
				tableClass = "nodata-dashboard";
			}
			var outputTable = this.templateAudit({
				locale : locale,
				tableClass : tableClass
			});

			this.$el.html(outputTable);

			if (collection.length == 0) {
				$("#auditAction", this.el).append("0");
				$("#auditWarning", this.el).append("0");
				$("#auditError", this.el).append("0");
			}
			collection.each(this.auditItemRender, this);
			
			if (collection.length < 5) {
				for (var i = 0; i < 5-collection.length; i++) {
					this.noAuditItemRender();
				}
			}
			
			return this;
		},

		auditItemRender: function(model) {
			$('#auditAction', this.el).empty().append(model.get("cntAction")).digits();
			$('#auditError', this.el).empty().append(model.get("cntError")).digits();
			$('#auditWarning', this.el).empty().append(model.get("cntWarning")).digits();
			
			//suricata 제외
//			model.set({
//				strContent : model.get("strContent"),
//				tmOccur : model.get("tmOccur")
//			});
                        model.set({
				setType : 'log-'+dataExpression.auditEngCheck(model.get("ltype1")),
				ltype1: dataExpression.auditTypeCheck(model.get("ltype1"))
			});
			var itemView = new ItemView({model: model});
			$('#liItem', this.el).append(itemView.auditItemRender());
			
			//suricata 제외
			//$('#tableAuditItem', this.el).append(itemView.auditItemRender().el);
		},
		
		// 건수 클릭 시 새창띄우기로 변경 
		goAuditAction: function() {
//			location.href = 'home.html#systemStatus_auditLog/auditAction';
			window.open('home.html#systemStatus_auditLog/auditAction', '_.blank');
		},
		
		goAuditWarning: function() {
//			location.href = 'home.html#systemStatus_auditLog/auditWarning';
			window.open('home.html#systemStatus_auditLog/auditWarning', '_.blank');
		},
		
		goAuditError: function() {
//			location.href = 'home.html#systemStatus_auditLog/auditError';
			window.open('home.html#systemStatus_auditLog/auditError', '_.blank');
		}, 
		
		noItemRender: function() {
			$('#tbody', this.el).append('<tr><td class="overflow-ellipsis"><span>&nbsp;</span></td><td height="40"></td><td></td><td></td></tr>');
		}, 
		
		noItemPlusRender: function() {
			$('#tbody', this.el).append('<tr><td class="overflow-ellipsis"><span>&nbsp;</span></td><td height="35"></td><td></td><td></td><td></td><td></td><td></td></tr>');
		}, 
		
		noAuditItemRender: function() {
			$('#liItem', this.el).append('<li class=\"li_noAudit\"><span>&nbsp;</span></li>');
		},
		/**
		 * Application Top5 조회 화면 
		 */
		makeApplicationTable: function() {
			var collection = this.collection;
			var tableClass = "";
			
			if (collection.length == 0) {
				tableClass = "nodata-dashboard";
			}
			var outputTable = this.templateApplication({
				headerName : this.headerName,
				tableClass : tableClass
			});

			this.$el.html(outputTable);
			collection.each(this.applicationItemRender, this);
			
			if (collection.length < 5  && collection.length > 0) {
				for (var i = 0; i < 5-collection.length; i++) {
					this.noItemRender();
				}
			}
			return this;
		},
		applicationItemRender: function(model) {
			model.set({
				bType: dataExpression.getbTypeName(model.get('nType')),
				totalCount: dataExpression.getFormatTrafficData(model.get('lTotCount')),
				percentCount: dataExpression.getFormatPercentData(model.get('lTotCount'), model.get('sumCount')),
				startDateInput : this.startDateInput,
				endDateInput : this.endDateInput,
				startDateGraphInput : this.startDateGraphInput,
				endDateGraphInput : this.endDateGraphInput,
				lnetgroupIndex: this.lnetgroupIndex,
				lnetworkIndex: this.lnetworkIndex,
				lvsensorIndex: this.lvsensorIndex,
				lsensorIndex: this.lsensorIndex,
				pathName: this.pathName
			});

			var itemView = new ItemView({model: model});
		
			$('#tbody', this.el).append(itemView.applicationItemRender().el);
		}, 
	});
	
	TableView.MakeTable = function($el, options) {
		var table = new TableView(options);
	
		switch(options.type) {
			case "Service":
				$el.html(table.makeServiceTable().el);
				break;
				
			case "Event":
				$el.html(table.makeEventTable().el);	
				break;
				
			case "Audit":
				$el.html(table.makeAuditTable().el);	
				break;
				
			case "VictimIp":
				$el.html(table.makeVictimIpTable().el);	
				break;
			case "Application":
				$el.html(table.makeApplicationTable().el);
				break;
			default:
				$el.html("");
				
		}
		return table;
	};
	
	return TableView;
});