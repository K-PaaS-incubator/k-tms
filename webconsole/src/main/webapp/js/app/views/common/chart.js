define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            bootstrap = require('bootstrap'),
            d3 = require('d3');
    // 차트 시간에 따른 변경 ------------------------------------
    var formatMillisecond = d3.time.format(".%L"),
            formatSecond = d3.time.format(":%S"),
            formatMinute = d3.time.format("%H:%M"),
            formatHour = d3.time.format("%H:%M"),
            formatDay = d3.time.format("%a %d"),
            formatWeek = d3.time.format("%b %d"),
            formatMonth = d3.time.format("%B"),
            formatYear = d3.time.format("%Y");

    function multiFormat(date) {
        return (d3.time.second(date) < date ? formatMillisecond
                : d3.time.minute(date) < date ? formatSecond
                : d3.time.hour(date) < date ? formatMinute
                : d3.time.day(date) < date ? formatHour
                : d3.time.month(date) < date ? (d3.time.week(date) < date ? formatDay : formatWeek)
                : d3.time.year(date) < date ? formatMonth
                : formatYear)(date);
    }
    // =================================================================
    // require i18n
    var locale = require('i18n!nls/str');

    // require template
    var tpl = require('text!tpl/common/chart.html');
    var tplWithList = require('text!tpl/common/chartWithList.html');
    var tplAnalysis = require('text!tpl/common/chartAnalysis.html');
    var tplAnalysisWithTooltip = require('text!tpl/common/chartAnalysisWithTooltip.html');

    var dataExpression = require('utils/dataExpression');

    var ChartView = Backbone.View.extend({
        types: ['LinePlusLine', 'LinePlusLineWithArea', 'LinePlusBar', 'Bubble', 'MultiLine', 'MultiDualLine', 'PieDonut', 'TwoLine', 'MultiPieDonut', 'PiePlusBar', 'StackedArea', 'StackedAreaDual'],
        template: _.template(tpl),
        templateWithList: _.template(tplWithList),
        templateAnalysis: _.template(tplAnalysis),
        templateAnalysisWithTooltip: _.template(tplAnalysisWithTooltip),
        initialize: function (options) {
            var title = options.title || '';
            var type = options.hasOwnProperty('type') ? options.type : 'LinePlusLine';
            var chartName = options.hasOwnProperty('chartName') ? options.chartName : 'TotMalTraffic';
            var collection = options.collection || '';
            var percentValue = options.percentValue || '';
            var dataType = options.dataType;
            var chartHeight = options.chartHeight || '250';

            var svgMainMarginTop = options.hasOwnProperty('svgMainMarginTop') ? options.svgMainMarginTop : 30;
            var svgMainMarginRight = options.hasOwnProperty('svgMainMarginRight') ? options.svgMainMarginRight : 80;
            var svgMainMarginBottom = options.hasOwnProperty('svgMainMarginBottom') ? options.svgMainMarginBottom : 20;
            var svgMainMarginLeft = options.hasOwnProperty('svgMainMarginLeft') ? options.svgMainMarginLeft : 100;

            var svgMiniMarginTop = options.hasOwnProperty('svgMiniMarginTop') ? options.svgMiniMarginTop : 430;

            if (_.indexOf(this.types, type) === -1) {
                throw new Error('Invalid type: [' + type + '] Must be one of: ' + this.types.join(', '));
            }

            this.type = type;
            this.title = title;
            this.chartName = chartName;
            this.collection = collection;
            this.percentValue = percentValue;
            this.dataType = options.dataType;
            this.chartHeight = chartHeight;
            this.parentEl = options.parentEl;
            this.svgMainMarginTop = svgMainMarginTop;
            this.svgMainMarginRight = svgMainMarginRight;
            this.svgMainMarginBottom = svgMainMarginBottom;
            this.svgMainMarginLeft = svgMainMarginLeft;
            this.svgMiniMarginTop = svgMiniMarginTop;
        },
        render: function () {
            var outputChart = this.template({
                type: this.type,
                title: this.title,
                chartName: this.chartName,
                collection: this.collection,
                parentEl: this.parentEl,
                locale: locale
            });

            this.$el.html(outputChart);
            return this;
        },
        linePlusLine: function () {
            var svg;
            var chartDivName = this.chartName;
            var collection = this.collection;
            var data;
            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;
            var chartHeight = this.chartHeight;

            require(['d3'], function (d3) {
                var $chartDiv = $('#' + chartDivName + '_chart');
                $chartDiv.empty();
                var divWid = $chartDiv.width(),
//                    divHei = $chartDiv.height();
                        divHei = chartHeight;

                var main_margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                main_width = divWid - main_margin.left - main_margin.right,
                        main_height = divHei - main_margin.top - main_margin.bottom;

                var formatDate = d3.time.format("%y-%m-%d %H:%M"),
                        formatDate2 = d3.time.format("%H:%M"),
                        parseDate = formatDate.parse,
                        bisectDate = d3.bisector(function (d) {
                            return d.Time;
                        }).left,
                        formatOutput0 = function (d) {
                            return formatDate2(d.Time) + " - " + dataExpression.getFormatTrafficData(d.TrafficBps) + " bps";
                        },
                        formatOutput1 = function (d) {
                            return formatDate2(d.Time) + " - " + dataExpression.getFormatTrafficData(d.EventBps) + " bps";
                        };

                var main_x = d3.time.scale()
                        .range([0, main_width]);

                var main_y0 = d3.scale.sqrt()
                        .range([main_height, 0]),
                        main_y1 = d3.scale.sqrt()
                        .range([main_height, 0]);

                var main_xAxis = d3.svg.axis()
                        .scale(main_x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var main_yAxisLeft = d3.svg.axis()
                        .scale(main_y0)
                        .ticks(5)
                        .orient("left")
                        .tickFormat(dataExpression.bytesToString);

                var main_yAxisRight = d3.svg.axis()
                        .scale(main_y1)
                        .ticks(5)
                        .orient("right")
                        .tickFormat(dataExpression.bytesToString);

                var main_area0 = d3.svg.area()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y0(main_height)
                        .y1(function (d) {
                            return main_y0(d.TrafficBps);
                        });

                var main_area1 = d3.svg.area()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y0(main_height)
                        .y1(function (d) {
                            return main_y1(d.EventBps);
                        });

                var main_line0 = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y(function (d) {
                            return main_y0(d.TrafficBps);
                        });

                var main_line1 = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y(function (d) {
                            return main_y1(d.EventBps);
                        });

                svg = d3.select("#" + chartDivName + "_chart")
                        .attr("width", divWid)
                        .attr("height", divHei)
                        .attr('viewBox', '0 0 ' + Math.min(divWid, divHei) + ' ' + Math.min(divWid, divHei))
                        .attr('preserveAspectRatio', 'xMinYMin');

                var main = svg.append("g")
                        .attr("transform", "translate(" + main_margin.left + "," + main_margin.top + ")");

                //데이터조작
                data = d3.csv.parse(collection);
                data.forEach(function (d) {
                    d.Time = parseDate(d.Time);
                    d.TrafficBps = +d.TrafficBps;
                    d.EventBps = +d.EventBps;
                });

                data.sort(function (a, b) {
                    return a.Time - b.Time;
                });

                var XMinMax = d3.extent(data, function (d) {
                    return d.Time;
                });
                var Y0MinMax = d3.extent(data, function (d) {
                    return d.TrafficBps;
                });
                var Y1MinMax = d3.extent(data, function (d) {
                    return d.EventBps;
                });

                //				d3.max(data, function (kv) { return d3.max(kv.values, function (d) { return d.valueOut; }) }),
                //		        d3.min(data, function (kv) { return d3.min(kv.values, function (d) { return d.valueOut; }) })

                main_x.domain(XMinMax);
                main_y0.domain([
                    d3.min(data, function (d) {
                        return d.TrafficBps
                    }) * 0.95,
                    d3.max(data, function (d) {
                        return d.TrafficBps
                    })
                ]);
                main_y1.domain([
                    d3.min(data, function (d) {
                        return d.EventBps
                    }) * 0.95,
                    d3.max(data, function (d) {
                        return d.EventBps
                    }) * 1.3
                ]);

                main.append("path")
                        .datum(data)
                        .attr("class", "area0")
                        //                    .attr("clip-path", "url(#clip)")
                        .attr("d", main_area0);

                main.append("path")
                        .datum(data)
                        .attr("class", "area1")
                        //                    .attr("clip-path", "url(#clip)")
                        .attr("d", main_area1);

                // Draw the y Grid lines
                main.append("g")
                        .attr("class", "grid")
                        .call(make_y_axis()
                                .tickSize(-main_width, 0, 0)
                                .tickFormat("")
                                )
                /*
                 // Draw the y Grid lines
                 main.append("g")            
                 .attr("class", "grid")
                 .attr("transform", "translate(0," + main_height + ")")
                 .call(make_x_axis()
                 .tickSize(-main_height, 0, 0)
                 .tickFormat("")
                 )
                 */

                /*
                 main.append("path")
                 .datum(data)
                 .attr("class", "line line0")
                 .attr("d", main_line0);
                 */

                main.append("path")
                        .datum(data)
                        .attr("class", "line line1")
                        .attr("d", main_line1);

                main.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + main_height + ")")
                        .call(main_xAxis)
                        .selectAll('text')
                        .style('text-anchor', 'end')
                        .attr('dx', '-.8em')
                        .attr('dy', '.15em')
                        .attr("transform", "rotate(-30)");

                //왼쪽 Y축
                main.append("g")
                        .attr("class", "y axis axisLeft")
                        .call(main_yAxisLeft)
                        .append("text")
                        .attr("x", 5)
                        .attr("y", -15)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("bps");

                //오른쪽 Y축
                main.append("g")
                        .attr("class", "y axis axisRight")
                        .attr("transform", "translate(" + main_width + ", 0)")
                        .call(main_yAxisRight)
                        .append("text")
                        .attr("x", 7)
                        .attr("y", -15)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("bps");

                var rtn0 = false, rtn1 = false;
                data.forEach(function (d) {
                    //전체트래픽 Min값
                    //					if(d.TrafficBps == Y0MinMax[0]){
                    //						var minPointY0 = main.append("g").attr("class","focus").style("display", "");
                    //						minPointY0.append("circle").attr("class", "maxY0").attr("r", 4);
                    //						minPointY0.append("text").attr("class", "y0").attr("dy", "-5").attr("dx", "0.5em");  
                    //						minPointY0.select("circle.maxY0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[0]) + ")");
                    //						minPointY0.select("text.y0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[0]) + ")").text("Min");
                    //					}
                    //전체트래픽 Max값
                    if (d.TrafficBps == Y0MinMax[1] && Y0MinMax[1] > 0 && rtn0 == false) {

                        var tmp_chart_width = $("#" + chartDivName + "_chart").attr('width');
                        var max_pos0_txt_x;
                        var max_pos0_txt_y;

                        if (tmp_chart_width < main_x(d.Time) + 120)
                            max_pos0_txt_x = main_x(d.Time) - 35;
                        else
                            max_pos0_txt_x = main_x(d.Time) + 3;

                        max_pos0_txt_y = parseInt(main_y0(Y0MinMax[1])) + 10;

                        var maxPointY0 = main.append("g").attr("class", "focus").style("display", "");
                        maxPointY0.append("circle").attr("class", "maxY0").attr("r", 4);
                        maxPointY0.append("text").attr("class", "y0").attr("dy", "-5").attr("dx", "0.5em");
                        maxPointY0.select("circle.maxY0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[1]) + ")");
                        maxPointY0.select("text.y0").attr("transform", "translate(" + max_pos0_txt_x + "," + max_pos0_txt_y + ")").text("Max");
                        rtn0 = true;
                    }
                    //유해트래픽 Min값
                    //					if(d.EventBps == Y1MinMax[0]){
                    //						var minPointY1 = main.append("g").attr("class","focus").style("display", "");
                    //						minPointY1.append("circle").attr("class", "maxY1").attr("r", 4);
                    //						minPointY1.append("text").attr("class", "y1").attr("dy", "-1").attr("dx", "0.5em");  
                    //						minPointY1.select("circle.maxY1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[0]) + ")");
                    //						minPointY1.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[0]) + ")").text("Min");
                    //					}
                    //유해트래픽 Max값
                    if (d.EventBps == Y1MinMax[1] && Y1MinMax[1] > 0 && rtn1 == false) {

                        var tmp_chart_width = $("#" + chartDivName + "_chart").attr('width');
                        var max_pos1_txt_x;
                        var max_pos1_txt_y;

                        if (tmp_chart_width < main_x(d.Time) + 120)
                            max_pos1_txt_x = main_x(d.Time) - 35;
                        else
                            max_pos1_txt_x = main_x(d.Time) + 3;

                        max_pos1_txt_y = parseInt(main_y1(Y1MinMax[1])) + 7;

                        var maxPointY1 = main.append("g").attr("class", "focus").style("display", "");
                        maxPointY1.append("circle").attr("class", "maxY1").attr("r", 4);
                        maxPointY1.append("text").attr("class", "y1").attr("dy", "-1").attr("dx", "0.5em");
                        maxPointY1.select("circle.maxY1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[1]) + ")");
                        maxPointY1.select("text.y1").attr("transform", "translate(" + max_pos1_txt_x + "," + max_pos1_txt_y + ")").text("Max");
                        rtn1 = true;
                    }
                });

                var focus = main.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("line")
                        .attr("class", "y0")
                        .attr("x1", main_width)
                        .attr("x2", main_width);

                focus.append("line")
                        .attr("class", "y1")
                        .attr("x1", main_width)
                        .attr("x2", main_width);

                focus.append("circle")
                        .attr("class", "y0")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y0")
                        .attr("dy", "-1em");

                focus.append("circle")
                        .attr("class", "y1")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y1")
                        .attr("dy", "-1em");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", main_width)
                        .attr("height", main_height)
                        .on("mouseover", function () {
                            focus.style("display", null);
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                        })
                        .on("mousemove", mousemove);

                /* Create a shared transition for anything we're animating */
                /* Add 'curtain' rectangle to hide entire graph */
                /*
                 var curtain = svg.append('rect')
                 .attr('x', -1 * main_width)
                 .attr('y', -1 * main_height)
                 .attr('height', main_height)
                 .attr('width', main_width)
                 .attr('class', 'curtain')
                 .attr('transform', 'rotate(180)')
                 .style('fill', '#ffffff')
                 */
                /* Optionally add a guideline */
                /*
                 var guideline = svg.append('line')
                 .attr('stroke', '#333')
                 .attr('stroke-width', 0)
                 .attr('class', 'guide')
                 .attr('x1', 1)
                 .attr('y1', 1)
                 .attr('x2', 1)
                 .attr('y2', main_height)
                 */
                /* Create a shared transition for anything we're animating */
                /*
                 var t = svg.transition()
                 .delay(750)
                 .duration(6000)
                 .ease('linear')
                 .each('end', function() {
                 d3.select('line.guide')
                 .transition()
                 .style('opacity', 0)
                 .remove()
                 });
                 
                 t.select('rect.curtain')
                 .attr('width', 0);
                 t.select('line.guide')
                 .attr('transform', 'translate(' + main_width + ', 0)')
                 
                 */
                /*
                 t.select('rect.curtain')
                 .attr('width', 0);
                 t.select('line.guide')
                 .attr('transform', 'translate(' + width + ', 0)')
                 
                 d3.select("#show_guideline").on("change", function(e) {
                 guideline.attr('stroke-width', this.checked ? 1 : 0);
                 curtain.attr("opacity", this.checked ? 0.75 : 1);
                 })
                 */

                function mousemove() {
                    var x0 = main_x.invert(d3.mouse(this)[0]),
                            i = bisectDate(data, x0, 1),
                            d0 = data[i - 1],
                            d1 = data[i],
                            d2 = data[i - 16],
                            d = x0 - d0.Time > d1.Time - x0 ? d1 : d0;

                    focus.select("circle.y0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(d.TrafficBps) + ")");

                    if (i > data.length / 2) {
                        focus.select("text.y0").attr("transform", "translate(" + main_x(d2.Time) + "," + main_y0(d.TrafficBps) + ")").text(formatOutput0(d)).style("text-anchor", "end");
                    } else {
                        focus.select("text.y0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(d.TrafficBps) + ")").text(formatOutput0(d)).style("text-anchor", "start");
                    }
                    focus.select("circle.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(d.EventBps) + ")");

                    if (i > data.length / 2) {
                        focus.select("text.y1").attr("transform", "translate(" + main_x(d2.Time) + "," + main_y1(d.EventBps) + ")").text(formatOutput1(d)).style("text-anchor", "end");
                    } else {
                        focus.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(d.EventBps) + ")").text(formatOutput1(d)).style("text-anchor", "start");
                    }
                    focus.select(".y0").attr("transform", "translate(" + main_width * -1 + ", " + main_y0(d.TrafficBps) + ")").attr("x2", main_width + main_x(d.Time));
                    focus.select(".y1").attr("transform", "translate(0, " + main_y1(d.EventBps) + ")").attr("x1", main_x(d.Time));
                }
                //});

                // function for the x grid lines
                function make_x_axis() {
                    return d3.svg.axis()
                            .scale(main_x)
                            .orient("bottom")
                            .ticks(13)
                }

                // function for the y grid lines
                function make_y_axis() {
                    return d3.svg.axis()
                            .scale(main_y0)
                            .orient("left")
                            .ticks(3)
                }
            });

            var outputChart = this.template({
                type: this.type,
                title: this.title,
                chartName: this.chartName,
                chart: this.svg,
                locale: locale
            });

            this.$el.html(outputChart);
            return this;
        },
        linePlusLineWithArea: function () {
            var svg;
            var chartDivName = this.chartName;
            var collection = this.collection;
            var data;
            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;
            var chartHeight = this.chartHeight;

            require(['d3'], function (d3) {
                var $chartDiv = $('#' + chartDivName + '_chart');
                $chartDiv.empty();
                var divWid = $chartDiv.width(),
                        //              divHei = $chartDiv.height();
                        divHei = chartHeight;

                var main_margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                main_width = divWid - main_margin.left - main_margin.right,
                        main_height = divHei - main_margin.top - main_margin.bottom;

                var formatDate = d3.time.format("%y-%m-%d %H:%M"),
                        formatDate2 = d3.time.format("%H:%M"),
                        parseDate = formatDate.parse,
                        bisectDate = d3.bisector(function (d) {
                            return d.Time;
                        }).left,
                        formatOutput0 = function (d) {
                            return formatDate2(d.Time) + " - " + dataExpression.getFormatTrafficData(d.EventCount) + ""; // 툴팁 단위 변환 numberFormat에서 getFormatTrafficData으로 
                        },
                        formatOutput1 = function (d) {
                            return formatDate2(d.Time) + " - " + dataExpression.getFormatTrafficData(d.EventBps) + " bps";
                        };

                var main_x = d3.time.scale()
                        .range([0, main_width]);

                var main_y0 = d3.scale.sqrt()
                        .range([main_height, 0]),
                        main_y1 = d3.scale.sqrt()
                        .range([main_height, 0]);

                var main_xAxis = d3.svg.axis()
                        .scale(main_x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var main_yAxisLeft = d3.svg.axis()
                        .scale(main_y0)
                        .ticks(5)
                        .orient("left")
                        .tickFormat(dataExpression.bytesToString);			// 공격건수/유해트래픽 y축 count 단위 변환

                var main_yAxisRight = d3.svg.axis()
                        .scale(main_y1)
                        .ticks(5)
                        .orient("right")
                        .tickFormat(dataExpression.bytesToString);

                var main_area0 = d3.svg.area()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y0(main_height)
                        .y1(function (d) {
                            return main_y0(d.EventCount);
                        });

                var main_area1 = d3.svg.area()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y0(main_height)
                        .y1(function (d) {
                            return main_y1(d.EventBps);
                        });

                var main_line0 = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y(function (d) {
                            return main_y0(d.EventCount);
                        });

                var main_line1 = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y(function (d) {
                            return main_y1(d.EventBps);
                        });

                svg = d3.select("#" + chartDivName + "_chart")
                        .attr("width", divWid)
                        .attr("height", divHei)
                        .attr('viewBox', '0 0 ' + Math.min(divWid, divHei) + ' ' + Math.min(divWid, divHei))
                        .attr('preserveAspectRatio', 'xMinYMin');

                var main = svg.append("g")
                        .attr("transform", "translate(" + main_margin.left + "," + main_margin.top + ")");

                //데이터조작
                data = d3.csv.parse(collection);
                data.forEach(function (d) {
                    d.Time = parseDate(d.Time);
                    d.EventCount = +d.EventCount;
                    d.EventBps = +d.EventBps;
                });

                data.sort(function (a, b) {
                    return a.Time - b.Time;
                });

                var XMinMax = d3.extent(data, function (d) {
                    return d.Time;
                });
                var Y0MinMax = d3.extent(data, function (d) {
                    return d.EventCount;
                });
                var Y1MinMax = d3.extent(data, function (d) {
                    return d.EventBps;
                });

                //				d3.max(data, function (kv) { return d3.max(kv.values, function (d) { return d.valueOut; }) }),
                //		        d3.min(data, function (kv) { return d3.min(kv.values, function (d) { return d.valueOut; }) })

                main_x.domain(XMinMax);
                main_y0.domain([d3.min(data, function (d) {
                        return d.EventCount
                    }) * 0.95,
                    d3.max(data, function (d) {
                        return d.EventCount
                    }) * 1.01
                ]);
                main_y1.domain([d3.min(data, function (d) {
                        return d.EventBps
                    }) * 0.95,
                    d3.max(data, function (d) {
                        return d.EventBps
                    }) * 1.3
                ]);

                main.append("path")
                        .datum(data)
                        .attr("class", "area0")
                        //                    .attr("clip-path", "url(#clip)")
                        .attr("d", main_area0);

                main.append("path")
                        .datum(data)
                        .attr("class", "area1")
                        .attr("d", main_area1);

                // Draw the y Grid lines
                main.append("g")
                        .attr("class", "grid")
                        .call(make_y_axis()
                                .tickSize(-main_width, 0, 0)
                                .tickFormat("")
                                )

                /*
                 // Draw the y Grid lines
                 main.append("g")            
                 .attr("class", "grid")
                 .attr("transform", "translate(0," + main_height + ")")
                 .call(make_x_axis()
                 .tickSize(-main_height, 0, 0)
                 .tickFormat("")
                 )
                 */

                /*
                 main.append("path")
                 .datum(data)
                 .attr("class", "line line0")
                 .attr("d", main_line0);
                 */

                main.append("path")
                        .datum(data)
                        .attr("class", "line line1")
                        .attr("d", main_line1);

                main.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + main_height + ")")
                        .call(main_xAxis)
                        .selectAll('text')
                        .style('text-anchor', 'end')
                        .attr('dx', '-.8em')
                        .attr('dy', '.15em')
                        .attr("transform", "rotate(-30)");

                //왼쪽 Y축
                main.append("g")
                        .attr("class", "y axis axisLeft")
                        .call(main_yAxisLeft)
                        .append("text")
                        .attr("x", 4)
                        .attr("y", -15)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("count");							// TODO 축에 text append

                //오른쪽 Y축
                main.append("g")
                        .attr("class", "y axis axisRight")
                        .attr("transform", "translate(" + main_width + ", 0)")
                        .call(main_yAxisRight)
                        .append("text")
                        .attr("x", 7)
                        .attr("y", -15)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("bps");

                var rtn0 = false, rtn1 = false;
                data.forEach(function (d) {
                    /*
                     //전체트래픽 Min값
                     if(d.EventCount == Y0MinMax[0]){
                     var minPointY0 = main.append("g").attr("class","focus").style("display", "");
                     minPointY0.append("circle").attr("class", "maxY0").attr("r", 4);
                     minPointY0.append("text").attr("class", "y0").attr("dy", "-10").attr("dx", "0.5em");  
                     minPointY0.select("circle.maxY0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[0]) + ")");
                     minPointY0.select("text.y0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[0]) + ")").text("Min");
                     }*/
                    //전체트래픽 Max값
                    if (d.EventCount == Y0MinMax[1] && Y0MinMax[1] > 0 && rtn0 == false) {

                        var tmp_chart_width = $("#" + chartDivName + "_chart").attr('width');
                        var max_pos0_txt_x;
                        var max_pos0_txt_y;

                        if (tmp_chart_width < main_x(d.Time) + 120)
                            max_pos0_txt_x = main_x(d.Time) - 35;
                        else
                            max_pos0_txt_x = main_x(d.Time) + 3;

                        max_pos0_txt_y = parseInt(main_y0(Y0MinMax[1])) + 14;

                        var maxPointY0 = main.append("g").attr("class", "focus").style("display", "");
                        maxPointY0.append("circle").attr("class", "maxY0").attr("r", 4);
                        maxPointY0.append("text").attr("class", "y0").attr("dy", "-10").attr("dx", "0.5em");
                        maxPointY0.select("circle.maxY0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[1]) + ")");
                        maxPointY0.select("text.y0").attr("transform", "translate(" + max_pos0_txt_x + "," + max_pos0_txt_y + ")").text("Max");
                        rtn0 = true;
                    }
                    //유해트래픽 Min값
                    //                    if (d.EventBps == Y1MinMax[0]) {
                    //                        var minPointY1 = main.append("g").attr("class", "focus").style("display", "");
                    //                        minPointY1.append("circle").attr("class", "maxY1").attr("r", 4);
                    //                        minPointY1.append("text").attr("class", "y1").attr("dy", "5").attr("dx", "0.5em");
                    //                        minPointY1.select("circle.maxY1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[0]) + ")");
                    //                        minPointY1.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[0]) + ")").text("Min");
                    //                    }
                    //유해트래픽 Max값
                    if (d.EventBps == Y1MinMax[1] && Y1MinMax[1] > 0 && rtn1 == false) {

                        var tmp_chart_width = $("#" + chartDivName + "_chart").attr('width');
                        var max_pos1_txt_x;
                        var max_pos1_txt_y;

                        if (tmp_chart_width < main_x(d.Time) + 120)
                            max_pos1_txt_x = main_x(d.Time) - 35;
                        else
                            max_pos1_txt_x = main_x(d.Time) + 3;

                        max_pos1_txt_y = parseInt(main_y1(Y1MinMax[1]));

                        var maxPointY1 = main.append("g").attr("class", "focus").style("display", "");
                        maxPointY1.append("circle").attr("class", "maxY1").attr("r", 4);
                        maxPointY1.append("text").attr("class", "y1").attr("dy", "5").attr("dx", "0.5em");
                        maxPointY1.select("circle.maxY1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[1]) + ")");
                        maxPointY1.select("text.y1").attr("transform", "translate(" + max_pos1_txt_x + "," + max_pos1_txt_y + ")").text("Max");
                        rtn1 = true;
                    }
                });

                var focus = main.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("line")
                        .attr("class", "y0")
                        .attr("x1", main_width)
                        .attr("x2", main_width);

                focus.append("line")
                        .attr("class", "y1")
                        .attr("x1", main_width)
                        .attr("x2", main_width);

                focus.append("circle")
                        .attr("class", "y0")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y0")
                        .attr("dy", "-0.5em");

                focus.append("circle")
                        .attr("class", "y1")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y1")
                        .attr("dy", "-0.5em");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", main_width)
                        .attr("height", main_height)
                        .on("mouseover", function () {
                            focus.style("display", null);
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                        })
                        .on("mousemove", mousemove);

                function mousemove() {
                    var x0 = main_x.invert(d3.mouse(this)[0]),
                            i = bisectDate(data, x0, 1),
                            d0 = data[i - 1],
                            d1 = data[i],
                            d2 = data[i - 16],
                            d = x0 - d0.Time > d1.Time - x0 ? d1 : d0;

                    focus.select("circle.y0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(d.EventCount) + ")");
                    if (i > data.length / 2) {
                        focus.select("text.y0").attr("transform", "translate(" + main_x(d2.Time) + "," + main_y0(d.EventCount) + ")").text(formatOutput0(d)).style("text-anchor", "end");
                    } else {
                        focus.select("text.y0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(d.EventCount) + ")").text(formatOutput0(d)).style("text-anchor", "start");
                    }
                    //focus.select("text.y0").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(d.EventCount) + ")").text(formatOutput0(d));
                    focus.select("circle.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(d.EventBps) + ")");
                    if (i > data.length / 2) {
                        focus.select("text.y1").attr("transform", "translate(" + main_x(d2.Time) + "," + main_y1(d.EventBps) + ")").text(formatOutput1(d)).style("text-anchor", "end");
                    } else {
                        focus.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(d.EventBps) + ")").text(formatOutput1(d)).style("text-anchor", "start");
                    }
                    //focus.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(d.EventBps) + ")").text(formatOutput1(d));
                    focus.select(".y0").attr("transform", "translate(" + main_width * -1 + ", " + main_y0(d.EventCount) + ")").attr("x2", main_width + main_x(d.Time));
                    focus.select(".y1").attr("transform", "translate(0, " + main_y1(d.EventBps) + ")").attr("x1", main_x(d.Time));
                }
                //});

                // function for the x grid lines
                function make_x_axis() {
                    return d3.svg.axis()
                            .scale(main_x)
                            .orient("bottom")
                            .ticks(13)
                }

                // function for the y grid lines
                function make_y_axis() {
                    return d3.svg.axis()
                            .scale(main_y0)
                            .orient("left")
                            .ticks(3)
                }
            });

            var outputChart = this.template({
                type: this.type,
                title: this.title,
                chartName: this.chartName,
                chart: this.svg,
                locale: locale
            });

            this.$el.html(outputChart);
            return this;
        },
        linePlusBar: function () {
            var svg;
            var chartDivName = this.chartName;
            var collection = this.collection;
            var data;

            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;

            require(['d3'], function (d3) {
                var $chartDiv = $('#' + chartDivName + '_chart');
                $chartDiv.empty();
                var divWid = $chartDiv.width(),
                        divHei = $chartDiv.height();

                var main_margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                main_width = divWid - main_margin.left - main_margin.right,
                        main_height = divHei - main_margin.top - main_margin.bottom;

                var formatDate = d3.time.format("%y-%m-%d %H:%M"),
                        formatDate2 = d3.time.format("%H:%M"),
                        parseDate = formatDate.parse,
                        bisectDate = d3.bisector(function (d) {
                            return d.Time;
                        }).left,
                        formatOutput0 = function (d) {
                            return formatDate2(d.Time) + " - " + dataExpression.getFormatTrafficData(d.EventCount) + " " + locale.dashboard.count;
                        },
                        formatOutput1 = function (d) {
                            return formatDate2(d.Time) + " - " + dataExpression.getFormatTrafficData(d.EventBps) + " bps";
                        };

                var main_x = d3.time.scale()
                        .range([0, main_width]); //.range([0, main_width-4]);

                var main_y0 = d3.scale.sqrt()
                        .range([main_height, 0]),
                        main_y1 = d3.scale.sqrt()
                        .range([main_height, 0]);

                var main_xAxis = d3.svg.axis()
                        .scale(main_x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var main_yAxisLeft = d3.svg.axis()
                        .scale(main_y0)
                        .ticks(5)
                        .orient("left")
                        .tickFormat(dataExpression.countToString);

                var main_yAxisRight = d3.svg.axis()
                        .scale(main_y1)
                        .ticks(5)
                        .orient("right")
                        .tickFormat(dataExpression.bytesToString);

                var main_line0 = d3.scale.linear();

                var main_line1 = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return main_x(d.Time);
                        })
                        .y(function (d) {
                            return main_y1(d.EventBps);
                        });

                svg = d3.select("#" + chartDivName + "_chart")
                        .attr("width", divWid)
                        .attr("height", divHei)
                        .attr('viewBox', '0 0 ' + Math.min(divWid, divHei) + ' ' + Math.min(divWid, divHei))
                        .attr('preserveAspectRatio', 'xMinYMin');

                var main = svg.append("g")
                        .attr("transform", "translate(" + main_margin.left + "," + main_margin.top + ")");

                //데이터조작
                data = d3.csv.parse(collection);
                data.forEach(function (d) {
                    d.Time = parseDate(d.Time);
                    d.EventCount = +d.EventCount;
                    d.EventBps = +d.EventBps;
                });

                data.sort(function (a, b) {
                    return a.Time - b.Time;
                });

                var XMinMax = d3.extent(data, function (d) {
                    return d.Time;
                });
                var Y0MinMax = d3.extent(data, function (d) {
                    return d.EventCount;
                });
                var Y1MinMax = d3.extent(data, function (d) {
                    return d.EventBps;
                });

                main_x.domain(XMinMax);
                main_y0.domain(Y0MinMax);
                main_y1.domain(Y1MinMax);

                // Draw the y Grid lines
                main.append("g")
                        .attr("class", "grid")
                        .attr("transform", "translate(0," + main_height + ")")
                        .call(make_x_axis()
                                .tickSize(-main_height, 0, 0)
                                .tickFormat("")
                                )

                main.selectAll(".bar")
                        .data(data)
                        .enter().append("rect")
                        .attr("class", "bar")
                        .attr("x", function (d) {
                            return main_x(d.Time) - 2
                        })
                        .attr("width", "4px")
                        .attr("y", function (d) {
                            return main_y0(d.EventCount);
                        })
                        .attr("height", function (d) {
                            return main_height - main_y0(d.EventCount);
                        })
                        .attr("d", main_line0);

                main.append("path")
                        .datum(data)
                        .attr("class", "line line1")
                        .attr("d", main_line1);

                main.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + main_height + ")")
                        .call(main_xAxis);

                //왼쪽 Y축
                main.append("g")
                        .attr("class", "y axis axisLeft2")
                        .call(main_yAxisLeft)
                        .append("text")
                        .attr("x", svgMainMarginLeft + 3)
                        .attr("y", 0)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("Event Count");

                //오른쪽 Y축
                main.append("g")
                        .attr("class", "y axis axisRight")
                        .attr("transform", "translate(" + main_width + ", 0)")
                        .call(main_yAxisRight)
                        .append("text")
                        .attr("x", -4)
                        .attr("y", 0)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("Malicious Traffic");

                data.forEach(function (d) {
                    //전체트래픽 Min값
                    if (d.EventCount == Y0MinMax[0]) {
                        var minPointY0 = main.append("g").attr("class", "focus").style("display", "");
                        minPointY0.append("circle").attr("class", "maxY2").attr("r", 4);
                        minPointY0.append("text").attr("class", "y2").attr("dy", "-14").attr("dx", "0.5em");
                        minPointY0.select("circle.maxY2").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[0]) + ")");
                        minPointY0.select("text.y2").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[0]) + ")").text("Min : " + dataExpression.getFormatTrafficData(Y0MinMax[0]) + " " + locale.dashboard.count);
                    }
                    //전체트래픽 Max값
                    if (d.EventCount == Y0MinMax[1]) {
                        var maxPointY0 = main.append("g").attr("class", "focus").style("display", "");
                        maxPointY0.append("circle").attr("class", "maxY2").attr("r", 4);
                        maxPointY0.append("text").attr("class", "y2").attr("dy", "-14").attr("dx", "0.5em");
                        maxPointY0.select("circle.maxY2").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[1]) + ")");
                        maxPointY0.select("text.y2").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(Y0MinMax[1]) + ")").text("Max : " + dataExpression.getFormatTrafficData(Y0MinMax[1]) + " " + locale.dashboard.count);
                    }
                    //유해트래픽 Min값
                    if (d.EventBps == Y1MinMax[0]) {
                        var minPointY1 = main.append("g").attr("class", "focus").style("display", "");
                        minPointY1.append("circle").attr("class", "maxY1").attr("r", 4);
                        minPointY1.append("text").attr("class", "y1").attr("dy", "-1").attr("dx", "0.5em");
                        minPointY1.select("circle.maxY1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[0]) + ")");
                        minPointY1.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[0]) + ")").text("Min : " + dataExpression.getFormatTrafficData(Y1MinMax[0]));
                    }
                    //유해트래픽 Max값
                    if (d.EventBps == Y1MinMax[1]) {
                        var maxPointY1 = main.append("g").attr("class", "focus").style("display", "");
                        maxPointY1.append("circle").attr("class", "maxY1").attr("r", 4);
                        maxPointY1.append("text").attr("class", "y1").attr("dy", "-1").attr("dx", "0.5em");
                        maxPointY1.select("circle.maxY1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[1]) + ")");
                        maxPointY1.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(Y1MinMax[1]) + ")").text("Max : " + dataExpression.getFormatTrafficData(Y1MinMax[1]));
                    }
                });

                var focus = main.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("line")
                        .attr("class", "y2")
                        .attr("x1", main_width) // nach links
                        .attr("x2", main_width); // nach rechts

                focus.append("line")
                        .attr("class", "y1")
                        .attr("x1", main_width)
                        .attr("x2", main_width);

                focus.append("circle")
                        .attr("class", "y2")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y2")
                        .attr("dy", "-1em");

                focus.append("circle")
                        .attr("class", "y1")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y1")
                        .attr("dy", "-1em");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", main_width)
                        .attr("height", main_height)
                        .on("mouseover", function () {
                            focus.style("display", null);
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                        })
                        .on("mousemove", mousemove);

                function mousemove() {
                    var x0 = main_x.invert(d3.mouse(this)[0]),
                            i = bisectDate(data, x0, 1),
                            d0 = data[i - 1],
                            d1 = data[i],
                            d = x0 - d0.Time > d1.Time - x0 ? d1 : d0;

                    focus.select("circle.y2").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(d.EventCount) + ")");
                    focus.select("text.y2").attr("transform", "translate(" + main_x(d.Time) + "," + main_y0(d.EventCount - 5) + ")").text(formatOutput0(d));
                    focus.select("circle.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(d.EventBps) + ")");
                    focus.select("text.y1").attr("transform", "translate(" + main_x(d.Time) + "," + main_y1(d.EventBps) + ")").text(formatOutput1(d));
                    focus.select(".y2").attr("transform", "translate(" + main_width * -1 + ", " + main_y0(d.EventCount) + ")").attr("x2", main_width + main_x(d.Time));
                    focus.select(".y1").attr("transform", "translate(0, " + main_y1(d.EventBps) + ")").attr("x1", main_x(d.Time));
                }

                // function for the x grid lines
                function make_x_axis() {
                    return d3.svg.axis()
                            .scale(main_x)
                            .orient("bottom")
                            .ticks(13)
                }

                // function for the y grid lines
                function make_y_axis() {
                    return d3.svg.axis()
                            .scale(main_y0)
                            .orient("left")
                            .ticks(3)
                }
            });

            var outputChart = this.template({
                type: this.type,
                title: this.title,
                chartName: this.chartName,
                chart: this.svg,
                locale: locale
            });

            this.$el.html(outputChart);
            return this;
        },
        bubble: function () {
            var svg;
            var chartDivName = this.chartName;
            var collection = this.collection;
            var data;
            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;
            var eventCountList;

            require(['d3'], function (d3) {
                var $chartDiv = $('#' + chartDivName + '_chart');
                $chartDiv.empty();
                var divWid = $chartDiv.width(),
                        divHei = $chartDiv.height();

                //var fill = d3.scale.category20c();
                var fill = d3.scale.category20b();

                var main_margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                main_width = divWid - main_margin.left - main_margin.right,
                        main_height = divHei - main_margin.top - main_margin.bottom;

                var formatData = d3.format("d"),
                        bisectDate = d3.bisector(function (d) {
                            return d.x;
                        }).left;

                var main_x = d3.scale.linear()
                        .range([0, main_width]);

                var main_y = d3.scale.linear()
                        .range([main_height, 0]);

                var main_xAxis = d3.svg.axis()
                        .scale(main_x)
                        .tickFormat(dataExpression.bytesToString)
                        .orient("bottom")
                        .ticks(10)
                        .tickSubdivide(12);

                var main_yAxis = d3.svg.axis()
                        .scale(main_y)
                        .ticks(5)
                        .orient("left")
                        .tickFormat(dataExpression.bytesToString);

                svg = d3.select("#" + chartDivName + "_chart")
                        .attr("width", divWid)
                        .attr("height", divHei)
                        .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                        .attr('preserveAspectRatio', 'xMinYMin');

                var tooltip = d3.select("body")
                        .append("div")
                        .style("position", "absolute")
                        .style("z-index", "10")
                        .style("visibility", "hidden")
                        .style("display", "none")
                        .style("color", "white")
                        .style("padding", "8px")
                        .style("background-color", "rgba(0, 0, 0, 0.75)")
                        .style("border-radius", "6px")
                        .style("font", "12px sans-serif")
                        .text("tooltip");

                var main = svg.append("g")
                        .attr("transform", "translate(" + main_margin.left + "," + main_margin.top + ")");

                //데이터조작
                data = collection;

                main_x.domain([
                    //0,
                    -20000000,
                    formatData(d3.max(data, function (d) {
                        return (d.x);
                    })) * 1.2
                ]);

                main_y.domain([
                    //0,
                    -180000,
                    formatData(d3.max(data, function (d) {
                        return (d.y);
                    })) * 1.55
                ]);

                // Draw the y Grid lines
                main.append("g")
                        .attr("class", "grid")
                        .call(make_y_axis()
                                .tickSize(-main_width, 0, 0)
                                .tickFormat("")
                                );

                // Draw the y Grid lines
                main.append("g")
                        .attr("class", "grid")
                        .attr("transform", "translate(0," + main_height + ")")
                        .call(make_x_axis()
                                .tickSize(-main_height, 0, 0)
                                .tickFormat("")
                                );

                var focus = main.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("line")
                        .attr("class", "y4")
                        .attr("x1", main_width)
                        .attr("x2", main_width);

                focus.append("line")
                        .attr("class", "y5")
                        .attr("y1", main_height); // nach links


                main.selectAll("circle")
                        .data(data)
                        .enter()
                        .append("circle")
                        .attr("cx", function (d) {
                            return main_x(d.x)
                        })
                        .attr("cy", function (d) {
                            return main_y(d.y)
                        })
                        .attr("r", function (d) {
                            return d.r
                        })
                        .style("fill", function (d) {
                            return fill(d.strName);
                        })
                        .on("mouseover", function (d) {
                            tooltip.html(d.strName + "<br>&nbsp;&nbsp;&nbsp;<font color=#B0B0B0>" + locale.dashboard.total + " : " + dataExpression.getFormatTrafficData(d.x) + "</font><br>&nbsp;&nbsp;&nbsp;<font color=#FC5F29>" + locale.dashboard.malicious + " : " + dataExpression.getFormatTrafficData(d.y) + "</font><br>&nbsp;&nbsp;&nbsp;<font color=#d065e9>" + locale.dashboard.event + " : " + dataExpression.getFormatTrafficData(d.size) + " " + locale.dashboard.count + " </font>");
                            tooltip.style("visibility", "visible");
                            tooltip.style("display", "block");
                            focus.style("display", "block");
                        })
                        .on("mousemove", function (d) {
                            tooltip.style("top", (d3.event.pageY - 10) + "px").style("left", (d3.event.pageX + 10) + "px");
                            focus.select(".y4")
                                    .attr("transform", "translate(" + main_width * -1 + ", " + main_y(d.y) + ")")
                                    .attr("x1", main_width + main_x(d.x));
                            focus.select(".y5")
                                    .attr("transform", "translate(" + main_x(d.x) + " , 0")
                                    .attr("x1", main_x(d.x))
                                    .attr("y1", main_y(d.y))
                                    .attr("x2", main_x(d.x))
                                    .attr("y2", main_height);
                        })
                        .on("mouseout", function () {
                            tooltip.style("visibility", "hidden");
                            tooltip.style("display", "none");
                            focus.style("display", "none");
                        });

                main.selectAll("title")
                        .data(data)
                        .enter()
                        .append("text")
                        .text(function (d) {
                            if (d.x > 0 && d.y > 0) {
                                return d.strName;
                            } else {
                                return "";
                            }
                        })
                        .attr("x", function (d) {
                            return main_x(d.x);
                        })
                        .attr("y", function (d) {
                            return main_y(d.y);
                        })
                        .style("fill", "navy")
                        .style("text-anchor", "middle")
                        .style("font-family", "굴림")
                        .style("font-size", "11px");


                // function for the x grid lines
                function make_x_axis() {
                    return d3.svg.axis()
                            .scale(main_x)
                            .orient("bottom")
                            .ticks(10)
                }

                // function for the y grid lines
                function make_y_axis() {
                    return d3.svg.axis()
                            .scale(main_y)
                            .orient("left")
                            .ticks(3)
                }

                //왼쪽 X축
                main.append("g")
                        .attr("class", "x axis axixBubble")
                        .attr("transform", "translate(0," + main_height + ")")
                        .call(main_xAxis)
                        .append("text")
                        .attr("x", main_width + 20)
                        .attr("y", -3)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("bps");

                //왼쪽 Y축
                main.append("g")
                        .attr("class", "y axis axixBubble")
                        .call(main_yAxis)
                        .append("text")
                        .attr("x", 5)
                        .attr("y", -15)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("bps");
            });

            //공격횟수 순위 센서 명
            eventCountList = "";

            if (collection.length > 0) {
                for (var i = 0; i < collection.length; i++) {
                    eventCountList += "<tr><td class='align-center'>" + (i + 1) + "</td><td class='align-left'>" + collection[i].strName + "</td></tr>";
                }

                if (collection.length < 5) {
                    for (var i = 0; i < 5 - collection.length; i++) {
                        eventCountList += "<tr><td class='align-center'>&nbsp;</td><td class='align-left'></td></tr>";
                    }
                }
            } else {
                for (var i = 0; i < 5; i++) {
                    eventCountList += "<tr><td class='align-center'>&nbsp;</td><td class='align-left'></td></tr>";
                }
            }

            var outputChart = this.templateWithList({
                type: this.type,
                title: this.title,
                chartName: this.chartName,
                chart: this.svg,
                eventCountList: eventCountList,
                locale: locale
            });

            this.$el.html(outputChart);
            return this;
        },
        // 그래프 팝업 차트  
        multiLine: function () {
            var svg;
            var chartDivName = this.chartName;
            var data = this.collection;
            var thisView = this;
            var parentElName;
            var dataType = this.dataType;
            var chartHeight = this.chartHeight;

            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;
            var svgMiniMarginTop = this.svgMiniMarginTop;
            require(['d3'], function (d3) {
                if (thisView.parentEl) {
                    var $chartDiv = $(thisView.parentEl).closest('.tab-pane').find('.' + chartDivName + '_chart');
                    var $selChartDiv = $('.tab-content').find('.active');
                    thisView.parentElName = $chartDiv.closest('.tab-pane').prop('id');
                } else {
                    var $selChartDiv = $('#' + chartDivName + '_chart');
                    var $chartDiv = $('#' + chartDivName + '_chart');
                }
                $chartDiv.empty();

                var divWid = $selChartDiv.width(),
                        divHei = $chartDiv.height();

                var margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                mini_margin = {
                    top: svgMiniMarginTop,
                    right: svgMainMarginRight,
                    left: svgMainMarginLeft
                },
                width = divWid - margin.left - margin.right,
                        height = divHei - margin.top - margin.bottom,
                        mini_height = 15;

                var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                        parseDate = formatDate.parse,
                        bisectDate = d3.bisector(function (d) {
                            return d.time;
                        }).left,
                        formatOutput = function (d) {
                            if (thisView.dataType == "bps" || thisView.dataType == "pps") {
                                return dataExpression.getFormatTrafficData(d.value);
                            } else if (thisView.dataType == "count") {
                                return dataExpression.getFormatTrafficData(d.value);
                            } else {
                                return d.value;
                            }
                        };

                var x = d3.time.scale()
                        .range([0, width]);

                var mini_x = d3.time.scale()
                        .range([0, width]);

                var y = d3.scale.linear()
                        .range([height, 0]);

                var mini_y = d3.scale.linear()
                        .range([mini_height, 0]);

                var xAxis = d3.svg.axis()
                        .scale(x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var mini_xAxis = d3.svg.axis()
                        .scale(mini_x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var yAxis = d3.svg.axis()
                        .scale(y)
                        .orient("left");
                // 공격횟수도 M, K, G 로 표현하기 위함.
//				if(thisView.dataType == "bps" || thisView.dataType == "pps"){
                var yAxis = d3.svg.axis()
                        .scale(y)
                        .orient("left")
                        .tickFormat(dataExpression.bytesToString);
//				}

                var mini_yAxis = d3.svg.axis()
                        .scale(mini_y)
                        .tickFormat("")
                        .orient("left");

                var brush = d3.svg.brush()
                        .x(mini_x)
                        .on("brushend", brushed);

                var area = d3.svg.area()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x(d.time);
                        })
                        .y(function (d) {
                            return y(d.value);
                        });

                var line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x(d.time);
                        })
                        .y(function (d) {
                            return y(d.value);
                        });

                var mini_line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return mini_x(d.time);
                        })
                        .y(function (d) {
                            return mini_y(d.value);
                        });
                if (thisView.parentEl) {
                    svg = d3.select("#" + thisView.parentElName + " #" + chartDivName + "_chart")
                            .attr("width", divWid)
                            .attr("height", divHei)
                            .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                            .attr('preserveAspectRatio', 'xMinYMin');
                } else {
                    svg = d3.select("#" + chartDivName + "_chart")
                            .attr("width", divWid)
                            .attr("height", divHei)
                            .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                            .attr('preserveAspectRatio', 'xMinYMin');
                }

                // dashboard에서 protocol chart가 팝업 chart width에 영향 주는 것을 방지                
                if ($("#protocolChart").length == "1")
                    $("#clip > rect").attr("width", "600");

                /*
                 
                 if (thisView.parentEl) {
                 var $chartDiv = $(thisView.parentEl).closest('.tab-pane').find('.' + chartDivName + '_chart');
                 var $selChartDiv = $('.tab-content').find('.active');
                 thisView.parentElName = $chartDiv.closest('.tab-pane').prop('id');
                 } else {
                 var $selChartDiv = $('#' + chartDivName + '_chart');
                 var $chartDiv = $('#' + chartDivName + '_chart');
                 */

                svg.append("defs").append("clipPath")
                        .attr("id", "clip")
                        .append("rect")
                        .attr("width", width)
                        .attr("height", height);

                var main = svg.append("g")
                        .attr("class", "main")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
                        .attr("width", width)
                        .attr("height", height);


                var mini = svg.append("g")
                        .attr("class", "mini")
                        .attr("transform", "translate(" + mini_margin.left + "," + mini_margin.top + ")");

