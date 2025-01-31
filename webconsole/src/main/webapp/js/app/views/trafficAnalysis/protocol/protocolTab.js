/**
 *  protocol Tab View
 */
define(function (require) {
	
    "use strict";

    // require library
    var $ 							= require('jquery'),
        _ 							= require('underscore'),
        Backbone 					= require('backbone'),
        dataExpression 				= require('utils/dataExpression'),
        analysisDate 				= require('utils/analysisDate'),
        Pagination 					= require('views/common/pagination');

    // require locale
    var locale = require('i18n!nls/str');

    // require model, collection, view
    var ProtocolModel 				= require('models/trafficAnalysis/protocolModel'),
        ProtocolCollection 			= require('collections/trafficAnalysis/protocolCollection'),
        ProtocolItem 				= require('views/trafficAnalysis/protocol/protocolItem'),
        ChartFlow 					= require('views/common/chartFlow'),
        ExportView 					= require('views/common/export');

    // require template
    var tabViewTpl 					= require('text!tpl/trafficAnalysis/protocolTab.html');

    return Backbone.View.extend({
        tabBoxTemplate: _.template(tabViewTpl),
        initialize: function(options) {

            this.protocolCollection = new ProtocolCollection();
            this.protocolModel 		= new ProtocolModel();
            this.pagination 		= new Pagination();
            this.exportCollection 	= new ProtocolCollection();	// 내보내기  추가
            this.exportTotalModel 	= new ProtocolModel();		// 내보내기  추가
            this.chartFlow;
            this.defaultTimeModel;
            this.listType = options.listType;
            this.evt = options.evt;

            _.bindAll(this, "removeItemViews", "retrieve", "customOn", "customOff", "render", "getListData", "getTotalData", "setTotalData", "addOne", "addAll");

            this.customOn();

            this.listenTo(this.protocolModel, 'change', this.setTotalData);
            this.listenTo(this.protocolCollection, 'add', this.addOne);
            this.listenTo(this.protocolCollection, 'reset', this.addAll);
        },
        events: {
            'click #readMoreBtn'	: 'getListData',
            'click .btnGraph'		: 'callMakeGraph',
            'click .btnExport'		: 'callMakeExport'
        },
        callMakeGraph: function() {
            this.chartFlow.makeGraph();
        },
        callMakeExport: function(e) {
            var self = this;
            var format = $(e.currentTarget).data('format');
            var exportRange = $("input:radio[name='exportRange']:checked").val();

            var templateFile = 'js/tpl/common/excel.jsp';

            if (format == 'excel') {
                templateFile = 'js/tpl/common/excel.jsp';
            } else if (format == 'word') {
                templateFile = 'js/tpl/common/word.jsp';
            } else if (format == 'html') {
                templateFile = 'js/tpl/common/html.jsp';
            }

            if (exportRange == 'all') {
                self.model.set('isDownload', 'Y');
            } else {
                self.model.set('isDownload', '');
            }

            self.getExportList(templateFile);
        },
        getExportList: function(templateFile) {

            var self = this;
            var paging = {};
            var reset = true;

            _.extend(paging, {"startRowSize": 0, "endRowSize": this.pagination.endRowSize, "rowSize":(this.pagination.rowSize)});
            var listParam = $.extend(paging, self.model.toJSON());

            self.exportCollection.fetch({
                method: 'POST',
                data: JSON.stringify(listParam),
                url: "api/trafficAnalysis_protocolList",
                contentType: 'application/json',
                beforeSend: function() {
                    Backbone.Loading.setLoading($('.progressbar', self.el));
                },
                success: function(collection) {
                    self.model.set('isDownload', '');
                    if (self.exportCollection.length > 0) {
                        self.getExportTotal(templateFile, listParam);
                    }
                },
                error: function(message) {
                    self.model.set('isDownload', '');
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.progressbar', self.el));
                }
            });
        },
        // 전체 건수 조회
        getExportTotal: function(templateFile, listParam) {
            var self = this;
            var totalParam = this.model.toJSON();

            self.exportTotalModel.fetch({
                method: 'POST',
                url: "api/trafficAnalysis_protocolTotal",
                contentType: 'application/json',
                data: JSON.stringify(totalParam),
                reset: true,
                success: function(model) {
                    self.excuteExport(templateFile, listParam, self.exportTotalModel);	
                },
            });
        },
        // export UI 세팅
        excuteExport: function(templateFile, listParam, exportTotalModel) {
            var self = this;

            var contentTable = document.createElement('table');
            $(contentTable).append('<tr>' +
                    '<th class=\"content-table-header\" style=\"width:100px;\">' + locale.rank + '</th>' +
                    '<th class=\"content-table-header\" >' + locale.protocol + '</th>' +
                    '<th class=\"content-table-header\" >' + 'Inbound + Outbound (bps)' + '</th>' +
                    '<th class=\"content-table-header\" >' + 'Inbound' + '</th>' +
                    '<th class=\"content-table-header\" >' + 'Outbound' + '</th>' +
                    '<th class=\"content-table-header\" >' + 'Inbound + Outbound (pps)' + '</th>' +
                    '<th class=\"content-table-header\" >' + 'Inbound' + '</th>' +
                    '<th class=\"content-table-header\" >' + 'Outbound' + '</th>' +
                    '</tr>' + 
                    '<tr>' +
                    '<td class=\"total-table-data\" >' + locale.whole + '</td>' +
                    '<td class=\"total-table-data\" >' + '' + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('bps')) + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('ingressBps')) + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('egressBps')) + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('pps')) + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('ingressPps')) + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('egressPps')) + '</td>' +
                    '</tr>');
            var forMat = [];
            forMat[0] = "bps";
            forMat[1] = "ingressBps";
            forMat[2] = "egressBps";
            forMat[3] = "pps";
            forMat[4] = "ingressPps";
            forMat[5] = "egressPps";

            new ExportView({
                templateFile: templateFile, 			// 공통
                docTitle: locale.protocol, 				// file 명 (출력메뉴)
                requireModel: new ProtocolModel(), 		// 리스트 모델			
                listParam: listParam, 					// 선택 조건 
                fetchCollection: self.exportCollection, // 리스트 데이터		
                contentTable: contentTable, 			// 리스트
                totalColomn: 8, 						// 테이블 총 컬럼		
                listKeys: new Array(_.pick(new ProtocolModel().attributes), 
                		'rNum', 						// Model에서 출력에 사용되는 key  
                        'strName', 						// 프로토콜
                        'bps', 							// Inbound + Outbound (bps)
                        'ingressBps', 					// Inbound
                        'egressBps', 					// Outbound
                        'pps', 							// Inbound + Outbound (pps)
                        'ingressPps', 					// Inbound
                        'egressPps' 					// Outbound
                        ),
                forMat: forMat
            }).makeFile();
        },
        onClose: function() {
            //this.removeItemViews();
            this.customOff();
        },
        customOn: function() {
            this.evt.bind("retrieve", this.retrieve);
        },
        customOff: function() {
            this.evt.unbind("retrieve");
        },
        render: function() {
            this.$el.html(this.tabBoxTemplate({
                locale: locale
            }));
            this.paginationBasicInfo = this.pagination.getPaginationType('readMorePaginator');
            $('#pageLayout', this.el).append(this.paginationBasicInfo.type.el);

            return this;
        },
        removeItemViews: function() {
            this.evt.trigger('cleanUpItemView');
        },
        retrieve: function(model) {
            this.model = model;
            this.pagination.setInitialization();
            this.getListData();

            $("#chart", this.el).empty();
            this.chartFlow = new ChartFlow({
                searchModel: this.model,
                chartName: "protocol",
                chartUrl: "api/trafficAnalysis/selectTrafficProtocolChart",
                parentEl: this.el,
                dataType: "bps"
            });
            $("#chart", this.el).append(this.chartFlow.el);
            this.chartFlow.render();
        },
        getListData: function() {

            var self = this;
            var paging = this.pagination.readMorePagination();
            if (!paging) {
                return;
            }
            var listParam = $.extend(paging, this.model.toJSON());
            var reset = true;

            $("#listDate", self.el).text(this.model.get('startDateInput') + " ~ " + this.model.get('endDateInput'));
            
            if (paging.startRowSize == 0) {
                $("#graphItemCheckTotal", this.el).eq(0).prop('checked', false);
                for (var i = 0; i < 5; i++) {
                    $("input[name='graphItemCheck']", this.el).eq(i).attr('checked', false);
                }
                self.reset = true;
            } else {
                self.reset = false;
            }

            self.protocolCollection.fetch({
                method: 'POST',
                url: "api/trafficAnalysis_protocolList",
                contentType: 'application/json',
                data: JSON.stringify(listParam),
                reset: self.reset,
                beforeSend: function() {
                    Backbone.Loading.setLoading($('body'));
                    $('#pageLayout', self.el).hide();
                },
                success: function(data) {
                    if (self.protocolCollection.length > 0) {
                        $('.btnExport').attr('disabled', false);
                        $('input:radio[name="exportRange"]').attr('disabled', false);
                        $('.btnGraph').attr('disabled', false);
                        
                        self.$(".table").removeClass("nodata");
                        self.$('#totalRowSize').text(self.protocolCollection.at(0).get("totalRowSize"));
                                                
                        $("#totalRowSize").digits();

                        if (paging.startRowSize == 0) {
                            // 그래프 그리기 
                            // 전체 체크박스 체크
                            self.$("#graphItemCheckTotal").eq(0).prop('checked', true);
                            // 체크박스 TOP5 체크
                            for (var i = 0; i < 5; i++) {
                                if (i < self.protocolCollection.length) {
                                    self.$("input[name='graphItemCheck']").eq(i).attr('checked', true);
                                    self.$("td[name='graphItemCheckTd']").eq(i).addClass('trend-blue-bg-lv' + (i + 1));

                                }
                            }
                            self.chartFlow.makeGraph();
                        }
                    } else {
                        $('.btnExport').attr('disabled', true);
                        $('input:radio[name="exportRange"]').attr('disabled', true);
                        $('.btnGraph').attr('disabled', true);
                        
                        Backbone.Utils.DomSeletor.hideThHightLight(self.model.get('sortSelect'), self.el);

                        self.$('#totalRowSize').text(0);
                        self.$(".table").addClass("nodata");
                    }
                    self.getTotalData();
                },
                complete: function() {
                    if (self.protocolCollection.length <= 0) {
                        Backbone.Loading.removeLoading($('body'));
                    }
                    $('#pageLayout', self.el).show();
                }
            });
        },
        getTotalData: function() {
            if (this.protocolCollection.length > 0) {
                this.pagination.setTotalRowSize(this.protocolCollection.at(0).get('totalRowSize'));

                var listParam = this.model.toJSON();

                this.protocolModel.fetch({
                    method: 'POST',
                    url: "api/trafficAnalysis_protocolTotal",
                    contentType: 'application/json',
                    data: JSON.stringify(listParam),
                    reset: true,
                    success: function(data) {
                    	console.log(data);
                    },
                });
            } else {
                this.pagination.setTotalRowSize(0);
                this.protocolModel.clear();
            }
        },
        setTotalData: function() {
            if (this.protocolCollection.length > 0) {
                // 총 프로토콜수 
                this.$('#totalRowsize').text(this.protocolCollection.at(0).get('totalRowSize'));

                // 전체값 BPS
                this.$('#totalBps').text(dataExpression.getFormatTrafficData(this.protocolModel.get('bps')));
                this.$('#totalIngressBps').text(dataExpression.getFormatTrafficData(this.protocolModel.get('ingressBps')));
                this.$('#totalEgressBps').text(dataExpression.getFormatTrafficData(this.protocolModel.get('egressBps')));

                // 전체값 PPS
                this.$('#totalPps').text(dataExpression.getFormatTrafficData(this.protocolModel.get('pps')));
                this.$('#totalIngressPps').text(dataExpression.getFormatTrafficData(this.protocolModel.get('ingressPps')));
                this.$('#totalEgressPps').text(dataExpression.getFormatTrafficData(this.protocolModel.get('egressPps')));

            } else {
                // 총 프로토콜수 
                this.$('#totalRowsize').text(0);

                $('#totalText', this.el).text("");
                // 전체값 BPS
                this.$('#totalBps').text("");
                this.$('#totalIngressBps').text("");
                this.$('#totalEgressBps').text("");

                // 전체값 PPS
                this.$('#totalPps').text("");
                this.$('#totalIngressPps').text("");
                this.$('#totalEgressPps').text("");

            }
        },
        addOne: function(model) {
            var protocolItem = new ProtocolItem({
                model: model,
                searchModel: this.model,
                listType: this.listType,
                parentEl: this.el
            });
            this.$('#protocolListTbody').append(protocolItem.render().el);
        },
        addAll: function() {
            var self = this;
            this.$("#protocolListTbody").empty();
            this.protocolCollection.each(this.addOne, self);

            Backbone.Utils.DomSeletor.showThHighLight(this.model.get('sortSelect'), this.el);
        }
    });
});