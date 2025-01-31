/**
 * ChartFlowView View
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            bootstrap = require('bootstrap'),
            d3 = require('d3');

    var LineChartCollection = require('collections/detectionAnalysis/lineChartCollection');

    var locale = require('i18n!nls/str');

    var ChartFlowView = Backbone.View.extend({
        template: _.template('<div class="pull-right font-size-11" id="chartDate"></div><div id="analysisChart" width="100%" height="235" class="margin-t5"></div>'),
        initialize: function (options) {
            var thisView = this;
            _.bindAll(this, "makeGraph", "getChartSearchData", "makeChartGraph", "onClose");
            this.searchModel = options.searchModel;
            this.chartName = options.chartName;
            this.url = options.chartUrl;
            this.parentEl = options.parentEl;
            this.dataType = options.dataType;
            this.allChartData = [];

            //this.color = ['#BDBDBD', '#a3d6fd', '#b8e0fe', '#ceeaff', '#e1f2ff', '#f2f9ff'];
            this.color = ['#BDBDBD', '#FFA7A7', '#FFC19E', '#FFE08C', '#B7F0B1', '#B2EBF4'];
            //this.color = ['#BDBDBD', '#FFD8D8', '#FAE0D4', '#FAECC5', '#FAF4C0', '#E4F7BA'];
            //this.color = ['#BDBDBD', '#FFD9EC', '#FFD9FA', '#E8D9FF', '#DAD9FF', '#D9E5FF'];

            //this.chartType = "StackedArea";
            this.chartType = "StackedArea";
            this.chartTypedual = "StackedAreaDual";

//			$(window).on('resize', function() {
//				//thisView.makeChartGraph(thisView.allChartData);
//				
//				if(thisView.searchModel.get("winBoundSelect") == 1){ //방향>양방향
//					thisView.makeInOutChartGraph(thisView.allChartData);
//				}else { //방향>힙계, In, Out
//					thisView.makeChartGraph(thisView.allChartData);
//				}
//			});
        },
        render: function () {
            this.$el.html(this.template());

            return this;
        },
        makeGraph: function () {
            var thisView = this;
            this.allChartData = [];
            //리스트 시간 표시
            $("#chartDate", this.el).text(this.searchModel.get('startDateInput') + " ~ " + this.searchModel.get('endDateInput'));

            //체크되어있는 항목 가져오기
            var checkedItem = [];
            var checkedPlusItem = [];
            var arrStrName = [];
            //background 
            var j = 0;
            //alert($(this.parentEl).closest('.tab-pane').prop("id"));
            $("input[name='graphItemCheck']", thisView.parentEl).each(function(i) {
                if ($("input[name='graphItemCheck']:eq(" + i + ")", thisView.parentEl).prop('checked')) {
                    $("td[name='graphItemCheckTd']:eq(" + i + ")", thisView.parentEl).removeClass().addClass('align-center').addClass('trend-blue-bg-lv'+(j+1));
                    j++;
                } else {
                    $("td[name='graphItemCheckTd']:eq(" + i + ")", thisView.parentEl).removeClass().addClass('align-center');
                }
            });
            //전체 값 세팅
            if ($("#graphItemCheckTotal", thisView.parentEl).prop('checked')) {
                this.color = ['#BDBDBD', '#42828f', '#5ccbb3', '#c4ddb7', '#67b2e4', '#99a4ae'];
                checkedItem[0] = -1;
                checkedPlusItem[0] = -1;
                //항목 별 세팅
                $("input[name='graphItemCheck']:checked", thisView.parentEl).each(function (i) {
                    var strName = $(this).data('strname');
                    if (typeof strName != 'undefined') {
                        arrStrName[i + 1] = strName;
                    }
                    var tempLocation = ($(this).val()).indexOf("|");
                    if (tempLocation > 0) {
                        checkedItem[i + 1] = ($(this).val()).substr(0, tempLocation);
                        checkedPlusItem[i + 1] = ($(this).val()).substring(tempLocation + 1, ($(this).val()).length);
                    } else {
                        checkedItem[i + 1] = ($(this).val());
                        checkedPlusItem[i + 1] = "";
                    }
                });
            } else {
                this.color = ['#42828f', '#5ccbb3', '#c4ddb7', '#67b2e4', '#99a4ae'];

                $("input[name='graphItemCheck']:checked", thisView.parentEl).each(function (i) {
                    var tempLocation = ($(this).val()).indexOf("|");
                    if (tempLocation > 0) {
                        checkedItem[i] = ($(this).val()).substr(0, tempLocation);
                        checkedPlusItem[i] = ($(this).val()).substring(tempLocation + 1, ($(this).val()).length);
                    } else {
                        checkedItem[i] = ($(this).val());
                        checkedPlusItem[i] = "";
                    }
                });
            }

            var thisView = this;
            var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                    parseDate = formatDate.parse;
            if (checkedItem.length > 0) {
                $.each(checkedItem, function (j) {
                    var resultData = {};
                    var time, data;
                    var lineChartCollection = new LineChartCollection();

                    lineChartCollection.fetch({
                        method: "POST",
                        data: JSON.stringify(thisView.getChartSearchData({
                            graphItem: checkedItem[j],
                            graphPlusItem: checkedPlusItem[j]
                        })),
                        url: thisView.url,
                        contentType: 'application/json',
                        reset: true,
                        success: function (collection) {
                            resultData = {};
                            resultData["key"] = j;
                            resultData["color"] = thisView.color[j];
                            var strName = arrStrName[j];
                            if (typeof strName == "undefined") {
                                strName = collection.at(0).get("strName");
                            }
                            resultData["strName"] = strName;
                            resultData.values = [];
                            //debugger
                            if (thisView.searchModel.get("winBoundSelect") == 1) { //방향>양방향
                                for (var i = 0; i < collection.length; i++) {
                                    var model = collection.at(i);
                                    resultData.values.push({
                                        "time": parseDate(model.get('time')),
                                        "value": model.get('dataIn') + model.get('dataOut'),
                                        "valueIn": model.get('dataIn'),
                                        "valueOut": model.get('dataOut')
                                    });
                                }
                            } else { //방향>힙계,In,Out
                                for (var i = 0; i < collection.length; i++) {
                                    var model = collection.at(i);
                                    resultData.values.push({
                                        "time": parseDate(model.get('time')),
                                        "value": model.get('data')
                                    });
                                }
                            }

                            thisView.allChartData.push(resultData);
                            thisView.allChartData = thisView.sortByKey(thisView.allChartData, "key");
                            if (thisView.allChartData.length == checkedItem.length) {
                                if (thisView.searchModel.get("winBoundSelect") == 1) { //방향>양방향
                                    thisView.makeInOutChartGraph(thisView.allChartData);
                                } else { //방향>힙계, In, Out
                                    thisView.makeChartGraph(thisView.allChartData);
                                }
                            }
                        },
                        beforeSend: function () {
                            if (j == 0) {
                                if (!$("#loading").hasClass("loading-backdrop")) {
                                    Backbone.Loading.setLoading($('body'));
                                }
                            }
                        },
                        complete: function () {
                            if (j == (checkedItem.length - 1)) {
                                Backbone.Loading.removeLoading($('body'));
                            }
                        }
                    });
                });
            } else {
                var ModalContent = Backbone.View.extend({
                    render: function () {
                        this.$el.html('<p class="modal-body-white-padding">' + locale.ThereIsNoSelectedItem + '</p>');
                        return this;
                    }
                });
                Backbone.ModalView.msg({
                    title: "info",
                    size: "small",
                    body: new ModalContent()
                });
            }
        },
        getChartSearchData: function (item) {
            var params = $.extend({}, this.searchModel.toJSON(), item);
            return params;
        },
        makeChartGraph: function (tempAllChartData) {
            var thisView = this;

            this.chartView = Backbone.ChartView.MakeChart(this.$('#analysisChart', thisView.el), {
                title: '',
                type: thisView.chartType,
                chartName: thisView.chartName + $(thisView.parentEl).closest('.tab-pane').prop("id"),
                parentEl: thisView.parentEl,
                collection: tempAllChartData,
                dataType: thisView.dataType,
                svgMainMarginTop: 20,
                svgMainMarginRight: 30,
                svgMainMarginBottom: 80,
                svgMainMarginLeft: 45,
                svgMiniMarginTop: 235
            });
        },
        makeInOutChartGraph: function (tempAllChartData) {
            var thisView = this;
            this.chartView = Backbone.ChartView.MakeChart(this.$('#analysisChart', thisView.el), {
                title: '',
                type: this.chartTypedual,
                chartName: thisView.chartName + $(thisView.parentEl).closest('.tab-pane').prop("id"),
                parentEl: thisView.parentEl,
                collection: tempAllChartData,
                dataType: thisView.dataType,
                svgMainMarginTop: 20,
                svgMainMarginRight: 30,
                svgMainMarginBottom: 170,
                svgMainMarginLeft: 45,
                svgMiniMarginTop: 235
            });
        },
        sortByKey: function (array, key) {
            return array.sort(function (a, b) {
                var x = a[key];
                var y = b[key];
                return((x > y) ? -1 : ((x < y) ? 1 : 0))
            });
        },
        onClose: function () {
            if (this.chartView) {
                this.chartView.close();
            }
        }
    });

    return ChartFlowView;
});