//				var legend = svg.append("g")
//					.attr("class", "legend")
//					.attr("transform", "translate(" + margin.left / 2 + " , " + margin.top / 2 + ")");

                data.forEach(function (kv) {
                    var labelName = kv.key;
                    kv.values.forEach(function (d) {
                        d.value = +d.value;
                        d.label = labelName;
                    });
                });

                x.domain([
                    d3.min(data, function (kv) {
                        return d3.min(kv.values, function (d) {
                            return d.time;
                        })
                    }),
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.time;
                        })
                    })
                ]);
                y.domain([
                    0,
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.value;
                        })
                    }) * 1.2
                ]);

                mini_x.domain(x.domain());
                mini_y.domain(y.domain());

                //brush.extent(x.domain());
//                main.append("g")
//                        .attr("class", "x axis")
//                        .attr("transform", "translate(0," + height + ")")
//                        .call(xAxis);

                main.append("g")
                        .attr("class", "x axis rotate")
                        .attr("transform", "translate(0," + height + ")")
                        .call(xAxis)
                        .selectAll('text')
                        .style('text-anchor', 'end')
                        .attr('dx', '-.8em')
                        .attr('dy', '.15em')
                        .attr("transform", "rotate(-30)");

                main.append("g")
                        .attr("class", "y axis")
                        .call(yAxis);
                //				    .append("text")
                //				      .attr("transform", "rotate(-90)")
                //				      .attr("y", 6)
                //				      .attr("dy", ".71em")
                //				      .style("text-anchor", "end")
                //				      .text("Traffic (bps)");

                mini.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(mini_xAxis)
                        .selectAll('text')
                        .style('text-anchor', 'end')
                        .attr('dx', '-.8em')
                        .attr('dy', '.15em')
                        .attr("transform", "rotate(-30)");

                //                mini.append("g")
                //                    .attr("class", "y axis")
                //                    .call(mini_yAxis);

                var multiDataGroup = main.selectAll(".multiDataGroup")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "multiDataGroup");


                multiDataGroup.append("path")
                        .attr("class", "line")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d) {
                            return line(d.values);
                        })
                        .style("stroke", function (d) {
                            return d.color;
                        });

                multiDataGroup.append("text")
                        .datum(function (d) {
                            return {
                                key: d.key,
                                value: d.values[d.values.length - 1]
                            };
                        })
                        .attr("class", "label")
                        .attr("transform", function (d) {
                            return "translate(" + x(d.value.time) + "," + y(d.value.value) + ")";
                        })
                        .attr("x", 3)
                        .attr("dy", ".35em")
                        .text(function (d) {
                            return d.key;
                        });

                // Draw the x Grid lines
                //					main.append("g")
                //			        	.attr("class", "grid")
                //			        	.attr("transform", "translate(0," + height + ")")
                //			        	.call(make_x_axis()
                //			        			.tickSize(-height, 0, 0)
                //			        			.tickFormat("")
                //			        	)

                //                // Draw the y Grid lines
                //                main.append("g")
                //                    .attr("class", "grid")
                //                    .call(make_y_axis()
                //                        .tickSize(-width, 0, 0)
                //                        .tickFormat("")
                //                    )

                var mini_multiDataGroup = mini.selectAll(".multiDataGroup")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "multiDataGroup");

                mini_multiDataGroup.append("path")
                        .attr("class", "line")
                        .attr("d", function (d) {
                            return mini_line(d.values);
                        })
                        .style("stroke", function (d) {
                            return d.color;
                        });

                mini_multiDataGroup.append("g")
                        .attr("class", "grid")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(make_mini_x_axis()
                                .tickSize(-mini_height, 0, 0)
                                .tickFormat("")
                                );

                //미니사이즈에 범위 선택 드래그
                mini_multiDataGroup.append("g")
                        .attr("class", "brush")
                        .call(brush)
                        .selectAll("rect")
                        .attr("y", -6)
                        .attr("height", mini_height + 7);

                var focus = multiDataGroup.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("circle")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y0")
                        .attr("dy", "-1em");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", width)
                        .attr("height", height)
                        .on("mouseover", function () {
                            focus.style("display", null);
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                        })
                        .on("mousemove", mousemove);

                function mousemove() {
                    var x0 = x.invert(d3.mouse(this)[0]),
                            i = bisectDate(data[0].values, x0);
                    focus.select("circle")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i],
                                    color: d.color
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x(d.value.time) + "," + y(d.value.value) + ")";
                            })
                            .attr("stroke", function (d) {
                                return d.color
                            })
                    focus.select("text")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i]
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x(d.value.time) + "," + y(d.value.value) + ")";
                            })
                            .text(function (d) {
                                return formatOutput(d.value)
                            });
                }

                function mouseover(protocolName) {
                    d3.selectAll('path.line')
                            .classed("highlight", function (d) {
                                return d.key === protocolName.key;
                            })
                            .classed("fadeout", function (d) {
                                return d.key !== protocolName.key;
                            });
                }

                function mouseout(protocolName) {
                    d3.selectAll('path.line')
                            .classed("highlight", false)
                            .classed("fadeout", false);
                }

                function brushed() {
                    if (!d3.event.sourceEvent)
                        return; // only transition after input
                    var extent0 = brush.extent();
                    var extent1 = extent0.map(d3.time.minute);

                    extent1[0] = my5MinRound(extent0[0]);
                    extent1[1] = my5MinRound(extent0[1]);

                    if (brush.empty()) {
                        x.domain(mini_x.domain());
                    } else {
                        x.domain(extent1);
                        //brush.extent(extent1);
                    }

                    main.selectAll(".multiDataGroup")
                            .select("path")
                            .attr("d", function (d) {
                                return line(d.values)
                            });

                    main.selectAll(".multiDataGroup")
                            .selectAll(".label")
                            .attr("transform", function (d) {
                                return "translate(" + width + ", " + y(d.value.value) + ")";
                            });

                    main.select(".x.axis").call(xAxis);

                    mini.selectAll(".brush")
                            .transition()
                            .call(brush.extent(extent1))
                            .call(brush.event);
                }

                function my5MinRound(date) {
                    var subHalf = d3.time.minute.offset(date, -2.5);
                    var addHalf = d3.time.minute.offset(date, 2.5);
                    return d3.time.minutes(subHalf, addHalf, 5)[0];
                }

                // function for the x grid lines
                function make_x_axis() {
                    return d3.svg.axis()
                            .scale(x)
                            .orient("bottom")
                }

                // function for the y grid lines
                function make_y_axis() {
                    return d3.svg.axis()
                            .scale(y)
                            .orient("left")
                            .ticks(4)
                }

                function make_mini_x_axis() {
                    return d3.svg.axis()
                            .scale(mini_x)
                            .orient("bottom")
                }
            });

            var outputChart = this.templateAnalysis({
                chartName: this.chartName,
                chart: this.svg,
                chartHeight: chartHeight
            });

            this.$el.html(outputChart);
            return this;
        },
        multiDualLine: function () {
            var svg;
            var chartDivName = this.chartName;
            var data = this.collection;
            var thisView = this;
            var parentElName;
            var dataType = this.dataType;
            var chartHeight = this.chartHeight;

            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;
            var svgMiniMarginTop = this.svgMiniMarginTop;

            require(['d3'], function (d3) {
                if (thisView.parentEl) {
                    var $chartDiv = $(thisView.parentEl).closest('.tab-pane').find('.' + chartDivName + '_chart');
                    var $selChartDiv = $('.tab-content').find('.active');
                    thisView.parentElName = $chartDiv.closest('.tab-pane').prop('id');
                } else {
                    var $selChartDiv = $('#' + chartDivName + '_chart');
                    var $chartDiv = $('#' + chartDivName + '_chart');
                }
                $chartDiv.empty();

                var divWid = $selChartDiv.width(),
                        divHei = $chartDiv.height();
                var margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                mini_margin = {
                    top: svgMiniMarginTop,
                    right: svgMainMarginRight,
                    left: svgMainMarginLeft
                },
                width = divWid - margin.left - margin.right,
                        height = divHei - margin.top - margin.bottom,
                        mini_height = 15;
                /////////////////////
                var margin2 = {
                    top: svgMainMarginTop + height,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                mini_margin2 = {
                    top: svgMainMarginTop + 100 + height,
                    right: svgMainMarginRight,
                    left: svgMainMarginLeft
                },
                ////////////////
                width2 = divWid - margin2.left - margin2.right,
                        height2 = divHei - margin.top - margin.bottom;

                var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                        parseDate = formatDate.parse,
                        bisectDate = d3.bisector(function (d) {
                            return d.time;
                        }).left,
                        formatOutput = function (d) {
                            return dataExpression.getFormatTrafficData(d.valueOut);
                        },
                        formatOutput2 = function (d) {
                            return dataExpression.getFormatTrafficData(d.valueIn);
                        };

                var x = d3.time.scale()
                        .range([0, width]);

                var mini_x = d3.time.scale()
                        .range([0, width]);

                var x2 = d3.time.scale()
                        .range([0, width2]);

                var y = d3.scale.linear()
                        .range([height, 0]);

                var mini_y = d3.scale.linear()
                        .range([mini_height, 0]);

                var y2 = d3.scale.linear()
                        .range([height2, 0]);

                var xAxis = d3.svg.axis()
                        .scale(x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var xAxis2 = d3.svg.axis()
                        .scale(x2)
                        .tickFormat("")
                        .orient("top");

                var mini_xAxis = d3.svg.axis()
                        .scale(mini_x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var yAxis = d3.svg.axis()
                        .scale(y)
                        .orient("left");

                var yAxis2 = d3.svg.axis()
                        .scale(y2)
                        .orient("left");

                if (thisView.dataType == "bps" || thisView.dataType == "pps") {
                    var yAxis = d3.svg.axis()
                            .scale(y)
                            .orient("left")
                            .tickFormat(dataExpression.bytesToString);

                    var yAxis2 = d3.svg.axis()
                            .scale(y2)
                            .orient("left")
                            .tickFormat(dataExpression.bytesToString);
                }

                var mini_yAxis = d3.svg.axis()
                        .scale(mini_y)
                        .tickFormat("")
                        .orient("left");

                var brush = d3.svg.brush()
                        .x(mini_x)
                        .on("brushend", brushed);

                var area = d3.svg.area()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x(d.time);
                        })
                        .y(function (d) {
                            return y(d.valueOut);
                        });

                var area2 = d3.svg.area()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x(d.time);
                        })
                        .y(function (d) {
                            return y(d.valueIn);
                        });

                var line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x(d.time);
                        })
                        .y(function (d) {
                            return y(d.valueOut);
                        });

                var line2 = d3.svg.line()
                        // .interpolate("cardinal")
                        .x(function (d) {
                            return x2(d.time);
                        })
                        .y(function (d) {
                            return y2(d.valueIn);
                        });

                var mini_line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return mini_x(d.time);
                        })
                        .y(function (d) {
                            return mini_y(d.value);
                        });

                if (thisView.parentEl) {
                    svg = d3.select("#" + thisView.parentElName + " #" + chartDivName + "_chart")
                            .attr("width", divWid)
                            .attr("height", divHei)
                            .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                            .attr('preserveAspectRatio', 'xMinYMin');
                } else {
                    svg = d3.select("#" + chartDivName + "_chart")
                            .attr("width", divWid)
                            .attr("height", divHei)
                            .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                            .attr('preserveAspectRatio', 'xMinYMin');
                }

                svg.append("defs").append("clipPath")
                        .attr("id", "clip")
                        .append("rect")
                        .attr("width", width)
                        .attr("height", height);

                var main = svg.append("g")
                        .attr("class", "main")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                var main2 = svg.append("g")
                        .attr("class", "main")
                        .attr("transform", "translate(" + margin2.left + "," + margin2.top + ")");

                var mini = svg.append("g")
                        .attr("class", "mini")
                        .attr("transform", "translate(" + mini_margin.left + "," + mini_margin.top + ")");

                var legend = svg.append("g")
                        .attr("class", "legend")
                        .attr("transform", "translate(" + margin.left / 2 + " , " + margin.top / 2 + ")");

                data.forEach(function (kv) {
                    var labelName = kv.key;
                    kv.values.forEach(function (d) {
                        d.value = +d.value;
                        d.valueIn = +d.valueIn;
                        d.valueOut = +d.valueOut;
                        d.label = labelName;
                    });
                });

                x.domain([
                    d3.min(data, function (kv) {
                        return d3.min(kv.values, function (d) {
                            return d.time;
                        })
                    }),
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.time;
                        })
                    })
                ]);

                x2.domain([
                    d3.min(data, function (kv) {
                        return d3.min(kv.values, function (d) {
                            return d.time;
                        })
                    }),
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.time;
                        })
                    })
                ]);

                y.domain([
                    0,
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.valueOut;
                        })
                    })
                ]);

                y2.domain([
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.valueIn;
                        })
                    }),
                    0
                ]);


                mini_x.domain(x.domain());
                mini_y.domain([
                    0,
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.value;
                        })
                    })
                ]);
                //mini_y.domain(y.domain());

                //brush.extent(x.domain());
                main.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + height + ")")
                        .call(xAxis);

                main.append("g")
                        .attr("class", "y axis")
                        //.call(yAxis)
                        .call(yAxis)
                        .append("text")
                        .attr("x", width)
                        .attr("y", "-10")
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("Outbound");

                main2.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0, 0)")
                        .call(xAxis2);

                main2.append("g")
                        .attr("class", "y axis")
                        .call(yAxis2)
                        .append("text")
                        .attr("x", width2)
                        .attr("y", height2)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("Inbound");

                mini.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(mini_xAxis);

                var multiDataGroup = main.selectAll(".multiDataGroup")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "multiDataGroup");

                //                multiDataGroup.append("path")
                //	                .attr("class", "area1")
                //                    .attr("clip-path", "url(#clip)")
                //                    .attr("d", function(d) {
                //                        return area(d.values);
                //                    })
                //                    .style("fill", function(d) {
                //                        return d.color;
                //                    });

                multiDataGroup.append("path")
                        .attr("class", "line")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d) {
                            return line(d.values);
                        })
                        .style("stroke", function (d) {
                            return d.color;
                        });

                multiDataGroup.append("text")
                        .datum(function (d) {
                            return {
                                key: d.key,
                                value: d.values[d.values.length - 1]
                            };
                        })
                        .attr("class", "label")
                        .attr("transform", function (d) {
                            return "translate(" + x(d.value.time) + "," + y(d.value.valueOut) + ")";
                        })
                        .attr("x", 3)
                        .attr("dy", ".35em")
                        .text(function (d) {
                            return d.key;
                        });

                var multiDataGroup2 = main2.selectAll(".multiDataGroup2")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "multiDataGroup2");

                multiDataGroup2.append("path")
                        .attr("class", "line")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d) {
                            return line2(d.values);
                        })
                        .style("stroke", function (d) {
                            return d.color;
                        });

                multiDataGroup2.append("text")
                        .datum(function (d) {
                            return {
                                key: d.key,
                                value: d.values[d.values.length - 1]
                            };
                        })
                        .attr("class", "label")
                        .attr("transform", function (d) {
                            return "translate(" + x(d.value.time) + "," + y(d.value.valueIn) + ")";
                        })
                        .attr("x", 3)
                        .attr("dy", ".35em")
                        .text(function (d) {
                            return d.key;
                        });

                // Draw the x Grid lines
                //					main.append("g")
                //			        	.attr("class", "grid")
                //			        	.attr("transform", "translate(0," + height + ")")
                //			        	.call(make_x_axis()
                //			        			.tickSize(-height, 0, 0)
                //			        			.tickFormat("")
                //			        	)

                //                // Draw the y Grid lines
                //                main.append("g")
                //                    .attr("class", "grid")
                //                    .call(make_y_axis()
                //                        .tickSize(-width, 0, 0)
                //                        .tickFormat("")
                //                    )

                var mini_multiDataGroup = mini.selectAll(".multiDataGroup")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "multiDataGroup");

                mini_multiDataGroup.append("path")
                        .attr("class", "line")
                        .attr("d", function (d) {
                            return mini_line(d.values);
                        })
                        .style("stroke", function (d) {
                            return d.color;
                        });

                mini_multiDataGroup.append("g")
                        .attr("class", "grid")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(make_mini_x_axis()
                                .tickSize(-mini_height, 0, 0)
                                .tickFormat("")
                                );

                //미니사이즈에 범위 선택 드래그
                mini_multiDataGroup.append("g")
                        .attr("class", "brush")
                        .call(brush)
                        .selectAll("rect")
                        .attr("y", -6)
                        .attr("height", mini_height + 7);

                var focus = multiDataGroup.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("circle")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y0")
                        .attr("dy", "-1em");

                var focus2 = multiDataGroup2.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus2.append("circle")
                        .attr("r", 4);

                focus2.append("text")
                        .attr("class", "y0")
                        .attr("dy", "-1em");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", width)
                        .attr("height", height)
                        .on("mouseover", function () {
                            focus.style("display", null);
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                        })
                        .on("mousemove", mousemove);

                main2.append("rect")
                        .attr("class", "overlay")
                        .attr("width", width2)
                        .attr("height", height2)
                        .on("mouseover", function () {
                            focus2.style("display", null);
                        })
                        .on("mouseout", function () {
                            focus2.style("display", "none");
                        })
                        .on("mousemove", mousemove2);

                function mousemove() {
                    var x0 = x.invert(d3.mouse(this)[0]),
                            i = bisectDate(data[0].values, x0);
                    focus.select("circle")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i],
                                    color: d.color
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x(d.value.time) + "," + y(d.value.valueOut) + ")";
                            })
                            .attr("stroke", function (d) {
                                return d.color
                            })
                    focus.select("text")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i]
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x(d.value.time) + "," + y(d.value.valueOut) + ")";
                            })
                            .text(function (d) {
                                return formatOutput(d.value)
                            });
                }

                function mousemove2() {
                    var x0 = x.invert(d3.mouse(this)[0]),
                            i = bisectDate(data[0].values, x0);
                    focus2.select("circle")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i],
                                    color: d.color
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x2(d.value.time) + "," + y2(d.value.valueIn) + ")";
                            })
                            .attr("stroke", function (d) {
                                return d.color;
                            })
                    focus2.select("text")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i]
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x2(d.value.time) + "," + y2(d.value.valueIn) + ")";
                            })
                            .text(function (d) {
                                return formatOutput2(d.value)
                            });
                }

                function brushed() {
                    if (!d3.event.sourceEvent)
                        return; // only transition after input
                    var extent0 = brush.extent();
                    var extent1 = extent0.map(d3.time.minute);

                    extent1[0] = my5MinRound(extent0[0]);
                    extent1[1] = my5MinRound(extent0[1]);

                    if (brush.empty()) {
                        x.domain(mini_x.domain());
                        x2.domain(mini_x.domain());
                    } else {
                        x.domain(extent1);
                        x2.domain(extent1);
                        //brush.extent(extent1);
                    }

                    main.selectAll(".multiDataGroup")
                            .select("path")
                            .attr("d", function (d) {
                                return line(d.values)
                            });

                    main2.selectAll(".multiDataGroup2")
                            .select("path")
                            .attr("d", function (d) {
                                return line2(d.values)
                            });

                    main.selectAll(".multiDataGroup")
                            .selectAll(".label")
                            .attr("transform", function (d) {
                                return "translate(" + width + ", " + y(d.value.valueOut) + ")";
                            });

                    main2.selectAll(".multiDataGroup2")
                            .selectAll(".label")
                            .attr("transform", function (d) {
                                return "translate(" + width2 + ", " + y(d.value.valueIn) + ")";
                            });

                    main.select(".x.axis").call(xAxis);
                    main2.select(".x.axis").call(xAxis2);

                    mini.selectAll(".brush")
                            .transition()
                            .call(brush.extent(extent1))
                            .call(brush.event);
                }

                function my5MinRound(date) {
                    var subHalf = d3.time.minute.offset(date, -2.5);
                    var addHalf = d3.time.minute.offset(date, 2.5);
                    return d3.time.minutes(subHalf, addHalf, 5)[0];
                }

                // function for the x grid lines
                function make_x_axis() {
                    return d3.svg.axis()
                            .scale(x)
                            .orient("bottom")
                }

                // function for the y grid lines
                function make_y_axis() {
                    return d3.svg.axis()
                            .scale(y)
                            .orient("left")
                            .ticks(4)
                }

                function make_mini_x_axis() {
                    return d3.svg.axis()
                            .scale(mini_x)
                            .orient("bottom")
                }
            });

            var outputChart = this.templateAnalysis({
                chartName: this.chartName,
                chart: this.svg,
                chartHeight: chartHeight
            });

            this.$el.html(outputChart);
            return this;
        },
        twoLine: function () {
            var svg;
            var chartDivName = this.chartName;
            var data = this.collection;
            var thisView = this;
            var chartHeight = 80;

            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;

            require(['d3'], function (d3) {
                var $selChartDiv = $('#' + chartDivName);
                var $chartDiv = $('#' + chartDivName);

                thisView.$("#profileWeekChart").empty();

                var divWid = $selChartDiv.width(),
                        divHei = $chartDiv.height();

                var margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                width = divWid - margin.left - margin.right,
                        height = divHei - margin.top - margin.bottom;

                var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                        parseDate = formatDate.parse,
                        bisectDate = d3.bisector(function (d) {
                            return d.time;
                        }).left,
                        formatOutput = function (d) {
                            return d.value;
                        };

                var x = d3.time.scale()
                        .range([0, width]);

                var y = d3.scale.linear()
                        .range([height, 0]);

                var xAxis = d3.svg.axis()
                        .scale(x)
                        .tickFormat("")
                        .orient("bottom");

                var yAxis = d3.svg.axis()
                        .scale(y)
                        .tickFormat("")
                        .orient("left");

                var area = d3.svg.area()
                        .x(function (d) {
                            return x(d.time);
                        })
                        .y(function (d) {
                            return y(d.value);
                        });

                var line = d3.svg.line()
                        .x(function (d) {
                            return x(d.time);
                        })
                        .y(function (d) {
                            return y(d.value);
                        });

                svg = d3.select("#" + chartDivName)
                        .attr("width", divWid)
                        .attr("height", divHei)
                        .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                        .attr('preserveAspectRatio', 'xMinYMin');
                //	.attr("class", "graph-svg-component");

                svg.append("defs").append("clipPath")
                        .attr("id", "clip")
                        .append("rect")
                        .attr("width", width)
                        .attr("height", height);

                var main = svg.append("g")
                        .attr("class", "main")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                data.forEach(function (kv) {
                    var labelName = kv.key;
                    kv.values.forEach(function (d) {
                        d.value = +d.value;
                    });
                });

                x.domain([
                    d3.min(data, function (kv) {
                        return d3.min(kv.values, function (d) {
                            return d.time;
                        })
                    }),
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.time;
                        })
                    })
                ]);
                y.domain([
                    0,
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.value;
                        })
                    }) * 1.2
                ]);

