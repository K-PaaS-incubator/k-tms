define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            dataExpression = require('utils/dataExpression'),
            alertMessage = require('utils/AlertMessage'),
            d3 = require('d3');

    // require template
    var Tpl = require('text!tpl/trafficAnalysis/trafficAnalysisPopup.html');

    var LineChartCollection = require('collections/detectionAnalysis/lineChartCollection');

    return Backbone.View.extend({
        className: 'tab-content',
        template: _.template(Tpl),
        events: {
            "click .genGraph": "generateGraph"
        },
        initialize: function (options) {
            options = options || {};

            this.collection = new LineChartCollection();
            this.searchCondition = options.searchCondition;

            this.searchCondition = options.searchCondition;
            if (options.searchType == 'bpsTrafficChart') {
                this.searchType = 0;
                this.dataType = "bps";
            } else if (options.searchType == 'ppsTrafficChart') {
                this.searchType = 1;
                this.dataType = "pps";
            } else {
                this.searchType = 0;
                this.dataType = "bps";
            }

        },
        render: function () {
            this.$el.append(this.template({
                locale: locale,
                dataType: this.dataType
            }));

            this.targetView = Backbone.TargetView.MakeTarget($('#targetType', this.el), this.searchCondition);
            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5
            });

            this.periodView.setDate({
                periodType: this.searchCondition.simpleTimeSelect,
                startDate: this.searchCondition.startDateInput,
                endDate: this.searchCondition.endDateInput
            });

            $('select#searchType', this.el).val(this.searchType);
            $('select#winBoundSelect', this.el).val(this.searchCondition.winBoundSelect);

            this.generateGraph();

            return this;
        },
        generateGraph: function () {
            var target = this.targetView.currentSelOrgSensor();
            var analysisPeriod = this.periodView.getPeriod();

            var searchCon = {};
            _.extend(searchCon, this.searchCondition, {
                lnetgroupIndex: target.lnetgroupIndex,
                lnetworkIndex: target.lnetworkIndex,
                lvsensorIndex: target.lvsensorIndex,
                lsensorIndex: target.lsensorIndex,
                startDateInput: analysisPeriod.startDate,
                endDateInput: analysisPeriod.endDate,
                winBoundSelect: parseInt($('select#winBoundSelect option:selected', this.el).val()),
                //nProtocol: this.model.get('nProtocol'),
                //wService: this.model.get('wService')
            });
            // 날짜 선택 범위 유효성 체크 
            if (Backbone.Utils.compareMaxDate($(".startDateInput", this.el).val(), $(".endDateInput", this.el).val()) == false) {
                alertMessage.infoMessage(errorLocale.validation.startEndTimeValid, 'info', '', 'small');

                return false;
            }

            var searchGraphDataUrl = 'selectServiceTrendBpsGraphData';
            if ($('select#searchType', this.el).val() == 1) {
                searchGraphDataUrl = 'selectServiceTrendPpsGraphData';
            }

            var thisView = this;
            this.collection.fetch({
                method: 'POST',
                url: 'api/' + searchGraphDataUrl,
                contentType: 'application/json',
                data: JSON.stringify(searchCon),
                success: function (collection) {
                    if (collection.length > 0) {
                        thisView.paintGraph();
                    } else {
                        thisView.paintGraphNoData();
                    }
                },
                beforeSend: function () {
                    Backbone.Loading.setModalLoading($('body'));
                },
                complete: function () {
                    Backbone.Loading.removeModalLoading($('body'));
                }
            });
        },
        paintGraphNoData: function () {
            $('#last', this.el).text(0);
            $('#min', this.el).text(0);
            $('#max', this.el).text(0);
            $('#avg', this.el).text(0);

            $('.popupChart', this.el).addClass('nodata');
            $('.popupChart', this.el).empty();
        },
        paintGraph: function () {
            $('.popupChart', this.el).removeClass('nodata');

            var resultData = {};
            resultData["key"] = '';
            resultData["color"] = 'grey';
            resultData.values = [];

            var selectBox = parseInt($('select#searchType option:selected', this.el).val());
            var wInbound = $('select#winBoundSelect option:selected', this.el).val();
            var lastValue = this.collection.at(this.collection.length - 1);

            var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                    parseDate = formatDate.parse;

            this.collection.each(function (model) {
                var valueIn = wInbound == 1 ? model.get('ddataIn') : '';
                var valueOut = wInbound == 1 ? model.get('ddataOut') : '';

                resultData.values.push({
                    time: parseDate(model.get('time')),
                    value: model.get('ddata'),
                    valueIn: valueIn,
                    valueOut: valueOut
                });
            });

            $('#last', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('ddata')));
            $('#min', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('minDData')));
            $('#max', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('maxDData')));
            $('#avg', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('avgData')));
            //$('#min', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('minDData'))).append('<div class="chart-text-6 font-size-11">' + lastValue.get('time') + '</div>');
            //$('#max', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('maxDData'))).append('<div class="chart-text-6 font-size-11">' + lastValue.get('time') + '</div>');

            var unit = selectBox == 0 ? '(' + locale.unit + ': bps)' : '(' + locale.unit + ': pps)';
            $('.unit', this.el).text(unit);

            var allChartData = [];
            allChartData.push(resultData);

            var chartType = wInbound == 1 ? "MultiDualLine" : "MultiLine";
            //var marginRight = wInbound == 1 ? 0 : 30;
            //var marginRight = wInbound == 1 ? 35 : 35;
            var marginLeft = wInbound == 1 ? 40 : 60;
            var marginBottom = wInbound == 1 ? 170 : 80;
            var height = wInbound == 1 ? 325 : "";
            var miniMargin = wInbound == 1 ? 310 : 235;

            this.chartView = Backbone.ChartView.MakeChart($('.popupChart', this.el), {
                type: chartType,
                chartName: "Chart",
                collection: allChartData,
                dataType: this.dataType,
                svgMainMarginTop: 20,
                svgMainMarginRight: 35,
                svgMainMarginBottom: marginBottom,
                svgMainMarginLeft: marginLeft,
                svgMiniMarginTop: miniMargin,
                chartHeight: height
            });
        },
        onClose: function () {
            if (this.targetView) {
                this.targetView.close();
            }
            if (this.periodView) {
                this.periodView.close();
            }
            if (this.chartView) {
                this.chartView.close();
            }
        }
    });
});