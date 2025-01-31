/** 
 * @description 시스템 현황 > 매니저 현황 
 * 조회 화면 
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            errorLocale = require('i18n!nls/error'),
            alertMessage = require('utils/AlertMessage');

    // require utils
    var dataExpression = require('utils/dataExpression');

    // require model, collection, view
    var DbUsageCollection = require('collections/systemStatus/dbUsageCollection'),
            ManagerStateCollection = require('collections/systemStatus/managerStateCollection'),
            DbUsageListItemView = require('views/systemStatus/dbUsage/dbUsageListItem'),
            ManagerStateItemView = require('views/systemStatus/dbUsage/managerStateItem');

    var locale = require('i18n!nls/str');
    var tpl = require('text!tpl/systemStatus/dbUsage.html');

    var intervalFlag = true;
    var refreshTime = 60000 * 5;

    var LineChartCollection = require('collections/detectionAnalysis/lineChartCollection');

    return Backbone.View.extend({
        className: 'tab-content',
        template: _.template(tpl),
        events: {
            "click .genGraph": "clickGenerateGraph",
            "click .searchBtn": "reFresh"
        },
        initialize: function () {
            this.dbUsageCollection = new DbUsageCollection();
            this.managerStateCollection = new ManagerStateCollection();

            this.listenTo(this.dbUsageCollection, 'add', this.addOne);
            this.listenTo(this.dbUsageCollection, 'reset', this.addAll);

            this.children = [];
//			this.refreshData();
            this.collection = new LineChartCollection();
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale
            }));

            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5
            });

            /*
             this.periodView.setDate({
             periodType: this.searchCondition.simpleTimeSelect,
             startDate: this.searchCondition.startDateInput,
             endDate: this.searchCondition.endDateInput
             });
             */