//				main.append("g")
//					.attr("class", "x axis")
//					.attr("transform", "translate(0," + height + ")")
//					.call(xAxis);

                main.append("g")
                        .attr("class", "y axis")
                        .call(yAxis);

                var multiDataGroup = main.selectAll(".multiDataGroup")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "multiDataGroup");

                multiDataGroup.append("path")
                        .attr("class", "line")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d) {
                            return line(d.values);
                        })
                        .style("stroke", function (d) {
                            return d.color;
                        });

                multiDataGroup.append("text")
                        .datum(function (d) {
                            return {
                                key: d.key,
                                value: d.values[d.values.length - 1]
                            };
                        })
                        .attr("class", "label")
                        .attr("transform", function (d) {
                            return "translate(" + x(d.value.time) + "," + y(d.value.value) + ")";
                        })
                        .attr("x", 3)
                        .attr("dy", ".35em")
                        .text(function (d) {
                            return d.key;
                        });

                var focus = multiDataGroup.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("circle")
                        .attr("r", 4);

                focus.append("text")
                        .attr("class", "y0")
                        .attr("dy", "-1em");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", width)
                        .attr("height", height)
                        .on("mouseover", function () {
                            focus.style("display", null);
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                        })
                        .on("mousemove", mousemove);

                function mousemove() {
                    var x0 = x.invert(d3.mouse(this)[0]),
                            i = bisectDate(data[0].values, x0);
                    focus.select("circle")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i],
                                    color: d.color
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x(d.value.time) + "," + y(d.value.value) + ")";
                            })
                            .attr("stroke", function (d) {
                                return d.color
                            })
                    focus.select("text")
                            .datum(function (d) {
                                return {
                                    key: d.key,
                                    value: d.values[i]
                                };
                            })
                            .attr("transform", function (d) {
                                return "translate(" + x(d.value.time) + "," + y(d.value.value) + ")";
                            })
                            .text(function (d) {
                                return formatOutput(d.value)
                            });
                }

                function mouseover(protocolName) {
                    d3.selectAll('path.line')
                            .classed("highlight", function (d) {
                                return d.key === protocolName.key;
                            })
                            .classed("fadeout", function (d) {
                                return d.key !== protocolName.key;
                            });
                }

                function mouseout(protocolName) {
                    d3.selectAll('path.line')
                            .classed("highlight", false)
                            .classed("fadeout", false);
                }

            });

            var outputChart = this.templateAnalysis({
                chartName: this.chartName,
                chart: this.svg,
                chartHeight: this.chartHeight
            });

            this.$el.html(outputChart);
            return this;
        },
        pieDonut: function () {
//			var svg;
            var chartDivName = this.chartName;
            var data = this.collection;
            var percentValue = this.percentValue;
//
//			var m = 5,
//				r = 65,
//				z = d3.scale.category20c();
//
//			require(['d3'], function(d3) {
//				svg = d3.select("#" + chartDivName)
//					.data(data)
//					.attr("width", (r + m) * 2)
//					.attr("height", (r + m) * 2)
//					.append("svg:g")
//					.attr("transform", "translate(" + (r + m) + "," + (r + m) + ")");
//
//				svg.selectAll("path")
//					.data(d3.layout.pie())
//					.sort(null)
//					.enter().append("svg:path")
//					.attr("d", d3.svg.arc()
//						.innerRadius(r / 3 * 2)
//						.outerRadius(r))
//					.style("fill", function(d, i) {
//						return z(i);
//					});
//             
//				//alert(JSON.stringify(data[0][2]));
//				if(percentValue != undefined){
//					svg.append("text")
//					.attr("dy", ".35em")
//					.style("text-anchor", "middle")
//					.text(percentValue + "%");
//				}
//				
//			});
            require(['d3'], function (d3) {
                var m = 5,
                        r = 65;
                //radius = Math.min(width, height) / 3;

                var color = d3.scale.category20();

                var pie = d3.layout.pie()
                        .sort(null);

                var arc = d3.svg.arc()
                        .innerRadius(r / 3 * 2)
                        .outerRadius(r);

                var svg = d3.select("#" + chartDivName)
                        .data(data)
                        .attr("width", (r + m) * 2)
                        .attr("height", (r + m) * 2)
                        .append("g")
                        .attr("transform", "translate(" + (r + m) + "," + (r + m) + ")");

                var path = svg.selectAll("path")
                        .data(d3.layout.pie())
                        .enter().append("path")
                        .attr("fill", function (d, i) {
                            return color(i);
                        })
                        .attr("d", arc);

                if (percentValue != undefined) {
                    svg.append("text")
                            .attr("dy", ".35em")
                            .style("text-anchor", "middle")
                            .text(percentValue + "%");
                }

            });
            this.$el.html(this.svg);
            return this;
        },
        MultiPieDonut: function () {

            var chartDivName = this.chartName;
            var data, thisView = this;

            require(['d3'], function (d3) {
                var $chartDiv = $('#' + chartDivName);

                $chartDiv.empty();

                var width = $chartDiv.width(),
                        height = $chartDiv.height();

                //var width = 280,
                //height = 260,
                var radius = Math.min(width, height) / 2;

                var color = d3.scale.ordinal()
                        //.range(["#d0743c", "#a05d56", "#6b486b", "#7b6888", "#8a89a6", "#98abc5"]); // color set - old
                        //.range(["#eb6e5e", "#e98d34", "#f6d800", "#bbd84a", "#8cabcc", "#ccc"]);	// color set 1
                        //.range(["#e95446", "#ee993a", "#f3be3f", "#499d86", "#4e9ed7", "#ccc"]);	// color set 2
                        //.range(["#f09286", "#ffc19e", "#ffe08c", "#cce277", "#a9c0d9", "#ccc"]);	// color set 3
                        //.range(["#7cb5ec", "#f45b5b", "#2b908f", "#e4d354", "#f15c80", "#ccc"]);	// color set 4
                        //.range(["#79d1cf", "#95d7bb", "#d9dd81", "#e67a77", "#4d5360"]);	// color set 5
                        //.range(["#e67a77", "#d9dd81", "#95d7bb", "#79d1cf", "#4d5360"]);	// color set 6
                        //.range(["#26b99a", "#8abb6f", "#9b59b6", "#3498db", "#bdc3c7"]);	// color set 7
                        //.range(["#26b99a", "#8abb6f", "#9b59b6", "#3498db", "#bdc3c7"]);	// color set 8
                        .range(["rgba(3, 88, 106, 0.9)", "rgba(38, 185, 154, 1.0)", "rgba(155, 89, 182, 0.9)", "rgba(52, 152, 219, 0.9)", "rgba(189, 195, 199, 0.9)"]);	// color set 9
                //.range(["rgba(3, 88, 106, 0.9)", "rgba(3, 88, 106, 0.7)", "rgba(3, 88, 106, 0.5)", "rgba(3, 88, 106, 0.3)", "rgba(3, 88, 106, 0.1)", "#bdc3c7"]);	// color set 10


                var tooltip = d3.select("body")
                        .append("div")
                        .style("position", "absolute")
                        .style("z-index", "10")
                        .style("visibility", "hidden")
                        .style("display", "none")
                        .style("color", "white")
                        .style("padding", "8px")
                        .style("background-color", "rgba(0, 0, 0, 0.75)")
                        .style("border-radius", "6px")
                        .style("font", "12px sans-serif")
                        .text("tooltip");

                var arc = d3.svg.arc()
                        .outerRadius(radius - 20)
                        .innerRadius(radius - 80);

                var tmp_value;

                var pie = d3.layout.pie()
                        .sort(null)
                        .value(function (d, i) {

                            return d.value;

                            /*                        	
                             i++;                        	
                             tmp_value = "";
                             */

                            /* test data */
                            /*
                             if(i == 1) tmp_value = 95;
                             else if(i == 2) tmp_value = 3;
                             else if(i == 3) tmp_value = 0.8;
                             else if(i == 4) tmp_value = 0.7;
                             else if(i == 5) tmp_value = 0.5;
                             */

                            /* test data */
                            /*
                             if(i == 1) tmp_value = 35;
                             else if(i == 2) tmp_value = 30;
                             else if(i == 3) tmp_value = 25;
                             else if(i == 4) tmp_value = 7;
                             else if(i == 5) tmp_value = 3;
                             */

                            //return tmp_value;
                        });

                var svg = d3.select("#" + chartDivName).append("svg")
                        .attr("width", width)
                        .attr("height", height)
                        .append("g")
                        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");


                svg.append("g")
                        .attr("class", "lines");
                //데이터조작

                var total_cnt;
                data = d3.csv.parse(thisView.collection);
                data.forEach(function (d, i) {
                    //d.DestinationIp = d.labalName;
                    i++;
                    d.EventCount = +d.value;
                    d.Idx = +i;

                    total_cnt = i;
                });

                //d3.csv("data.csv", function(error, data) {

                var text_anchor;

                var g = svg.selectAll(".arc")
                        .data(pie(data))
                        .enter().append("g")
                        .attr("class", "arc")
                        .on("mouseover", function (d) {
                            tooltip.html("<font color=#B0B0B0>" + d.data.label + "</font>");
                            tooltip.style("visibility", "visible");
                            tooltip.style("display", "block");
                            tooltip.attr("class", "chart_tooltip");
                        })
                        .on("mousemove", function (d) {
                            tooltip.style("top", (d3.event.pageY - 10) + "px").style("left", (d3.event.pageX + 10) + "px");
                        })
                        .on("mouseout", function () {
                            tooltip.style("visibility", "hidden");
                            tooltip.style("display", "none");
                        });

                g.append("path")
                        .attr("d", arc)
                        .style("fill", function (d) {
                            return color(d.data.label);
                        });

                g.append("text")
                        .attr("transform", function (d) {
                            var c = arc.centroid(d);

                            /* position fix - start */
                            var point_x, point_y;
                            var ratio = d.data.EventCount;
                            var idx = d.data.Idx;
                            var pie_x, pie_y;

                            pie_x = c[0];
                            pie_y = c[1];

                            // area1                            
                            if (pie_x >= 0 && pie_y < 0) {
                                point_x = radius - 10;
                                point_y = pie_y - 20;
                            }
                            // area2
                            else if (pie_x >= 0 && pie_y >= 0) {
                                point_x = radius - 10;
                                point_y = pie_y + 20;
                            }
                            // area3
                            else if (pie_x < 0 && pie_y >= 0) {
                                point_x = -radius + 10;
                                point_y = pie_y + 20;
                            }
                            // area4
                            else if (pie_x < 0 && pie_y < 0) {
                                point_x = -radius + 10;
                                //point_y = radius - ((radius*2)/total_cnt)*idx + 10;
                                point_y = -(radius - ((total_cnt - idx) * 20 + 10));

                            }

                            /* position fix - end */

                            //return "translate(" + c[0] * 1.4 + "," + c[1] * 1.5 + ")";
                            return "translate(" + point_x + "," + point_y + ")";
                        })
                        .attr("dy", ".35em")
                        .attr("font-size", "10px")
                        .attr("font-face", "arial")
                        .style("text-anchor", function (d) {
                            var c = arc.centroid(d);

                            if (c[0] > 0)
                                text_anchor = "start";
                            else
                                text_anchor = "end";

                            return text_anchor;
                        })
                        .text(function (d) {
                            return d.data.value + "%";
                        });

                g.append("polyline")
                        //.attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
                        .attr("points", function (d) {
                            var c = arc.centroid(d);

                            /* position fix - start */
                            var point_x, point_y;
                            var point1_x, point1_y, point2_x, point2_y, point3_x, point3_y;
                            var ratio = d.data.EventCount;
                            var idx = d.data.Idx;
                            var pie_x, pie_y;

                            pie_x = c[0];
                            pie_y = c[1];

                            // area1                            
                            if (pie_x >= 0 && pie_y < 0) {
                                point_x = radius - 10;
                                point_y = pie_y - 20;
                            }
                            // area2
                            else if (pie_x >= 0 && pie_y >= 0) {
                                point_x = radius - 10;
                                point_y = pie_y + 20;
                            }
                            // area3
                            else if (pie_x < 0 && pie_y >= 0) {
                                point_x = -radius + 10;
                                point_y = pie_y + 20;
                            }
                            // area4
                            else if (pie_x < 0 && pie_y < 0) {
                                point_x = -radius + 10;
                                //point_y = radius - ((radius*2)/total_cnt)*idx + 10;
                                point_y = -(radius - ((total_cnt - idx) * 20 + 10));

                            }

                            point1_x = pie_x;
                            point1_y = pie_y;

                            /*                            
                             if(point_x < 0) point2_x = point_x + (idx * 20);
                             else point2_x = point_x - (idx * 20);
                             */
                            point2_x = point1_x
                            point2_y = point_y;

                            point3_x = point_x;
                            point3_y = point_y;

                            /* position fix - end */
                            return point1_x + ", " + point1_y + ", " + point2_x + ", " + point2_y + ", " + point3_x + ", " + point3_y;
                            //return point1_x + ", " + point1_y + ", " + point3_x + ", " + point3_y;
                            //return "translate("+arc.centroid(d)+")";
                        });
                /*
                 
                 var polyline = svg.select(".arc").selectAll("polyline")
                 .data(pie(data));
                 
                 polyline.enter()
                 .append("polyline");
                 
                 polyline.transition().duration(1000)
                 .attrTween("points", function(d){
                 this._current = this._current || d;
                 var interpolate = d3.interpolate(this._current, d);
                 this._current = interpolate(0);
                 return function(t) {
                 var d2 = interpolate(t);
                 var pos = arc.centroid(d2);
                 pos[0] = radius * 0.95 * (midAngle(d2) < Math.PI ? 1 : -1);
                 return [arc.centroid(d2), outerArc.centroid(d2), pos];
                 };			
                 });
                 
                 polyline.exit()
                 .remove();
                 */

                //});

            });
            this.$el.html(this.svg);
            return this;
        },
        stackedArea: function () {
            var svg;
            var chartDivName = this.chartName;
            var data = this.collection;
            var thisView = this;
            var parentElName;
            var dataType = this.dataType;
            var chartHeight = this.chartHeight;
            var svgMainMarginTop = this.svgMainMarginTop,
                svgMainMarginRight = this.svgMainMarginRight,
                svgMainMarginBottom = this.svgMainMarginBottom,
                svgMainMarginLeft = this.svgMainMarginLeft;
            var svgMiniMarginTop = this.svgMiniMarginTop;

            require(['d3'], function (d3) {
                var $chartDiv = $('#' + chartDivName + '_chart');
                var $selChartDiv = $('#' + chartDivName + '_chart');
                //var $chartDiv = $(thisView.parentEl).closest('.tab-pane').find('.' + chartDivName + '_chart');
                //var $selChartDiv = $('.tab-content').find('.active');
                thisView.parentElName = $chartDiv.closest('.tab-pane').prop('id');

                $chartDiv.empty();

                var divWid = $selChartDiv.width(),
                    divHei = $chartDiv.height();

                var margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                };
                var mini_margin = {
                    top: svgMiniMarginTop,
                    right: svgMainMarginRight,
                    left: svgMainMarginLeft
                };
                var width = divWid - margin.left - margin.right,
                    height = divHei - margin.top - margin.bottom,
                    mini_height = 15;

                var format = d3.time.format("%Y-%m-%d %H:%M");
                var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                    parseDate = formatDate.parse,
                    bisectDate = d3.bisector(function (d) {
                        return d.time;
                    }).left,
                    formatOutput = function (d) {
                        if (thisView.dataType == "bps") {
                            return dataExpression.getFormatTrafficData(d.value);
                        } else if (thisView.dataType == "count") {
                            return dataExpression.getFormatTrafficData(d.value);
                        } else {
                            return d.value;
                        }
                    };

                var x = d3.time.scale()
                        .range([0, width]);

                var mini_x = d3.time.scale()
                        .range([0, width]);

                var y = d3.scale.linear()
                        .range([height, 0]);

                var mini_y = d3.scale.linear()
                        .range([mini_height, 0]);

                var xAxis = d3.svg.axis()
                        .scale(x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var yAxis = d3.svg.axis()
                        .scale(y)
                        .orient("left")
                        .tickFormat(dataExpression.bytesToString);

                var mini_xAxis = d3.svg.axis()
                        .scale(mini_x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var mini_yAxis = d3.svg.axis()
                        .scale(mini_y)
                        .tickFormat("")
                        .orient("left");

                //svg = d3.select("#" + thisView.parentElName + " #" + chartDivName + "_chart")
                //svg = d3.select("#" + thisView.parentElName + " #" + chartDivName + "_chart")

                svg = d3.select("#" + chartDivName + "_chart")
                        .attr("width", divWid)
                        .attr("height", divHei)
                        .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                        .attr('preserveAspectRatio', 'xMinYMin');

                svg.append("defs").append("clipPath")
                        .attr("id", "clip")
                        .append("rect")
                        .attr("width", width)
                        .attr("height", height);

                var main = svg.append("g")
                        .attr("class", "main")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
                        .attr("width", width)
                        .attr("height", height);

                var tempLabelName = [];
                var areaColor = [];
                var tempKey = [];

                data.forEach(item => {
                    tempLabelName = [...tempLabelName, item.key];
                });
                tempLabelName = tempLabelName.reverse();

                //debugger
                var newDataset = tempLabelName.map(function (n) {
                    areaColor = [...areaColor, data[n].color];
                    tempKey = [...tempKey, data[n].key];
                    return data[n].values.map(d => {
                        return { key: tempKey[n], x: d.time, y: d.value, y0: 0, areaColor: areaColor[n] };
                    });
                });
                d3.layout.stack()(newDataset);

                x.domain([
                    d3.min(data, function (kv) {
                        return d3.min(kv.values, function (d) {
                            return d.time;
                        })
                    }),
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.time;
                        })
                    })
                ]);

                //전체값 존재 여부 확인
                var totalYn = "N";
                data.forEach(d => {
                    if (d.color === "#BDBDBD") totalYn = "Y";
                });

                if (totalYn === "Y") {
                    y.domain([
                        0,
                        d3.max(data, function (kv) {
                            return d3.max(kv.values, function (d) {
                                return d.value;
                            })
                        }) * 1.2
                    ]);
                } else {
                    // 분석 차트일 때
                    if(chartDivName === "protocol") {
                        var tot = 0;
                        //Total 값 재구성(
                        for(var i = 0; i < data[0].values.length; i++){
                            var tmpTot = 0;
                            for(var j = 0; j < data.length; j++){
                                tmpTot += data[j].values[i].value;
                            }
                            if(tot < tmpTot){
                                tot = tmpTot;
                            }
                        }
                        //end
                        y.domain([0, tot]);
                    } else {
                        var tot = 0;
                        var tmpData = null;
                        d3.max(data, function(kv) {
                            if(tmpData == null){
                                tmpData = kv.values;
                            }
                            else {
                                $.each(kv.values, function(a){
                                    tmpData[a].value += kv.values[a].value;
                                })
                            }
                        });
                        if(tmpData !== undefined){
                            y.domain([0,
                                d3.max(tmpData, function(d) {
                                    return d.value;
                                }) * 1.2
                            ]);
                        }
                    }
                }

                var area = d3.svg.area()
                        .x(function (d) {
                            return x(d.x);
                        })
                        .y0(function (d) {
                            return y(d.y0);
                        })
                        .y1(function (d) {
                            return y(d.y0 + d.y);
                        });

                var multiDataGroup = main.selectAll(".multiDataGroup")
                        .data(newDataset)
                        .enter()
                        .append("g")
                        .attr("class", "multiDataGroup")
                        .style("fill", d => {
                            if (d[0].areaColor !== "#BDBDBD") return d[0].areaColor;
                        });

                multiDataGroup.append("path")
                        .attr("clip-path", "url(#clip)")
                        /*.attr("class", "test1")*/
                        .attr("d", d => {
                            if (d[0].areaColor !== "#BDBDBD") return area(d);
                        });

                var line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x(d.x);
                        })
                        .y(function (d) {
                            return y(d.y);
                        });

                var multiDataTotal = main.selectAll(".multiDataTotal")
                        .data(newDataset)
                        .enter()
                        .append("g")
                        .attr("class", "line");

                multiDataTotal.append("path")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", d => {
                            if (d[0].areaColor === "#BDBDBD") return line(d);
                        })
                        .style("stroke", d => {
                            return d[0].areaColor;
                        });

                main.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + height + ")")
                        .call(xAxis)
                        .selectAll('text')
                        .style('text-anchor', 'end')
                        .attr('dx', '-.8em')
                        .attr('dy', '.15em')
                        .attr("transform", "rotate(-30)");

