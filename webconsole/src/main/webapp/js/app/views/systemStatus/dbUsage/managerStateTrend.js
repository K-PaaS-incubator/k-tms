define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            d3 = require('d3'),
            dataExpression = require('utils/dataExpression'),
            alertMessage = require('utils/AlertMessage');

    // require template
    var Tpl = require('text!tpl/systemStatus/managerStateTrend.html');

    var LineChartCollection = require('collections/detectionAnalysis/lineChartCollection');

    return Backbone.View.extend({
        className: 'tab-content',
        template: _.template(Tpl),
        events: {
            "click .genGraph": "clickGenerateGraph",
            "click #tab-btn-cpu": "clickGenerateGraphCpu",
            "click #tab-btn-memory": "clickGenerateGraphMemory",
            "click #tab-btn-hdd": "clickGenerateGraphHdd",
            "click #tab-btn-process": "clickGenerateGraphProcess"
        },
        initialize: function (options) {
            options = options || {};

            this.collection = new LineChartCollection();
            this.searchCondition = options.searchCondition;
        },
        render: function () {
            this.$el.append(this.template({
                locale: locale,
                dataType: this.dataType
            }));

            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5
            });

            this.periodView.setDate({
                periodType: this.searchCondition.simpleTimeSelect,
                startDate: this.searchCondition.startDateInput,
                endDate: this.searchCondition.endDateInput
            });

            this.generateGraph('cpuUsed');
            //this.generateGraph('memUsed');
            //this.generateGraph('hddUsed');
            //this.generateGraph('processNum');

            return this;
        },
        clickGenerateGraph: function () {
            if ($("#cpu-tab", this.el).hasClass('active') == true)
                this.generateGraph('cpuUsed');
            if ($("#memory-tab", this.el).hasClass('active') == true)
                this.generateGraph('memUsed');
            if ($("#hdd-tab", this.el).hasClass('active') == true)
                this.generateGraph('hddUsed');
            if ($("#process-tab", this.el).hasClass('active') == true)
                this.generateGraph('processNum');
        },
        clickGenerateGraphCpu: function () {
            this.generateGraph('cpuUsed');
        },
        clickGenerateGraphMemory: function () {
            this.generateGraph('memUsed');
        },
        clickGenerateGraphHdd: function () {
            this.generateGraph('hddUsed');
        },
        clickGenerateGraphProcess: function () {
            this.generateGraph('processNum');
        },
        generateGraph: function (graphType) {
            var analysisPeriod = this.periodView.getPeriod();
            // 날짜 선택 범위 체크 
            if (Backbone.Utils.compareMaxDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                alertMessage.infoMessage(errorLocale.validation.periodDate, 'info', '', 'small');
                return false;
            }
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
            var thisView = this;

            this.collection.fetch({
                method: 'POST',
                url: 'api/systemStatus/' + searchGraphDataUrl,
                contentType: 'application/json',
                data: JSON.stringify(searchCon),
                success: function (collection) {

                    if (collection.length > 0) {
                        thisView.paintGraph(graphType);
                    } else {
                        thisView.paintGraphNoData(graphType);
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
        onClose: function () {
            if (this.periodView) {
                this.periodView.close();
            }
            if (this.chartView) {
                this.chartView.close();
            }
        }
    });
});