//			this.getDbUsageData();
            this.getManagerStateData();

            this.generateGraph('cpuUsed');
            this.generateGraph('memUsed');
            this.generateGraph('hddUsed');
            this.generateGraph('processNum');


            return this;
        },
        clickGenerateGraph: function () {
            //console.log('generate graph');
            var analysisPeriod = this.periodView.getPeriod();
            // 날짜 선택 범위 체크
            if (Backbone.Utils.compareMaxDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                alertMessage.infoMessage(errorLocale.validation.periodDate, 'info', '', 'small');
                return false;
            }
            this.generateGraph('cpuUsed');
            this.generateGraph('memUsed');
            this.generateGraph('hddUsed');
            this.generateGraph('processNum');
        },
        generateGraph: function (graphType) {
            var analysisPeriod = this.periodView.getPeriod();
            var searchCon = {};
            _.extend(searchCon, this.searchCondition, {
                startDateInput: analysisPeriod.startDate,
                endDateInput: analysisPeriod.endDate
            });

            var searchGraphDataUrl;

            if (graphType == 'cpuUsed') {
                searchGraphDataUrl = 'selectManagerStateCpuUsedGraphData';
            } else if (graphType == 'memUsed') {
                searchGraphDataUrl = 'selectManagerStateMemUsedGraphData';
            } else if (graphType == 'hddUsed') {
                searchGraphDataUrl = 'selectManagerStateHddUsedGraphData';
            } else if (graphType == 'processNum') {
                searchGraphDataUrl = 'selectManagerStateProcessNumGraphData';
            }
            //console.log('searchGraphDataUrl = ' + searchGraphDataUrl);
            var thisView = this;

            this.collection.fetch({
                method: 'POST',
                url: 'api/systemStatus/' + searchGraphDataUrl,
                contentType: 'application/json',
                data: JSON.stringify(searchCon),
                beforeSend: function () {
                    if (graphType == 'cpuUsed') {
                        Backbone.Loading.setLoading($('body'));
                    }
                },
                success: function (collection) {

                    if (collection.length > 0) {
                        thisView.paintGraph(graphType);
                    } else {
                        thisView.paintGraphNoData(graphType);
                    }
                },
                complete: function () {
                    if (graphType == 'cpuUsed') {
                        Backbone.Loading.removeLoading($('body'));
                    }
                }
            });
        },
        paintGraphNoData: function (graphType) {
            $('.' + graphType + 'Last', this.el).text(0);
            $('.' + graphType + 'Min', this.el).text(0);
            $('.' + graphType + 'Max', this.el).text(0);
            $('.' + graphType + 'Avg', this.el).text(0);

            $('.' + graphType + 'PopupChart', this.el).addClass('nodata');
            $('.' + graphType + 'PopupChart', this.el).empty();
        },
        paintGraph: function (graphType) {
            $('.' + graphType + 'PopupChart', this.el).removeClass('nodata');

            var resultData = {};
            resultData["key"] = '';
            resultData["color"] = 'grey';
            resultData.values = [];

            var lastValue = this.collection.at(this.collection.length - 1);

            var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                    parseDate = formatDate.parse;
            this.collection.each(function (model) {
                resultData.values.push({
                    time: parseDate(model.get('time')),
                    value: model.get('ddata')
                });
            });
            $('.' + graphType + 'Avg', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('avgData')));
            $('.' + graphType + 'Last', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('ddata')));
            $('.' + graphType + 'Min', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('minDData')));
            $('.' + graphType + 'Max', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('maxDData')));

            var unit = graphType == 'processNum' ? locale.unitCount : '%';
            $(".unit").text('(' + locale.unit + ' : ' + unit + ')');

            var allChartData = [];
            allChartData.push(resultData);

            this.chartView = Backbone.ChartView.MakeChart($('.' + graphType + 'PopupChart', this.el), {
                type: "MultiLine",
                chartName: graphType + "Chart",
                collection: allChartData,
                svgMainMarginTop: 20,
                svgMainMarginRight: 30,
                svgMainMarginBottom: 110,
                svgMainMarginLeft: 40,
                svgMiniMarginTop: 163,
                chartHeight: 230
            });
        },
        // 도넛 차트 조회 
        getDbUsageData: function () {
            var thisView = this;

            this.dbUsageCollection.fetch({
                method: "POST",
                url: 'api/systemStatus/selectDbUsageList',
                contentType: 'application/json',
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.progressbar', self.el));
                },
                success: function (dbUsageCollection) {
                    if (thisView.dbUsageCollection.length > 0) {
                        thisView.$("#currentTime", thisView.el).text(thisView.dbUsageCollection.at(0).get("tmoccur"));
                    }
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.progressbar', self.el));
                }
            });
        },
        // 매니저 현황 조회 
        getManagerStateData: function () {
            var self = this;
            this.managerStateCollection.fetch({
                method: "POST",
                data: JSON.stringify({}),
                url: 'api/systemStatus/selectManagerStateList',
                contentType: 'application/json',
                reset: true,
                success: function (data) {
                    self.addAllManager();
                    if (data.length > 0) {
                        self.$("#list").removeClass("nodata");
                        $("#managerCurrentTime", self.el).text(data.at(0).get("tmoccur"));
                    } else {
                        self.$("#list").addClass("nodata");
                    }
                }
            });
        },
        addOne: function (model) {
            var thisView = this;

            var status = ''; //상태 조회
            var statusStr = 'Healthy'; //상태 조회
            if (dataExpression.getFormatPercent(model.get("dblUsed"), model.get("dblTotal")) > 90) {
                status = "warning";
                statusStr = "Warning";
            }

            model.set({
                status: status,
                statusStr: statusStr
            });
            var view = new DbUsageListItemView({
                model: model
            });
            thisView.children.push(view);
            $("#list", thisView.el).append(view.render().el);

        },
        addAll: function () {
            var self = this;
            $("#list", self.el).empty();
            this.dbUsageCollection.each(this.addOne, self.el);
        },
        addAllManager: function () {
            var self = this;
            $("#managerStateList", self.el).empty();
            for (var i = 0; i < self.managerStateCollection.length; i++) {
                var view = new ManagerStateItemView({
                    model: self.managerStateCollection.at(i)
                });
                $("#managerStateList", self.el).append(view.render().el);
            }
        },
        onClose: function () {
            if (this.children.length > 0) {
                _.each(this.children, function (child) {
                    child.close();
                });
            }
        },
        reFresh: function () {
            $("#list", this.el).empty();
            this.getManagerStateData();
            this.getDbUsageData();
        }
    });
});