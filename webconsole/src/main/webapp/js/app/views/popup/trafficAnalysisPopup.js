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
            var thisView = this;
            var target = this.targetView.currentSelOrgSensor();
            var analysisPeriod = this.periodView.getPeriod();

            var searchCon = {};
            if (_.isNull($('select#winBoundSelect', this.el).val())) {
                _.extend(searchCon, this.searchCondition);
            } else {
                // 트래픽분석 > 유해트래픽 조회용으로 사용하기 위해 수정 
                var nProtocol, ucType;
                if (this.model == undefined) {
                    nProtocol = this.searchCondition.nProtocol;			// 유해트래픽에서 전달받은 값 
                    ucType = this.searchCondition.ucType;
                } else {
                    nProtocol = this.model.get('nProtocol');			// 유해트래픽을 제외한 나머지 메뉴에서 전달받은 값 
                    ucType = this.model.get('ucType');
                }
                _.extend(searchCon, this.searchCondition, {
                    lnetgroupIndex: target.lnetgroupIndex,
                    lnetworkIndex: target.lnetworkIndex,
                    lvsensorIndex: target.lvsensorIndex,
                    lsensorIndex: target.lsensorIndex,
                    startDateInput: analysisPeriod.startDate,
                    endDateInput: analysisPeriod.endDate,
                    winBoundSelect: parseInt($('select#winBoundSelect', this.el).val()),
                    nProtocol: nProtocol,
                    ucType: ucType
                });
            }

            // 날짜 선택 범위 유효성 체크 
            if (Backbone.Utils.compareMaxDate($(".startDateInput", this.el).val(), $(".endDateInput", this.el).val()) == false) {
                alertMessage.infoMessage(errorLocale.validation.startEndTimeValid, 'info', '', 'small');

                return false;
            }

            var searchGraphDataUrl;
            if ($('select#searchType', this.el).val() == 1) {
                searchGraphDataUrl = 'selectTrafficAnalysisPpsPopupGraphData';
            } else {
                searchGraphDataUrl = 'selectTrafficAnalysisBpsPopupGraphData';
            }

            this.collection.fetch({
                method: 'POST',
                url: 'api/' + searchGraphDataUrl,
                contentType: 'application/json',
                data: JSON.stringify(searchCon),
                success: function () {
                    thisView.paintGraph();
                },
                beforeSend: function () {
                    Backbone.Loading.setModalLoading($('body'));
                },
                complete: function () {
                    Backbone.Loading.removeModalLoading($('body'));
                }
            });
        },
        paintGraph: function () {
            var resultData = {};
            resultData["key"] = '';
            resultData["color"] = 'grey';
            resultData.values = [];

            var selectBox = parseInt($('select#searchType option:selected', this.el).val());
            var lastValue = this.collection.at(this.collection.length - 1);
            var wInbound = $('select#winBoundSelect option:selected', this.el).val();

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

//			if(parseInt($('select#winBoundSelect', this.el).val()) == 1){ //방향>양방향
//				this.collection.each(function(model) {
//					resultData.values.push({
//						time: parseDate(model.get('time')),
//						value: model.get('ddata'),
//						valueIn: model.get('ddataIn'),
//						valueOut: model.get('ddataOut')
//					});
//				});
//			} else {
//				this.collection.each(function(model) {
//					resultData.values.push({
//						time: parseDate(model.get('time')),
//						value: model.get('ddata')
//					});
//				});
//			}

            var unit = selectBox == 0 ? '(' + locale.unit + ': bps)' : '(' + locale.unit + ': pps)';
            $('.unit', this.el).text(unit);

            $('#last', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('ddata')));
            //$('#min', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('minDData'))).append('<div class="chart-text-6 font-size-11">' + lastValue.get('time') + '</div>');
            //$('#max', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('maxDData'))).append('<div class="chart-text-6 font-size-11">' + lastValue.get('time') + '</div>');
            $('#min', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('minDData')));
            $('#max', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('maxDData')));
            $('#avg', this.el).text(dataExpression.getFormatTrafficData(lastValue.get('avgData')));

            var allChartData = [];
            allChartData.push(resultData);

            if (parseInt($('select#winBoundSelect', this.el).val()) == 1) { //방향>양방향
                this.chartView = Backbone.ChartView.MakeChart($('.popupChart', this.el), {
                    type: "MultiDualLine",
                    chartName: "Chart",
                    collection: allChartData,
                    dataType: this.dataType,
                    svgMainMarginTop: 20,
                    svgMainMarginRight: 45,
                    svgMainMarginBottom: 170,
                    svgMainMarginLeft: 40,
                    svgMiniMarginTop: 310,
                    chartHeight: 325
                });
            } else {
                this.chartView = Backbone.ChartView.MakeChart($('.popupChart', this.el), {
                    type: "MultiLine",
                    chartName: "Chart",
                    collection: allChartData,
                    dataType: this.dataType,
                    svgMainMarginTop: 20,
                    svgMainMarginRight: 45,
                    svgMainMarginBottom: 80,
                    svgMainMarginLeft: 40,
                    svgMiniMarginTop: 235
                });
            }
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