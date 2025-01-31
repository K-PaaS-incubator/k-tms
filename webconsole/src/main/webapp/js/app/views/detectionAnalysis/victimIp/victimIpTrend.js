define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            dataExpression = require('utils/dataExpression'),
            d3 = require('d3'),
            alertMessage = require('utils/AlertMessage');

    // require template
    var Tpl = require('text!tpl/detectionAnalysis/detectionAnalysisTrend.html');

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
            if (options.searchType == 'attackCountTrafficChart') {
                this.searchType = 0;
                this.dataType = locale.count;
            } else if (options.searchType == 'bpsTrafficChart') {
                this.searchType = 1;
                this.dataType = "bps";
            } else if (options.searchType == 'ppsTrafficChart') {
                this.searchType = 2;
                this.dataType = "pps";
            } else {
                this.searchType = 0;
                this.dataType = locale.count;
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
                winBoundSelect: parseInt($('select#winBoundSelect', this.el).val()),
                //srcIp: this.model.get('dwSourceIp')
                //despIp: this.searchCondition.destIp
                destIp: this.model.get('dwDestinationIp')
            });
            // 날짜 선택 범위 유효성 체크 
            if (Backbone.Utils.compareMaxDate($(".startDateInput", this.el).val(), $(".endDateInput", this.el).val()) == false) {
                return alertMessage.infoMessage(errorLocale.validation.startEndTimeValid, 'info', '', 'small');
            }
            if (Backbone.Utils.compareDate($(".startDateInput", this.el).val(), $(".endDateInput", this.el).val()) == false) {
                return alertMessage.infoMessage(errorLocale.validation.grapeMinValid, 'info', '', 'small');
            }

            var searchGraphDataUrl = 'selectVictimIpAttackCountGraphData';
            if ($('select#searchType', this.el).val() == 1) {
                searchGraphDataUrl = 'selectVictimIpBpsGraphData';
            } else if ($('select#searchType', this.el).val() == 2) {
                searchGraphDataUrl = 'selectVictimIpPpsGraphData';
            }
            var thisView = this;
            this.collection.fetch({
                method: 'POST',
                url: 'api/' + searchGraphDataUrl,
                contentType: 'application/json',
                data: JSON.stringify(searchCon),
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.popupChart', thisView.el));
                },
                success: function (collection) {
                    if (collection.length > 0) {
                        thisView.paintGraph();
                    } else {
                        thisView.paintGraphNoData();
                    }
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.popupChart', thisView.el));
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
            var searchType = $('select#searchType option:selected', this.el).val();
            var lastValue = this.collection.at(this.collection.length - 1);

            if (searchType == 0) {
                $(".unit", this.el).text('(' + locale.unit + ': ' + locale.count + ')');
            } else if (searchType == 1) {
                $(".unit", this.el).text('(' + locale.unit + ': bps)');
            } else if (searchType == 2) {
                $(".unit", this.el).text('(' + locale.unit + ': pps)');
            }

            var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                    parseDate = formatDate.parse;
            this.collection.each(function (model) {
                resultData.values.push({
                    time: parseDate(model.get('time')),
                    value: model.get('ddata')
                });
            });
            var name = searchType == 0 ? 'Sum' : 'Avg';
            var avg = searchType == 0 ? dataExpression.getFormatTrafficData(lastValue.get('sumData')) : dataExpression.getFormatTrafficData(lastValue.get('avgData'));

            $('#tdName', this.el).text(name);
            $('#avg', this.el).text(avg);
            $('#last', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('ddata')));
            $('#min', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('minDData')));
            $('#max', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('maxDData')));

            var allChartData = [];
            allChartData.push(resultData);
            this.chartView = Backbone.ChartView.MakeChart($('.popupChart', this.el), {
                type: "MultiLine",
                chartName: "Chart",
                collection: allChartData,
                svgMainMarginTop: 20,
                //svgMainMarginRight: 0,
                svgMainMarginRight: 35,
                svgMainMarginBottom: 80,
                svgMainMarginLeft: 60,
                svgMiniMarginTop: 235
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