//                main.append("g")
//                        .attr("class", "y axis")
//                        .call(yAxis);


                /* y축 단위 추가
                 * 침입탐지분석 차트 축 count
                 * 트래픽분석 차트 축 bps
                 */
                main.append("g")
                        .attr("class", "y axis")
                        .call(yAxis)
                        .append("text")
                        .attr("x", 7)
                        .attr("y", -15)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text(thisView.dataType);

                var focus = main.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("line")
                        .attr("class", "line")
                        .attr("x1", 10).attr("x2", 10)
                        .attr("y1", 0).attr("y2", height);

                var $tooltipDiv;
                if (thisView.parentEl) {
                    $tooltipDiv = "#" + thisView.parentElName + " " + "#tooltipDiv";
                } else {
                    $tooltipDiv = "#tooltipDiv";
                }

                $($tooltipDiv + " #tooltipList").html("");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", width)
                        .attr("height", height)
                        .on("mouseover", function (d) {
                            focus.style("display", null);
                            d3.select($tooltipDiv)
                                    .style("visibility", "visible")
                                    .style("display", "block");
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                            d3.select($tooltipDiv)
                                    .style("visibility", "hidden")
                                    .style("display", "none");
                        })
                        .on("mousemove", mousemove);

                //미니 그래프 시작
                var brush = d3.svg.brush()
                        .x(mini_x)
                        .on("brushend", brushed);

                var mini = svg.append("g")
                        .attr("class", "mini")
                        .attr("transform", "translate(" + mini_margin.left + "," + mini_margin.top + ")");

                mini_x.domain(x.domain());
                mini_y.domain(y.domain());

                mini.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(mini_xAxis);

                var mini_line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return mini_x(d.x);
                        })
                        .y(function (d) {
                            return mini_y(d.y);
                        });

                var mini_multiDataGroup = mini.selectAll(".multiDataGroup")
                        .data(newDataset)
                        .enter().append("g")
                        .attr("class", "multiDataGroup");

                mini_multiDataGroup.append("path")
                        .attr("clip-path", "url(#clip)")
                        .attr("class", "line")
                        .attr("d", function (d) {
                            return mini_line(d);
                        })
                        .style("stroke", d => {
                            return d[0].areaColor;
                        });

                mini_multiDataGroup.append("g")
                        .attr("class", "grid")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(make_mini_x_axis()
                                .tickSize(-mini_height, 0, 0)
                                .tickFormat(""));

                //미니사이즈에 범위 선택 드래그
                mini_multiDataGroup.append("g")
                        .attr("class", "brush")
                        .call(brush)
                        .selectAll("rect")
                        .attr("y", -6)
                        .attr("height", mini_height + 7);


                function make_mini_x_axis() {
                    return d3.svg.axis()
                            .scale(mini_x)
                            .orient("bottom")
                }

                function brushed() {
                    if (!d3.event.sourceEvent)
                        return; // only transition after input
                    var extent0 = brush.extent();
                    var extent1 = extent0.map(d3.time.minute);

                    extent1[0] = my5MinRound(extent0[0]);
                    extent1[1] = my5MinRound(extent0[1]);

                    if (brush.empty()) {
                        x.domain(mini_x.domain());
                    } else {
                        x.domain(extent1);
                        //brush.extent(extent1);
                    }

                    main.selectAll(".multiDataGroup")
                            .select("path")
                            .attr("d", d => {
                                if (d[0].areaColor !== "#BDBDBD") return area(d);
                            });

                    main.selectAll(".line")
                            .select("path")
                            .attr("d", d => {
                                if (d[0].areaColor === "#BDBDBD") return line(d);
                            })
                            .style("stroke", d => {
                                return d[0].areaColor;
                            });

                    main.select(".x.axis").call(xAxis);

                    mini.selectAll(".brush")
                            .transition()
                            .call(brush.extent(extent1))
                            .call(brush.event);
                }

                function my5MinRound(date) {
                    var subHalf = d3.time.minute.offset(date, -2.5);
                    var addHalf = d3.time.minute.offset(date, 2.5);
                    return d3.time.minutes(subHalf, addHalf, 5)[0];
                }

                function mousemove() {

                    var xLine = d3.mouse(this)[0];
                    focus.select(".line")
                            .attr("x1", xLine)
                            .attr("x2", xLine)
                            .style("opacity", 0.5)
                            .style("stroke", "blue");

                    var x0 = x.invert(xLine),
                            i = bisectDate(data[0].values, x0);

                    if (i > data[0].values.length / 2) {
                        d3.select($tooltipDiv)
                                .style("top", (d3.event.pageY - 10) + "px")
                                .style("left", (d3.event.pageX - 180) + "px");
                    } else {
                        d3.select($tooltipDiv)
                                .style("top", (d3.event.pageY - 10) + "px")
                                .style("left", (d3.event.pageX + 10) + "px");
                    }

                    d3.select($tooltipDiv + " #tooltipTime")
                            .text(format(data[0].values[i].time));

                    var tooltipContent = "";
                    for (var j = data.length - 1; j >= 0; j--) {
                        tooltipContent += "<tr><td width='10' align='left'><table><tr><td width='10' height='10' bgcolor='" + data[j].color + "'></td></tr></table></td>";
                        tooltipContent += "<td width='100' align='left' height=22><font color='" + data[j].color + "'><div style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width:100px;'>&nbsp;" + data[j].strName;
                        +"</div></font></td>";
                        tooltipContent += "<td width='45' align='right' height=22><font color='" + data[j].color + "'>" + dataExpression.getFormatTrafficData(data[j].values[i].value) + "</font></td></tr>";
                    }

                    d3.select($tooltipDiv + " #tooltipList")
                            .html(tooltipContent);
                }
            });

            var outputChart = this.templateAnalysisWithTooltip({
                chartName: this.chartName,
                chart: this.svg,
                chartHeight: chartHeight
            });

            this.$el.html(outputChart);
            return this;
        },
        stackedAreaDual: function () {
            var svg;
            var chartDivName = this.chartName;
            var data = this.collection;
            var thisView = this;
            var parentElName;
            var dataType = this.dataType;
            var chartHeight = this.chartHeight;

            var svgMainMarginTop = this.svgMainMarginTop,
                    svgMainMarginRight = this.svgMainMarginRight,
                    svgMainMarginBottom = this.svgMainMarginBottom,
                    svgMainMarginLeft = this.svgMainMarginLeft;
            var svgMiniMarginTop = this.svgMiniMarginTop;

            require(['d3'], function (d3) {
                if (thisView.parentEl) {
                    var $chartDiv = $(thisView.parentEl).closest('.tab-pane').find('.' + chartDivName + '_chart');
                    var $selChartDiv = $('.tab-content').find('.active');
                    thisView.parentElName = $chartDiv.closest('.tab-pane').prop('id');
                } else {
                    var $selChartDiv = $('#' + chartDivName + '_chart');
                    var $chartDiv = $('#' + chartDivName + '_chart');
                }
                $chartDiv.empty();

                var divWid = $selChartDiv.width(),
                        divHei = $chartDiv.height();
                var margin = {
                    top: svgMainMarginTop,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                mini_margin = {
                    top: svgMiniMarginTop,
                    right: svgMainMarginRight,
                    left: svgMainMarginLeft
                },
                width = divWid - margin.left - margin.right,
                        height = divHei - margin.top - margin.bottom,
                        mini_height = 15;

                //DUAL 추가//
                var margin2 = {
                    top: svgMainMarginTop + height,
                    right: svgMainMarginRight,
                    bottom: svgMainMarginBottom,
                    left: svgMainMarginLeft
                },
                mini_margin2 = {
                    top: svgMainMarginTop + 100 + height,
                    right: svgMainMarginRight,
                    left: svgMainMarginLeft
                },
                ////////////////
                width2 = divWid - margin2.left - margin2.right,
                        height2 = divHei - margin.top - margin.bottom;


                var format = d3.time.format("%Y-%m-%d %H:%M");
                var formatDate = d3.time.format("%Y-%m-%d %H:%M"),
                        parseDate = formatDate.parse,
                        bisectDate = d3.bisector(function (d) {
                            return d.time;
                        }).left,
                        formatOutput = function (d) {
                            return dataExpression.getFormatTrafficData(d.valueOut);
                        },
                        formatOutput2 = function (d) {
                            return dataExpression.getFormatTrafficData(d.valueIn);
                        };

                var x = d3.time.scale()
                        .range([0, width]);

                var mini_x = d3.time.scale()
                        .range([0, width]);

                var x2 = d3.time.scale()
                        .range([0, width2]);

                var y = d3.scale.linear()
                        .range([height, 0]);

                var mini_y = d3.scale.linear()
                        .range([mini_height, 0]);

                var y2 = d3.scale.linear()
                        .range([0, height2]);

                var xAxis = d3.svg.axis()
                        .scale(x)
                        .tickFormat("")
                        .orient("bottom");

                var xAxis2 = d3.svg.axis()
                        .scale(x2)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var yAxis = d3.svg.axis()
                        .scale(y)
                        .orient("left")
                        .tickFormat(dataExpression.bytesToString)
                        .ticks(4);

                var yAxis2 = d3.svg.axis()
                        .scale(y2)
                        .orient("left")
                        .tickFormat(dataExpression.bytesToString)
                        .ticks(4);

                var mini_xAxis = d3.svg.axis()
                        .scale(mini_x)
                        .tickFormat(multiFormat)
                        .orient("bottom");

                var mini_yAxis = d3.svg.axis()
                        .scale(mini_y)
                        .tickFormat("")
                        .orient("left");

                if (thisView.parentElName) {
                    svg = d3.select("#" + thisView.parentElName + " #" + chartDivName + "_chart")
                            .attr("width", divWid)
                            .attr("height", divHei)
                            .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                            .attr('preserveAspectRatio', 'xMinYMin');
                } else {
                    svg = d3.select("#" + chartDivName + "_chart")
                            .attr("width", divWid)
                            .attr("height", divHei)
                            .attr('viewBox', '0 0 ' + divWid + ' ' + divHei)
                            .attr('preserveAspectRatio', 'xMinYMin');
                }

                svg.append("defs").append("clipPath")
                        .attr("id", "clip")
                        .append("rect")
                        .attr("width", width)
                        .attr("height", height);

                var main = svg.append("g")
                        .attr("class", "main")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
                //.attr("width", width)
                //.attr("height", height);

                var main2 = svg.append("g")
                        .attr("class", "main2")
                        .attr("transform", "translate(" + margin2.left + "," + margin2.top + ")");
                //.attr("width", width)
                //.attr("height", height);

                var tempLabelName = [];
                data.forEach(function (kv, i) {
                    tempLabelName.push(kv.key);
                });

                tempLabelName = tempLabelName.reverse();
                var areaColor = [];
                var tempKey = [];
                var newDataset = tempLabelName.map(function (n) {
                    areaColor.push(data[n].color);
                    tempKey.push(data[n].key);
                    return data[n].values.map(function (d, i) {
                        return {key: data[n].key, x: d.time, y: d.valueOut, y0: 0, areaColor: data[n].color};
                    });
                });
                d3.layout.stack()(newDataset);

                var newDataset2 = tempLabelName.map(function (n) {
                    areaColor.push(data[n].color);
                    tempKey.push(data[n].key);
                    return data[n].values.map(function (d, i) {
                        return {key: data[n].key, x: d.time, y: d.valueIn, y0: 0, areaColor: data[n].color};
                    });
                });
                d3.layout.stack()(newDataset2);

                x.domain([
                    d3.min(data, function (kv) {
                        return d3.min(kv.values, function (d) {
                            return d.time;
                        })
                    }),
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.time;
                        })
                    })
                ]);

                x2.domain([
                    d3.min(data, function (kv) {
                        return d3.min(kv.values, function (d) {
                            return d.time;
                        })
                    }),
                    d3.max(data, function (kv) {
                        return d3.max(kv.values, function (d) {
                            return d.time;
                        })
                    })
                ]);

                //전체값 존재 여부 확인

                var totalYn = "N";
                for (var key in data) {
                    if (data[key].color == "#BDBDBD") {
                        totalYn = "Y";
                    }
                }
                ;
                if (totalYn == "Y") {
                    y.domain([
                        0,
                        d3.max(data, function (kv) {
                            return d3.max(kv.values, function (d) {
                                return d.valueOut;
                            })
                        }) * 1.2
                    ]);

                    y2.domain([
                        0,
                        d3.max(data, function (kv) {
                            return d3.max(kv.values, function (d) {
                                return d.valueIn;
                            })
                        }) * 1.2
                    ]);
                } else {
                    y.domain([
                        0,
                        d3.max(data, function (kv) {
                            return d3.max(newDataset, function (d, i) {
                                return (d[i].y0 + d[i].y);
                            });
                        }) * 1.4
                    ]);

                    y2.domain([
                        0,
                        d3.max(data, function (kv) {
                            return d3.max(newDataset2, function (d, i) {
                                return (d[i].y0 + d[i].y);
                            });
                        }) * 1.4
                    ]);

                }

                var area = d3.svg.area()
                        .x(function (d) {
                            return x(d.x);
                        })
                        .y0(function (d) {
                            return y(d.y0);
                        })
                        .y1(function (d) {
                            return y(d.y0 + d.y);
                        });

                var area2 = d3.svg.area()
                        .x(function (d) {
                            return x(d.x);
                        })
                        .y0(function (d) {
                            return y2(d.y0);
                        })
                        .y1(function (d) {
                            return y2(d.y0 + d.y);
                        });

                var multiDataGroup = main.selectAll(".multiDataGroup")
                        .data(newDataset)
                        .enter()
                        .append("g")
                        .attr("class", "multiDataGroup")
                        .style("fill", function (d, i) {
                            if (d[i].areaColor != "#BDBDBD") {
                                return d[i].areaColor;
                            }
                        });

                multiDataGroup.append("path")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d, i) {
                            if (d[i].areaColor != "#BDBDBD") {
                                return area(d);
                            }
                        });

                var multiDataGroup2 = main2.selectAll(".multiDataGroup2")
                        .data(newDataset2)
                        .enter()
                        .append("g")
                        .attr("class", "multiDataGroup2")
                        .style("fill", function (d, i) {
                            if (d[i].areaColor != "#BDBDBD") {
                                return d[i].areaColor;
                            }
                        });

                multiDataGroup2.append("path")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d, i) {
                            if (d[i].areaColor != "#BDBDBD") {
                                return area2(d);
                            }
                        });

                var line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x(d.x);
                        })
                        .y(function (d) {
                            return y(d.y);
                        });

                var line2 = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return x2(d.x);
                        })
                        .y(function (d) {
                            return y2(d.y);
                        });

                var multiDataTotal = main.selectAll(".multiDataTotal")
                        .data(newDataset)
                        .enter()
                        .append("g")
                        //.attr("class", "multiDataTotal");
                        .attr("class", "line");

                multiDataTotal.append("path")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d, i) {
                            if (d[i].areaColor == "#BDBDBD") {
                                return line(d);
                            }
                        })
                        .style("stroke", function (d, i) {
                            return d[i].areaColor;
                        });

                var multiDataTotal2 = main2.selectAll(".multiDataTotal2")
                        .data(newDataset2)
                        .enter()
                        .append("g")
                        //.attr("class", "multiDataTotal");
                        .attr("class", "line");

                multiDataTotal2.append("path")
                        .attr("clip-path", "url(#clip)")
                        .attr("d", function (d, i) {
                            if (d[i].areaColor == "#BDBDBD") {
                                return line2(d);
                            }
                        })
                        .style("stroke", function (d, i) {
                            return d[i].areaColor;
                        });

                main.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + height + ")")
                        .call(xAxis);

                main.append("g")
                        .attr("class", "y axis")
                        .call(yAxis)
                        .append("text")
                        .attr("x", width)
                        .attr("y", 10)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("Outbound");

                main2.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + height2 + ")")
                        .call(xAxis2);

                main2.append("g")
                        .attr("class", "y axis")
                        .call(yAxis2)
                        .append("text")
                        .attr("x", width2)
                        .attr("y", height2 - 15)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("Inbound");

                var focus = main.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus.append("line")
                        .attr("class", "line")
                        .attr("x1", 10).attr("x2", 10)
                        .attr("y1", 0).attr("y2", height);

                var focus2 = main2.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                focus2.append("line")
                        .attr("class", "line")
                        .attr("x1", 10).attr("x2", 10)
                        .attr("y1", 0).attr("y2", height2);

                var $tooltipDiv;
                if (thisView.parentEl) {
                    $tooltipDiv = "#" + thisView.parentElName + " " + "#tooltipDiv";
                } else {
                    $tooltipDiv = "#tooltipDiv";
                }

                $($tooltipDiv + " #tooltipList").html("");

                var $tooltipDiv2;
                if (thisView.parentEl) {
                    $tooltipDiv2 = "#" + thisView.parentElName + " " + "#tooltipDiv2";
                } else {
                    $tooltipDiv2 = "#tooltipDiv2";
                }

                $($tooltipDiv2 + " #tooltipList").html("");

                main.append("rect")
                        .attr("class", "overlay")
                        .attr("width", width)
                        .attr("height", height)
                        .on("mouseover", function (d) {
                            focus.style("display", null);
                            d3.select($tooltipDiv)
                                    .style("visibility", "visible")
                                    .style("display", "block");
                        })
                        .on("mouseout", function () {
                            focus.style("display", "none");
                            d3.select($tooltipDiv)
                                    .style("visibility", "hidden")
                                    .style("display", "none");
                        })
                        .on("mousemove", mousemove);

                main2.append("rect")
                        .attr("class", "overlay")
                        .attr("width", width2)
                        .attr("height", height2)
                        .on("mouseover", function (d) {
                            focus2.style("display", null);
                            d3.select($tooltipDiv2)
                                    .style("visibility", "visible")
                                    .style("display", "block");
                        })
                        .on("mouseout", function () {
                            focus2.style("display", "none");
                            d3.select($tooltipDiv2)
                                    .style("visibility", "hidden")
                                    .style("display", "none");
                        })
                        .on("mousemove", mousemove2);

                //미니 그래프 시작
                var brush = d3.svg.brush()
                        .x(mini_x)
                        .on("brushend", brushed);

                var mini = svg.append("g")
                        .attr("class", "mini")
                        .attr("transform", "translate(" + mini_margin.left + "," + mini_margin.top + ")");

                mini_x.domain(x.domain());
                mini_y.domain(
                        [
                            0,
                            d3.max(data, function (kv) {
                                return d3.max(kv.values, function (d) {
                                    return d.value;
                                })
                            })
                        ]
                        );

                mini.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(mini_xAxis);

                var mini_line = d3.svg.line()
                        //.interpolate("cardinal")
                        .x(function (d) {
                            return mini_x(d.x);
                        })
                        .y(function (d) {
                            return mini_y(d.y);
                        });

                var mini_multiDataGroup = mini.selectAll(".multiDataGroup")
                        .data(newDataset)
                        .enter().append("g")
                        .attr("class", "multiDataGroup");

                mini_multiDataGroup.append("path")
                        .attr("clip-path", "url(#clip)")
                        .attr("class", "line")
                        .attr("d", function (d) {
                            //if(d[i].areaColor == "#BDBDBD"){
                            return mini_line(d);
                            //}
                        })
                        .style("stroke", function (d, i) {
                            return d[i].areaColor;
                        });

                mini_multiDataGroup.append("g")
                        .attr("class", "grid")
                        .attr("transform", "translate(0," + mini_height + ")")
                        .call(make_mini_x_axis()
                                .tickSize(-mini_height, 0, 0)
                                .tickFormat("")
                                );

                //미니사이즈에 범위 선택 드래그
                mini_multiDataGroup.append("g")
                        .attr("class", "brush")
                        .call(brush)
                        .selectAll("rect")
                        .attr("y", -6)
                        .attr("height", mini_height + 7);

                function make_mini_x_axis() {
                    return d3.svg.axis()
                            .scale(mini_x)
                            .orient("bottom")
                }

                function brushed() {
                    if (!d3.event.sourceEvent)
                        return; // only transition after input
                    var extent0 = brush.extent();
                    var extent1 = extent0.map(d3.time.minute);

                    extent1[0] = my5MinRound(extent0[0]);
                    extent1[1] = my5MinRound(extent0[1]);

                    if (brush.empty()) {
                        x.domain(mini_x.domain());
                        x2.domain(mini_x.domain());
                    } else {
                        x.domain(extent1);
                        x2.domain(extent1);
                    }

                    main.selectAll(".multiDataGroup")
                            .select("path")
                            .attr("d", function (d, i) {
                                if (d[i].areaColor != "#BDBDBD") {
                                    return area(d);
                                }
                            });

                    main.selectAll(".line")
                            .select("path")
                            .attr("d", function (d, i) {
                                if (d[i].areaColor == "#BDBDBD") {
                                    return line(d);
                                }
                            })
                            .style("stroke", function (d, i) {
                                return d[i].areaColor;
                            });

                    main2.selectAll(".multiDataGroup2")
                            .select("path")
                            .attr("d", function (d, i) {
                                if (d[i].areaColor != "#BDBDBD") {
                                    return area2(d);
                                }
                            });

                    main2.selectAll(".line")
                            .select("path")
                            .attr("d", function (d, i) {
                                if (d[i].areaColor == "#BDBDBD") {
                                    return line2(d);
                                }
                            })
                            .style("stroke", function (d, i) {
                                return d[i].areaColor;
                            });


                    main.select(".x.axis").call(xAxis);
                    main2.select(".x.axis").call(xAxis2);

                    mini.selectAll(".brush")
                            .transition()
                            .call(brush.extent(extent1))
                            .call(brush.event);
                }

                function my5MinRound(date) {
                    var subHalf = d3.time.minute.offset(date, -2.5);
                    var addHalf = d3.time.minute.offset(date, 2.5);
                    return d3.time.minutes(subHalf, addHalf, 5)[0];
                }

                function mousemove() {

                    var xLine = d3.mouse(this)[0];
                    focus.select(".line")
                            .attr("x1", xLine)
                            .attr("x2", xLine)
                            .style("opacity", 0.5)
                            .style("stroke", "blue");

                    var x0 = x.invert(xLine),
                            i = bisectDate(data[0].values, x0);

                    if (i > data[0].values.length / 2) {
                        d3.select($tooltipDiv)
                                .style("top", (d3.event.pageY - 10) + "px")
                                .style("left", (d3.event.pageX - 180) + "px");
                    } else {
                        d3.select($tooltipDiv)
                                .style("top", (d3.event.pageY - 10) + "px")
                                .style("left", (d3.event.pageX + 10) + "px");
                    }

                    d3.select($tooltipDiv + " #tooltipTime")
                            .text("[Outbound] " + format(data[0].values[i].time));

                    var tooltipContent = "";
                    for (var j = data.length - 1; j >= 0; j--) {
                        tooltipContent += "<tr><td width='10' align='left'><table><tr><td width='10' height='10' bgcolor='" + data[j].color + "'></td></tr></table></td>";
                        tooltipContent += "<td width='105' align='left' height=22><font color='" + data[j].color + "'><div style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width:105px;'>&nbsp;" + data[j].strName;
                        +"</div></font></td>";
                        tooltipContent += "<td width='45' align='right' height=22><font color='" + data[j].color + "'>" + dataExpression.getFormatTrafficData(data[j].values[i].valueOut) + "</font></td></tr>";
                    }

                    d3.select($tooltipDiv + " #tooltipList")
                            .html(tooltipContent);
                }

                function mousemove2() {

                    var xLine = d3.mouse(this)[0];
                    focus2.select(".line")
                            .attr("x1", xLine)
                            .attr("x2", xLine)
                            .style("opacity", 0.5)
                            .style("stroke", "blue");

                    var x0 = x.invert(xLine),
                            i = bisectDate(data[0].values, x0);

                    if (i > data[0].values.length / 2) {
                        d3.select($tooltipDiv2)
                                .style("top", (d3.event.pageY - 10) + "px")
                                .style("left", (d3.event.pageX - 180) + "px");
                    } else {
                        d3.select($tooltipDiv2)
                                .style("top", (d3.event.pageY - 10) + "px")
                                .style("left", (d3.event.pageX + 10) + "px");
                    }

                    d3.select($tooltipDiv2 + " #tooltipTime")
                            .text("[Inbound] " + format(data[0].values[i].time));

                    var tooltipContent = "";
                    for (var j = data.length - 1; j >= 0; j--) {
                        tooltipContent += "<tr><td width='10' align='left'><table><tr><td width='10' height='10' bgcolor='" + data[j].color + "'></td></tr></table></td>";
                        tooltipContent += "<td width='105' align='left' height=22><font color='" + data[j].color + "'><div style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width:105px;'>&nbsp;" + data[j].strName;
                        +"</div></font></td>";
                        tooltipContent += "<td width='45' align='right' height=22><font color='" + data[j].color + "'>" + dataExpression.getFormatTrafficData(data[j].values[i].valueIn) + "</font></td></tr>";
                    }

                    d3.select($tooltipDiv2 + " #tooltipList")
                            .html(tooltipContent);
                }
            });

            var outputChart = this.templateAnalysisWithTooltip({
                chartName: this.chartName,
                chart: this.svg,
                chartHeight: chartHeight
            });

            this.$el.html(outputChart);
            return this;
        }
    });

    ChartView.MakeChart = function ($el, options) {
        var chart = new ChartView(options);
        $(window).on('resize', function () {

            switch (options.type) {
                case "LinePlusLine":
                    $el.html(chart.linePlusLine().el);
                    break;

                case "LinePlusLineWithArea":
                    $el.html(chart.linePlusLineWithArea().el);
                    break;

                case "LinePlusBar":
                    $el.html(chart.linePlusBar().el);
                    break;

                case "Bubble":
                    $el.html(chart.bubble().el);
                    break;

                case "MultiLine":
                    $el.html(chart.el);
                    chart.multiLine();
                    break;

                case "MultiDualLine":
                    $el.html(chart.multiDualLine().el);
                    break;

                case "PieDonut":
                    $el.html(chart.pieDonut().el);
                    break;

                case "TwoLine":
                    $el.html(chart.twoLine().el);
                    break;

                case "MultiPieDonut":
                    $el.html(chart.MultiPieDonut().el);
                    break;

                case "PiePlusBar":
                    //$el.html(chart.PiePlusBar().el);
                    chart.PiePlusBar();
                    break;

                case "StackedArea":
                    $el.html(chart.stackedArea().el);
                    break;

                case "StackedAreaDual":
                    $el.html(chart.stackedAreaDual().el);
                    break;
                default :
                	$el.html("");
            }
        });

        switch (options.type) {
            case "LinePlusLine":
                $el.html(chart.linePlusLine().el);
                break;

            case "LinePlusLineWithArea":
                $el.html(chart.linePlusLineWithArea().el);
                break;

            case "LinePlusBar":
                $el.html(chart.linePlusBar().el);
                break;

            case "Bubble":
                $el.html(chart.bubble().el);
                break;

            case "MultiLine":
                $el.html(chart.multiLine().el);
                break;

            case "MultiDualLine":
                $el.html(chart.multiDualLine().el);
                break;

            case "PieDonut":
                $el.html(chart.pieDonut().el);
                break;

            case "TwoLine":
                $el.html(chart.twoLine().el);
                break;

            case "MultiPieDonut":
                $el.html(chart.MultiPieDonut().el);
                break;

            case "PiePlusBar":
                //$el.html(chart.PiePlusBar().el);
                chart.PiePlusBar();
                break;

            case "StackedArea":
                $el.html(chart.stackedArea().el);
                break;

            case "StackedAreaDual":
                $el.html(chart.stackedAreaDual().el);
                break;
            default :
            	$el.html("");

        }
        return chart;
    };

    return ChartView;
});
