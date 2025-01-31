define(function (require) {

    //"use strict";

    // require library
    var $ 						= require('jquery'),
        _ 						= require('underscore'),
        Backbone 				= require('backbone'),
        d3 						= require('d3'),
        TableView 				= require('views/dashboard/table');

    // require i18n
    var locale 					= require('i18n!nls/str');
    var analysisDate 			= require('utils/analysisDate');
    var dataExpression 			= require('utils/dataExpression');
    var sessionManager 			= require('utils/sessionManager');
    var MenuCollection 			= require('collections/menuCollection');

    // require template
    var allTpl 					= require('text!tpl/dashboard/dashboard5.html');
    var tpl 					= require('text!tpl/dashboard/dashboard6.html');	//suricata 제외용 대시보드
    var userTpl 				= require('text!tpl/dashboard/dashboard5-2.html');	//사용자용 대시보드
    var userExceptTpl 			= require('text!tpl/dashboard/dashboard6-2.html'), 	//사용자용 대시보드
        template 				= _.template(tpl),
        allTemplate 			= _.template(allTpl),
        userTpl 				= _.template(userTpl),
        userExceptTpl 			= _.template(userExceptTpl);

    //테이블 컬렉션/모델
    var ServiceTopNCollection 	= require('collections/dashboard/serviceCollection');
    var EventTopNCollection 	= require('collections/dashboard/eventCollection');
    var VictimIpTopNCollection 	= require('collections/dashboard/victimIpCollection');
    var AuditTopNCollection 	= require('collections/dashboard/auditCollection');
    var ApplicationCollection 	= require('collections/detectionAnalysis/applicationCollection');

    //기관별 데이터 컬렉션/모델
    //var OrgCollection 		= require('collections/dashboard/orgCollection');//버블차트부분

    //차트 컬렉션/모델
    var LineChartCollection 	= require('collections/dashboard/lineChartCollection');

    var intervalFlag = true;
    var refreshTime = 60000 * 5;

    return Backbone.View.extend({
        tagName: 'div',
        targetCategory: sessionManager.Category, // 범위 분류 (0: 전체, 1: 네트워크 그룹, 2: 네트워크)
        targetRefIndex: sessionManager.RefIndex, // 네트워크 그룹 또는 네트워크 인덱스
        targetPathName: sessionManager.PathName,
        initialize: function (options) {
            var thisView = this;

            thisView.timeHour = 12;

            // 콜렉션 생성 
            thisView.protocolChartCollection = new LineChartCollection();
            thisView.eventChartCollection = new LineChartCollection();
            thisView.statProtocolChartCollection = new LineChartCollection();

            thisView.formatDate = d3.time.format("%Y-%m-%d %H:%M"),
            thisView.parseDate = this.formatDate.parse;

            //테이블용 파라미터 전달 변수
            thisView.tableDay, thisView.tableDate, thisView.topN = 5;
            //차트용 파라미터 전달 변수
            thisView.chartDay, thisView.chartDate, thisView.interval = thisView.timeHour * 60 * 60; // 6시간 그래프 => 12시간으로 변경  
            //결과
            thisView.resultTotMal;
            thisView.resultTotMalEvt;
            thisView.resultTotPro;
            //thisView.resultOrg; //버블차트
            thisView.resultVictimIpTopN;

            //계정별 로그인 조건 추가
            thisView.selPath = thisView.targetPathName;
            var category = Number(thisView.targetCategory);
            if (category == 0) {
                thisView.selNetworkGroup = 0, thisView.selNetwork = 0, thisView.selVsensor = 0, thisView.selSensor = null;
            } else if (category == 1) { //네트워크 그룹으로 로그인
                thisView.selNetworkGroup = thisView.targetRefIndex, thisView.selNetwork = 0, thisView.selVsensor = -1, thisView.selSensor = null;
            } else if (category == 2) { //네트워크로 로그인
                thisView.selNetworkGroup = null, thisView.selNetwork = thisView.targetRefIndex, thisView.selVsensor = null, thisView.selSensor = -1;
            } else if (category == 3) { //물리센서로 로그인
                thisView.selNetworkGroup = null, thisView.selNetwork = 0, thisView.selVsensor = null, thisView.selSensor = thisView.targetRefIndex;
            } else if (category == 4) { //가상센서로 로그인
                thisView.selNetworkGroup = null, thisView.selNetwork = 0, thisView.selVsensor = thisView.targetRefIndex, thisView.selSensor = null;
            }

            //D3 로 생성된 차트만 resize 해준다.
            $(window).on('resize', function () {
                thisView.makeTotMalTrafficChart(thisView.resultTotMal);
                thisView.makeMalTrafficEventChart(thisView.resultTotMalEvt);
            });

            //테이블용 데이터(테이블명, 시간)
            thisView.tableDay = analysisDate.getAnalysisDay();
            thisView.tableDate = analysisDate.getAnalysisTime();
            //감사로그용 현재시간 조회
            thisView.tableNotAnalysisDate = analysisDate.getAnalysisTimeInitialization();
            thisView.tableDateSel = analysisDate.getAnalysisListTime(thisView.tableDate, thisView.interval, 0); // 통계 조회 시간 12시간으로 변경
            
            //감사로그용 현재시간 기준 12시간 간격 조회
            thisView.tableNotAnalysisDateSel = analysisDate.getNotAnalysisListTime(thisView.tableNotAnalysisDate, thisView.interval, 0); // 통계 조회 시간 12시간으로 변경            

            //차트용 데이터(테이블명, 시간)
            thisView.chartDay = analysisDate.getAnalysisDay();
            thisView.chartDate = analysisDate.getAnalysisChartTime(thisView.tableDate, thisView.interval, 0);
            Backbone.TableView = TableView;
            thisView.role = sessionManager.role;

        },
        render: function () {
            var thisView = this;
            // SYSTEM_TYPE 
            // suricata 제외 버전 
            //if (this.collection.length <= 49) {
            if (SYSTEM_TYPE == 3) {
                // SYSTEM_TYPE 3: KERIS 
                if (this.role == 1) {
                    // 사용자 계정 권한 
                    this.$el.html(userExceptTpl({
                        locale: locale,
                        targetCategory: this.targetCategory
                    }));
                } else {
                    // 관리자 계정 권한
                    this.$el.html(template({
                        locale: locale,
                        targetCategory: this.targetCategory
                    }));
                }
            } else {
                // SYSTEM_TYPE 1: 일반, 2: CC 
                if (this.role == 1) {
                    this.$el.html(userTpl({
                        locale: locale,
                        targetCategory: this.targetCategory
                    }));
                } else {
                    this.$el.html(allTemplate({
                        locale: locale,
                        targetCategory: this.targetCategory
                    }));
                }
            }
            return this;
        },
        renderData: function () {
            var thisView = this;

            this.makeServiceTopNTable();
            this.makeEventTopNTable();
            // Application Top5 
            if (SYSTEM_TYPE != 3) {
                this.makeApplicationTopNTable();
            }
            if (Number(this.targetCategory) == 0) {
                this.makeAuditTopNTable();
            }
            $.when(this.makeVictimIpTopNTable()).done(function () {
                thisView.makeVictimIpChart(thisView.resultVictimIpTopN);
            });

            //비동기
            $.when(this.getTotMalTrafficData()).done(function () {
                thisView.makeTotMalTrafficChart(thisView.resultTotMal);
            });
            
            $.when(this.getTotMalEventData()).done(function () {
                thisView.makeMalTrafficEventChart(thisView.resultTotMalEvt);
            });

            //프로토콜별 추이그래프
            thisView.getProtocolGraphData();

            return this;
        },
        //전체/유해트래픽 그래프
        getTotMalTrafficData: function() {
            var thisView = this;

            //-------------------------- 전체 트래픽 데이터 가져오기-------------------//
            //조건 설정
            var protocolChartParam = {
                tmstart: thisView.chartDate[0],
                tmend: thisView.chartDate[1],
                timeHour: thisView.timeHour,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };

            // 테이블 조회 
            thisView.protocolChartCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectProtocolTraffic',
                contentType: 'application/json',
                data: JSON.stringify(protocolChartParam),
                async: false
            });

            $("#chartDateTotMal", thisView.el).text(thisView.chartDate[0] + " ~ " + thisView.chartDate[1]);

            //-------------------------- 유해 트래픽 데이터 가져오기-------------------//
            //조건 설정
            var statProtocolChartParam = {
                tmstart: thisView.chartDate[0],
                tmend: thisView.chartDate[1],
                timeHour: thisView.timeHour,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };

            $("#chartDateEvtMal", thisView.el).text(thisView.chartDate[0] + " ~ " + thisView.chartDate[1]);
            // 테이블 조회 
            thisView.statProtocolChartCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectStatTraffic',
                contentType: 'application/json',
                data: JSON.stringify(statProtocolChartParam),
                async: false
            });
            
            var tmstartStr = thisView.protocolChartCollection.pluck("tmstart");
            var dblbpsStr = thisView.protocolChartCollection.pluck("dblbps");
            var eventDblbpsStr = thisView.statProtocolChartCollection.pluck("eventDblbps");

            //전체트래픽/유해트래픽 그래프 그리기
            thisView.resultTotMal = "Time,EventBps,TrafficBps\n";
            for (var i = 0; i < thisView.protocolChartCollection.length; i++) {
                if (i != thisView.protocolChartCollection.length - 1) {
                    thisView.resultTotMal += tmstartStr[i] + "," + eventDblbpsStr[i] + "," + dblbpsStr[i] + "\n";
                } else {
                    thisView.resultTotMal += tmstartStr[i] + "," + eventDblbpsStr[i] + "," + dblbpsStr[i];
                }
            }
        },
        getTotMalEventData: function() {
        	var thisView = this;
        	// -------------------------- 공격건수 가져오기 --------------------------// 
            //조건 설정
            var eventChartParam = {
                tmstart: thisView.chartDate[0],
                tmend: thisView.chartDate[1],
                timeHour: thisView.timeHour,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };

            $("#chartDateEvtMal", thisView.el).text(thisView.chartDate[0] + " ~ " + thisView.chartDate[1]);
            // 테이블 조회 
            thisView.eventChartCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectStatEvent',
                contentType: 'application/json',
                data: JSON.stringify(eventChartParam),
                async: false
            });

            var statProtocolChartParam = {
                tmstart: thisView.chartDate[0],
                tmend: thisView.chartDate[1],
                timeHour: thisView.timeHour,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };
            thisView.statProtocolChartCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectStatTraffic',
                contentType: 'application/json',
                data: JSON.stringify(statProtocolChartParam),
                async: false
            });
            
            var tmstartStr = thisView.eventChartCollection.pluck("tmstart");
        	var eventDblbpsStr = thisView.statProtocolChartCollection.pluck("eventDblbps");
        	var eventCountStr = thisView.eventChartCollection.pluck("eventCount");
        	
            //유해트래픽/공격횟수 그래프 그리기
            thisView.resultTotMalEvt = "Time,EventBps,EventCount\n";
            for (var i = 0; i < thisView.eventChartCollection.length; i++) {
                if (i != thisView.eventChartCollection.length - 1) {
                    thisView.resultTotMalEvt += tmstartStr[i] + "," + eventDblbpsStr[i] + "," + eventCountStr[i] + "\n";
                } else {
                    thisView.resultTotMalEvt += tmstartStr[i] + "," + eventDblbpsStr[i] + "," + eventCountStr[i];
                }
            }
        },
        makeTotMalTrafficChart: function (collection) {
            var thisView = this;
            Backbone.ChartView.MakeChart(thisView.$('#totMalTrafficChart', thisView.el), {
                type: "LinePlusLine",
                title: locale.dashboard.totalTrafficEventTraffic,
                chartName: "TotMalTraffic",
                collection: collection,
                svgMainMarginTop: 20,
                svgMainMarginRight: 45,
                svgMainMarginBottom: 25,
                svgMainMarginLeft: 40,
                chartHeight: 220
            });
        },
        //공격횟수/유해트래픽 그래프
        makeMalTrafficEventChart: function (collection) {
            var thisView = this;
            Backbone.ChartView.MakeChart(thisView.$('#malTrafficEventChart', thisView.el), {
                //type: "LinePlusBar",
                type: "LinePlusLineWithArea",
                title: locale.dashboard.eventCountEventTraffic,
                chartName: "MalTrafficEvent",
                collection: collection,
                svgMainMarginTop: 20,
                svgMainMarginRight: 45,
                svgMainMarginBottom: 25,
                svgMainMarginLeft: 40,
                chartHeight: 220
            });
        },
        /////// 프로토콜 별 추이그래프  
        getProtocolGraphData: function () {
            var thisView = this;
            //조건 설정
            var protocolChartParam = {
                tmstart: thisView.chartDate[0],
                tmend: thisView.chartDate[1],
                timeHour: thisView.timeHour,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };

            // 테이블 조회 
            thisView.protocolChartCollection.fetch({
                method: "POST",
                url: 'api/dashboard/protocolsTraffic',
                contentType: 'application/json',
                data: JSON.stringify(protocolChartParam),
                async: false,
                success: function (protocolChartCollection) {
                    thisView.makeProtocolChart(protocolChartCollection);
                }
            });

            $("#chartDateProtocol", thisView.el).text(thisView.chartDate[0] + " ~ " + thisView.chartDate[1]);
        },
        /**
         * 프로토콜 추이 그래프
         * @author LEE KYUNGHEE 
         * @since 2016 04
         * @description   
         */
        makeProtocolChart: function (protocolChartCollection) {

            $(".protocol").empty();
            var thisView = this;
            var groupBy = protocolChartCollection.groupBy('protocolName');
            this.allChartData = [];
            this.color = ['#92DCCC', '#8abb6f', '#3498db', '#9b59b6', '#B2EBF4', '#898989'];

            var j = 0;
            for (var key in groupBy) {
                var self = this;
                var group = groupBy[key];
                var resultData = {};
                resultData["key"] = j;
                resultData["color"] = self.color[j++];
                resultData["strName"] = group[0].get('protocolName');
                resultData.values = [];
                for (var i = 0; i < group.length; i++) {
                    var model = group[i];
                    resultData.values.push({
                        "time": thisView.parseDate(model.get('tmstart')),
                        "value": model.get('dblbps')
                    });
                    if (resultData.color == this.color[i]) {
                        $(".protocol").append('<span class="protocol-level' + i + ' margin-l10"></span><span">' + key + '</span>');
                    }
                }
                this.allChartData.push(resultData);
                this.allChartData = this.sortByKey(this.allChartData, "key");
            }
            Backbone.ChartView.MakeChart(this.$('#protocolChart', thisView.el), {
                type: "StackedArea",
                title: locale.dashboard.protocol,
                chartName: "protocol",
                collection: this.allChartData,
                dataType: "bps",
                svgMainMarginTop: 20,
                svgMainMarginRight: 10,
                svgMainMarginBottom: 25,
                svgMainMarginLeft: 40,
                chartHeight: 220
            });
        },
        sortByKey: function (array, key) {
            return array.sort(function (a, b) {
                var x = a[key];
                var y = b[key];
                return((x > y) ? -1 : ((x < y) ? 1 : 0));
            });
        },
        //Application TOP5
        makeApplicationTopNTable: function () {
            var thisView = this;
            var applicationCollection = new ApplicationCollection();
            var appLayerParam = {
                tableDate: thisView.tableDate,
                startDateInput: thisView.tableDateSel[0], // 리스트 조회 시간 5분에서 12시간으로 변경  		
                endDateInput: thisView.tableDateSel[1],
                topN: thisView.topN,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor,
                ipType: 4
            };

            $("#listDateApplicationTop", thisView.el).text(thisView.tableDateSel[0] + " ~ " + thisView.tableDateSel[1]);

            // 테이블 조회 
            applicationCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectApplicationTopN',
                contentType: 'application/json',
                data: JSON.stringify(appLayerParam),
                success: function (applicationCollection) {
                    Backbone.TableView.MakeTable(thisView.$('#tableApplicationTopN', thisView.el), {
                        type: "Application",
                        title: locale.dashboard.applicationTopN,
                        titleSub: locale.dashboard.target,
                        headerName: [locale.type, locale.count, locale.rate],
                        collection: applicationCollection,
                        startDateInput: thisView.tableDateSel[0],
                        endDateInput: thisView.tableDateSel[1],
                        startDateGraphInput: thisView.chartDate[0],
                        endDateGraphInput: thisView.chartDate[1],
                        lnetgroupIndex: thisView.selNetworkGroup,
                        lnetworkIndex: thisView.selNetwork,
                        lvsensorIndex: thisView.selVsensor,
                        lsensorIndex: thisView.selSensor,
                        pathName: thisView.targetPathName
                    });
                }
            });
        },
        //서비스 트래픽 TOP5
        makeServiceTopNTable: function() {
            var thisView = this;
            // 콜렉션 생성 
            var serviceTopNCollection = new ServiceTopNCollection();

            //조건 설정
            var serviceParam = {
                tableDate: thisView.tableDate,
                tmstart: thisView.tableDateSel[0], // 리스트 조회 시간 5분에서 12시간으로 변경  		
                tmend: thisView.tableDateSel[1],
                topN: thisView.topN,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };
            $("#listDateServiceTop", thisView.el).text(thisView.tableDateSel[0] + " ~ " + thisView.tableDateSel[1]);

            // 테이블 조회 
            serviceTopNCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectServiceTopN',
                contentType: 'application/json',
                data: JSON.stringify(serviceParam),
                success: function () {
                    Backbone.TableView.MakeTable(thisView.$('#tableServiceTopN', thisView.el), {
                        type: "Service",
                        title: locale.dashboard.serviceTopN,
                        titleSub: locale.dashboard.target,
                        headerName: [locale.dashboard.serviceType, locale.dashboard.serviceTraffic],
                        collection: serviceTopNCollection,
                        startDateInput: thisView.tableDateSel[0],
                        endDateInput: thisView.tableDateSel[1],
                        startDateGraphInput: thisView.chartDate[0],
                        endDateGraphInput: thisView.chartDate[1],
                        lnetgroupIndex: thisView.selNetworkGroup,
                        lnetworkIndex: thisView.selNetwork,
                        lvsensorIndex: thisView.selVsensor,
                        lsensorIndex: thisView.selSensor,
                        pathName: thisView.targetPathName
                    });
                }
            });
        },
        //침입탐지(공격) TOP5
        makeEventTopNTable: function () {
            var thisView = this;
            // 콜렉션 생성 
            var eventTopNCollection = new EventTopNCollection();

            //조건 설정
            var eventParam = {
                tableDate: thisView.tableDate,
                tmstart: thisView.tableDateSel[0],
                tmend: thisView.tableDateSel[1],
                topN: thisView.topN,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };
            $("#listDateEventTop", thisView.el).text(thisView.tableDateSel[0] + " ~ " + thisView.tableDateSel[1]);

            // 테이블 조회 
            eventTopNCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectEventTopN',
                contentType: 'application/json',
                data: JSON.stringify(eventParam),
                success: function () {
                    Backbone.TableView.MakeTable(thisView.$('#tableEventTopN', thisView.el), {
                        type: "Event",
                        title: locale.dashboard.eventTopN,
                        titleSub: locale.dashboard.target,
                        headerName: [locale.dashboard.eventType, locale.dashboard.eventCount],
                        collection: eventTopNCollection,
                        startDateInput: thisView.tableDateSel[0],
                        endDateInput: thisView.tableDateSel[1],
                        startDateGraphInput: thisView.chartDate[0],
                        endDateGraphInput: thisView.chartDate[1],
                        lnetgroupIndex: thisView.selNetworkGroup,
                        lnetworkIndex: thisView.selNetwork,
                        lvsensorIndex: thisView.selVsensor,
                        lsensorIndex: thisView.selSensor,
                        pathName: thisView.targetPathName
                    });
                }
            });
        },
        //감사로그 최근
        makeAuditTopNTable: function () {
            var thisView = this;
            // 콜렉션 생성 
            var auditTopNCollection = new AuditTopNCollection();
            //조건 설정
            var auditParam = {
                topN: thisView.topN,
                tableDate: thisView.tableNotAnalysisDate,
                tmstart: thisView.tableNotAnalysisDateSel[0],
                tmend: thisView.tableNotAnalysisDateSel[1]
            };
            $("#listDateAudit", thisView.el).text(thisView.tableNotAnalysisDateSel[0] + " ~ " + thisView.tableNotAnalysisDateSel[1]);
            
            // 테이블 조회 
            auditTopNCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectAuditTopN',
                contentType: 'application/json',
                data: JSON.stringify(auditParam),
                success: function() {
                    Backbone.TableView.MakeTable(thisView.$('#tableAuditTopN', thisView.el), {
                        type: "Audit",
                        title: locale.dashboard.auditTopN,
                        titleSub: locale.dashboard.auditTarget,
                        collection: auditTopNCollection,
                        startDateInput: thisView.tableDateSel[0],
                        endDateInput: thisView.tableDateSel[1]
                    });
                }
            });
        },
        //피해자IP TOP5
        makeVictimIpTopNTable: function() {
            var thisView = this;
            // 콜렉션 생성 
            var victimIpTopNCollection = new VictimIpTopNCollection();

            //조건 설정
            var victimIpParam = {
                tableDate: thisView.tableDate,
                tmstart: thisView.tableDateSel[0],
                tmend: thisView.tableDateSel[1],
                topN: thisView.topN,
                lnetgroupIndex: thisView.selNetworkGroup,
                lnetworkIndex: thisView.selNetwork,
                lvsensorIndex: thisView.selVsensor,
                lsensorIndex: thisView.selSensor
            };
            $("#listDateVictimIpTop", thisView.el).text(thisView.tableDateSel[0] + " ~ " + thisView.tableDateSel[1]);

            // 테이블 조회 
            victimIpTopNCollection.fetch({
                method: "POST",
                url: 'api/dashboard/selectVictimIpTopN',
                contentType: 'application/json',
                data: JSON.stringify(victimIpParam),
                success: function() {
                    Backbone.TableView.MakeTable(thisView.$('#tableVictimIpTopN', thisView.el), {
                        type: "VictimIp",
                        title: locale.dashboard.victimIpTopN,
                        titleSub: locale.dashboard.target,
                        headerName: [locale.dashboard.victimIpType, locale.dashboard.eventCount, locale.dashboard.serviceTraffic],
                        collection: victimIpTopNCollection,
                        startDateInput: thisView.tableDateSel[0],
                        endDateInput: thisView.tableDateSel[1],
                        startDateGraphInput: thisView.chartDate[0],
                        endDateGraphInput: thisView.chartDate[1],
                        lnetgroupIndex: thisView.selNetworkGroup,
                        lnetworkIndex: thisView.selNetwork,
                        lvsensorIndex: thisView.selVsensor,
                        lsensorIndex: thisView.selSensor,
                        pathName: thisView.targetPathName
                    });
                },
                async: false
            });

            var labelName = victimIpTopNCollection.pluck("dwDestinationIp");
            var nSumValue = victimIpTopNCollection.pluck("nSum");
            var totalNSumValue = victimIpTopNCollection.pluck("totalNSum");

            var totalNSumValueFin = totalNSumValue[0]; // 피해IP Top N SUM

            thisView.resultVictimIpTopN = "label,value\n";

            for (var i = 0; i < victimIpTopNCollection.length; i++) {
                thisView.resultVictimIpTopN += labelName[i] + "," + dataExpression.getFormatPercent(nSumValue[i], totalNSumValueFin) + "\n";
            }
        },
        makeVictimIpChart: function(collection) {
            var thisView = this;
            Backbone.ChartView.MakeChart(thisView.$('#victimIpChart', thisView.el), {
                type: "MultiPieDonut",
                title: locale.dashboard.trafficEventCount,
                titleSub: locale.dashboard.targetOrg,
                chartName: "victimIpChart",
                collection: collection,
                svgMainMarginTop: 30,
                svgMainMarginRight: 50,
                svgMainMarginBottom: 35,
                svgMainMarginLeft: 50
            });
        },
        removeVictimIpChartTooltip: function(collection) {
            $('.chart_tooltip').remove();
        },
    });

});
