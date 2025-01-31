define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            dataExpression = require('utils/dataExpression'),
            analysisDate = require('utils/analysisDate');

    var locale = require('i18n!nls/str');

    var MaliciousTrafficCollection = require('collections/trafficAnalysis/maliciousTrafficCollection'),
            MaliciousTrafficModel = require('models/trafficAnalysis/maliciousTrafficModel');

    var ChartFlow = require('views/common/chartFlow'),
            ExportView = require('views/common/export'),
            tabTpl = require('text!tpl/trafficAnalysis/maliciousTrafficStat.html'),
            itemTpl = require('text!tpl/trafficAnalysis/maliciousTrafficListItem.html');

    var TrafficAnalysisPopupView = require('views/popup/trafficAnalysisPopup');

    return Backbone.View.extend({
        template: _.template(tabTpl),
        itemTemplate: _.template(itemTpl),
        initialize: function (options) {
            this.maliciousCollection = new MaliciousTrafficCollection();
            this.maliciousTrafficCollection = new MaliciousTrafficCollection();
            this.totalTrafficModel = new MaliciousTrafficModel();
            this.maliciousTrafficModel = new MaliciousTrafficModel();

            this.exportCollection = new MaliciousTrafficCollection();
            this.exportTotalModel = new MaliciousTrafficModel();

            this.chartFlow;
            this.listType = options.listType;
            this.evt = options.evt;

            _.bindAll(this, "onClose", "customOn", "customOff", "render", "retrieve", "getListData", "getTotalData", "addOne", "addAll");

            this.customOn();

            this.listenTo(this.maliciousTrafficCollection, 'add', this.addOne);
            this.listenTo(this.maliciousTrafficCollection, 'reset', this.addAll);

        },
        events: {
            'click .btnGraph': 'callMakeGraph',
            'click .totalBpsPopup': 'totalTrafficBpsPopup',
            'click .maliciousBpsPopup': 'maliciousTrafficBpsPopup',
            'click .totalPpsPopup': 'totalTrafficPpsPopup',
            'click .maliciousPpsPopup': 'maliciousTrafficPpsPopup',
            'click .btnExport': 'callMakeExport'
        },
        callMakeExport: function (e) {
            var self = this;
            var format = $(e.currentTarget).data('format');

            var templateFile = 'js/tpl/common/excel.jsp';

            if (format == 'excel') {
                templateFile = 'js/tpl/common/excel.jsp';
            } else if (format == 'word') {
                templateFile = 'js/tpl/common/word.jsp';
            } else if (format == 'html') {
                templateFile = 'js/tpl/common/html.jsp';
            }

            self.getExportList(templateFile);
        },
        // collection 조회
        getExportList: function (templateFile) {

            var self = this;
            var paging = {};

            _.extend(paging, {"startRowSize": 0, "endRowSize": 2, "rowSize":2});
            var listParam = $.extend(paging, self.model.toJSON());

            self.exportCollection.fetch({
                method: 'POST',
                data: JSON.stringify(listParam),
                url: "api/trafficAnalysis/selectMaliciousTrafficList",
                contentType: 'application/json',
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.progressbar', self.el));
                },
                success: function (collection) {
                    if (self.exportCollection.length > 0) {
                        self.getExportTotal(templateFile, listParam, collection);
                    }
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.progressbar', self.el));
                }
            });
        },
        // 전체 건수 조회
        getExportTotal: function (templateFile, listParam, collection) {
            var self = this;
            var totalParam = this.model.toJSON();

            self.exportTotalModel.fetch({
                method: 'POST',
                url: "api/trafficAnalysis_protocolTotal",
                contentType: 'application/json',
                data: JSON.stringify(totalParam),
                reset: true,
                success: function (model) {
                    self.excuteExport(templateFile, listParam, collection);
                },
            });
        },
        // export UI 세팅
        excuteExport: function (templateFile, listParam, collection) {
            var self = this;

            var contentTable = document.createElement('table');
            $(contentTable).append(
                    '<tr>' +
                    '<td class=\"content-table-header\" style=\"width:100px;\">' + locale.item + '</td>' +
                    '<td class=\"content-table-header\" >' + 'Inbound + Outbound (bps)' + '</td>' +
                    '<td class=\"content-table-header\" >' + 'Inbound' + '</td>' +
                    '<td class=\"content-table-header\" >' + 'Outbound' + '</td>' +
                    '<td class=\"content-table-header\" >' + 'Inbound + Outbound (pps)' + '</td>' +
                    '<td class=\"content-table-header\" >' + 'Inbound' + '</td>' +
                    '<td class=\"content-table-header\" >' + 'Outbound' + '</td>' +
                    '</tr>'
                    );
            var forMat = [];
            forMat[0] = "bps";
            forMat[1] = "ingressBps";
            forMat[2] = "egressBps";
            forMat[3] = "pps";
            forMat[4] = "ingressPps";
            forMat[5] = "egressPps";

            new ExportView({
                menuOptions: 'malicious',
                templateFile: templateFile, // 공통
                docTitle: locale.maliciousTraffic, // file 명 (출력메뉴)
                requireModel: new MaliciousTrafficModel(), // 리스트 모델				
                listParam: listParam, // 선택 조건 
                fetchCollection: self.exportCollection, // 리스트 데이터			
                contentTable: contentTable, // 리스트
                totalColomn: 7, // 테이블 총 컬럼			
                listKeys: new Array(_.pick(new MaliciousTrafficModel().attributes),
                        // Model에서 출력에 사용되는 key 
                        'strName', // 전체트래픽
                        'bps', // Inbound + Outbound (bps)
                        'ingressBps', // Inbound
                        'egressBps', // Outbound
                        'pps', // Inbound + Outbound (pps)
                        'ingressPps', // Inbound
                        'egressPps' 						// Outbound
                        ),
                //collection    : collection.toJSON()
                collection: collection,
                forMat: forMat
            }).makeFile();
        },
        onClose: function () {
            this.customOff();
        },
        customOn: function () {
            this.evt.bind("retrieve", this.retrieve);
        },
        customOff: function () {
            this.evt.unbind("retrieve");
        },
        callMakeGraph: function () {
            this.chartFlow.makeGraph();
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale
            }));
            return this;
        },
        retrieve: function (model) {
            this.model = model;
            this.getListData();

            $("#chart", this.el).empty();
            this.chartFlow = new ChartFlow({
                searchModel: this.model,
                chartName: "malicious",
                chartUrl: "api/trafficAnalysis/selectMaliciousTrafficChart",
                parentEl: this.el,
                dataType: "bps"
            });
            $("#chart", this.el).append(this.chartFlow.el);
            this.chartFlow.render();
        },
        getListData: function () {
            var self = this;
            var listParam = this.model.toJSON();
            $("#listDate", self.el).text(this.model.get('startDateInput') + " ~ " + this.model.get('endDateInput'));

            // 5분 통계 데이터 조회 
            self.maliciousTrafficCollection.fetch({
                method: "POST",
                data: JSON.stringify(listParam),
                url: "api/trafficAnalysis/selectMaliciousTrafficList",
                contentType: 'application/json',
                reset: true,
                beforeSend: function () {
                    Backbone.Loading.setLoading($('body'));
                },
                success: function (collection) {
                    if (collection.at(0).get("ucType") != null) {
                        $('.btnExport').attr('disabled', false);
                        $('.btnGraph').attr('disabled', false);

                        self.$(".table").removeClass("nodata");
                        self.$('#totalRowSize').text(2);
                        // 그래프 그리기 
                        // 체크박스 체크
                        self.$("#graphItemCheckTotal").prop('checked', true);
                        self.$("input[name='graphItemCheck']").attr('checked', true);
                        // 유해 트래픽  TD
                        self.$("td[name='graphItemCheckTdItem']").addClass('trend-blue-bg-lv1');

                        self.chartFlow.makeGraph();
                    } else {
                        $('.btnExport').attr('disabled', true);
                        $('.btnGraph').attr('disabled', true);

                        Backbone.Utils.DomSeletor.hideThHightLight(self.model.get('sortSelect'), self.el);
                        self.$('#totalRowSize').text(0);
                        self.$(".table").addClass("nodata");
                    }
                    self.getTotalData();
                },
                complete: function () {
                    if (self.maliciousTrafficCollection.at(0).get("ucType") == null) {
                        Backbone.Loading.removeLoading($('body'));
                    }
                }
            });
        },
        getTotalData: function () {
            // 전체 트래픽 
            if (this.maliciousTrafficCollection.length > 0) {
                var listParam = $.extend(this.model.toJSON(), {
                    protocolSelect: "total",
                    ucType: 10,
                    nProtocol: 0
                });
                this.maliciousTrafficModel.fetch({
                    method: 'POST',
                    url: "api/trafficAnalysis_protocolTotal",
                    contentType: 'application/json',
                    data: JSON.stringify(listParam),
                    reset: true,
                    success: function (data) {
                    	console.log(data);
                    },
                });
            } else {
                this.maliciousTrafficModel.clear();
            }
        },
        addOne: function (model) {
            model.set({
                bps: dataExpression.getFormatTrafficData(model.get("bps")),
                ingressBps: dataExpression.getFormatTrafficData(model.get("ingressBps")),
                egressBps: dataExpression.getFormatTrafficData(model.get("egressBps")),
                pps: dataExpression.getFormatTrafficData(model.get("pps")),
                ingressPps: dataExpression.getFormatTrafficData(model.get("ingressPps")),
                egressPps: dataExpression.getFormatTrafficData(model.get("egressPps")),
                malBps: dataExpression.getFormatTrafficData(model.get("malBps")),
                malIngressBps: dataExpression.getFormatTrafficData(model.get("malIngressBps")),
                malEgressBps: dataExpression.getFormatTrafficData(model.get("malEgressBps")),
                malPps: dataExpression.getFormatTrafficData(model.get("malPps")),
                malIngressPps: dataExpression.getFormatTrafficData(model.get("malIngressPps")),
                malEgressPps: dataExpression.getFormatTrafficData(model.get("malEgressPps"))
            });

            this.$("#list").append(this.itemTemplate({
                maliciousTrafficModel: this.maliciousTrafficModel,
                locale: locale,
                model: model,
                searchModel: this.model,
                listType: this.listType,
                highLightEl: this.model.get('sortSelect'),
                isHighLight: Backbone.TemplateHelper.isHighLight
            }));
        },
        addAll: function () {
            this.$("#list").empty();
            this.maliciousTrafficCollection.each(this.addOne, this);

            Backbone.Utils.DomSeletor.showThHighLight(this.model.get('sortSelect'), this.el);
        },
        totalTrafficBpsPopup: function () {
            var listParam = this.model.toJSON();
            var searchCondition = {};
            _.extend(searchCondition, listParam, {
                nProtocol: 0,
                ucType: 10,
                winBoundSelect: 0,
                protocolSelect: "total"
            });
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: locale.totalTraffic + " " + locale.occurrenceTrend,
                body: new TrafficAnalysisPopupView({
                    searchType: 'bpsTrafficChart',
                    searchCondition: searchCondition
                })
            });
        },
        maliciousTrafficBpsPopup: function () {
            var listParam = this.model.toJSON();
            var searchCondition = {};
            _.extend(searchCondition, listParam, {
                nProtocol: 0,
                ucType: 60,
                winBoundSelect: 0
            });
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: locale.maliciousTraffic + " " + locale.occurrenceTrend,
                body: new TrafficAnalysisPopupView({
                    searchType: 'bpsTrafficChart',
                    searchCondition: searchCondition
                })
            });
        },
        totalTrafficPpsPopup: function () {
            var listParam = this.model.toJSON();
            var searchCondition = {};

            _.extend(searchCondition, listParam, {
                nProtocol: 0,
                ucType: 10,
                winBoundSelect: 0,
                protocolSelect: "total"
            });

            Backbone.ModalView.msg({
                size: 'medium-large',
                title: locale.totalTraffic + ' ' + locale.occurrenceTrend,
                body: new TrafficAnalysisPopupView({
                    searchType: 'ppsTrafficChart',
                    searchCondition: searchCondition
                })
            });
        },
        maliciousTrafficPpsPopup: function () {
            var listParam = this.model.toJSON();
            var searchCondition = {};
            _.extend(searchCondition, listParam, {
                nProtocol: 0,
                ucType: 60,
                winBoundSelect: 0
            });
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: locale.maliciousTraffic + " " + locale.occurrenceTrend,
                body: new TrafficAnalysisPopupView({
                    searchType: 'ppsTrafficChart',
                    searchCondition: searchCondition
                })
            });
        }
    });
});