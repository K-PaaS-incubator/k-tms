/**
 *  IP Tab View
 */
define(function (require) {
    "use strict";

    // require library
    var $ 					= require('jquery'),
        _ 					= require('underscore'),
        Backbone 			= require('backbone'),
        dataExpression 		= require('utils/dataExpression'),
        Pagination 			= require('views/common/pagination');

    // require locale
    var locale 				= require('i18n!nls/str');

    // require model, collection, view
    var IpModel 			= require('models/trafficAnalysis/ipModel'),
        IpCollection 		= require('collections/trafficAnalysis/ipCollection'),
        IpItem 				= require('views/trafficAnalysis/ip/ipItem'),
        ChartFlow 			= require('views/common/chartFlow'),
        ExportView 			= require('views/common/export');

    // require template
    var tabViewTpl 			= require('text!tpl/trafficAnalysis/ipTab.html');

    var intervalFlag 		= true;
    var refreshTime 		= 60000 * 5;

    return Backbone.View.extend({
        paging: '',
        tabBoxTemplate: _.template(tabViewTpl),
        initialize: function(options) {

            this.ipCollection 		= new IpCollection();
            this.ipModel 			= new IpModel();
            this.pagination 		= new Pagination();
            this.exportCollection 	= new IpCollection();	
            this.exportTotalModel 	= new IpModel();		

            this.chartFlow;
            this.defaultTimeModel;
            this.listType = options.listType;
            this.evt = options.evt;

//			_.bindAll(this, "onClose", "callMakeGraph", "removeItemViews", "retrieve", "customOn", "customOff", "render", "getListData", "getTotalData", "setTotalData", "addOne", "addAll");
            _.bindAll(this, "onClose", "callMakeGraph", "removeItemViews", "retrieve", "customOn", "customOff", "render", "getListData", "getTotalData", "setTotalData", "addOne");

            this.customOn();

            this.listenTo(this.ipModel, 'change', this.setTotalData);
//			this.listenTo(this.ipCollection, 'add', this.addOne);
//			this.listenTo(this.ipCollection, 'reset', this.addAll);
 
        },
        onClose: function() {
            //this.removeItemViews();
            this.customOff();
        },
        events: {
            'click #readMoreBtn'	: 'getListData',
            'click .btnGraph'		: 'callMakeGraph',
            'click .btnExport'		: 'callMakeExport'
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

            _.extend(paging, {"startRowSize": 0, "endRowSize": this.pagination.endRowSize, "rowSize":(this.pagination.endRowSize)});
            var listParam = $.extend(paging, self.model.toJSON());

            self.exportCollection.fetch({	
                method: 'POST',
                data: JSON.stringify(listParam),
                url: "api/trafficAnalysis_ipList",
                contentType: 'application/json',
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.progressbar', self.el));
                },
                success: function (collection) {
                    self.model.set('isDownload', '');
                    if (self.exportCollection.length > 0) {		
                        self.getExportTotal(templateFile, listParam);
                    }
                },
                error: function (message) {
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
                url: "api/trafficAnalysis_ipTotal",
                contentType: 'application/json',
                data: JSON.stringify(totalParam),
                reset: true,
                success: function (model) {
                    self.excuteExport(templateFile, listParam, self.exportTotalModel);	
                },
            });
        },
        // export UI 세팅
        excuteExport: function(templateFile, listParam, exportTotalModel) {
            var self = this;

            var contentTable = document.createElement('table');
            $(contentTable).append('<tr>' +
                    '<td class=\"content-table-header\" style=\"width:100px;\">' + locale.rank + '</td>' +
                    '<td class=\"content-table-header\" >' + 'IP' + '</td>' +
                    '<td class=\"content-table-header\" >' + 'bps' + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.rate + ' (%)' + '</td>' +
                    '<td class=\"content-table-header\" >' + 'pps' + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.rate + ' (%)' + '</td>' +
                    '</tr>'+
                    '<tr>'+
                    '<td class=\"total-table-data\" >' + locale.whole + '</td>' +
                    '<td class=\"total-table-data\" >' + '' + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('bps')) + '</td>' +
                    '<td class=\"total-table-data\" >' + '' + '</td>' +
                    '<td class=\"total-table-data\" >' + dataExpression.getFormatTrafficData(self.exportTotalModel.get('pps')) + '</td>' +
                    '<td class=\"total-table-data\" >' + '' + '</td>' +
                    '</tr>');
            var forMat = [];
            forMat[0] = "bps";
            forMat[1] = "pps";

            new ExportView({
            	menuOptions: 'ip',
                templateFile: templateFile, 				// 공통
                docTitle: 'IP', 							// file 명 (출력메뉴)
                requireModel: new IpModel(), 				// 리스트 모델			
                listParam: listParam, 						// 선택 조건 
                //fetchCollection: self.exportCollection, 	// 리스트 데이터
                fetchCollection: self.ipCollection, 		// 리스트 데이터		
                contentTable: contentTable, 				// 리스트
                totalColomn: 6, 							// 테이블 총 컬럼			
                listKeys: new Array(_.pick(new IpModel().attributes), 
                		'rNum', 							// Model에서 출력에 사용되는 key  
                        'ip', 								// IP
                        'bps', 								// BPS
                        'sumBps', 							// 점유율
                        'pps', 								// PPS
                        'sumPps'							// 점유율 
                        ),
                forMat: forMat
            }).makeFile();
        },
        callMakeGraph: function() {
            this.chartFlow.makeGraph();
        },
        customOn: function() {
            this.evt.bind("retrieve", this.retrieve);
        },
        customOff: function() {
            this.evt.unbind("retrieve");
        },
        render: function() {
            var self = this;
            this.$el.html(self.tabBoxTemplate({
                locale: locale
            }));

            this.paginationBasicInfo = this.pagination.getPaginationType('readMorePaginator');
            $('#pageLayout', self.el).append(this.paginationBasicInfo.type.el);

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
                chartName: "ip",
                chartUrl: "api/trafficAnalysis/selectIpChart",
                parentEl: this.el,
                dataType: "bps"
            });
            $("#chart", this.el).append(this.chartFlow.el);
            this.chartFlow.render();
        },
        getListData: function() {
            var self = this;

            self.paging = this.pagination.readMorePagination();
            if (!self.paging) {
                return;
            }
            var listParam = $.extend(self.paging, this.model.toJSON());
            var reset = true;

            $("#listDate", self.el).text(this.model.get('startDateInput') + " ~ " + this.model.get('endDateInput'));
            
            // 그래프 체크박스 초기화
            if (self.paging.startRowSize == 0) {
                $("#graphItemCheckTotal", this.el).eq(0).prop('checked', false);
                for (var i = 0; i < 5; i++) {
                    $("input[name='graphItemCheck']", this.el).eq(i).attr('checked', false);
                }
                self.reset = true;
            } else {
                self.reset = false;
            }

            self.ipCollection.fetch({
                method: 'POST',
                url: 'api/trafficAnalysis_ipList',
                contentType: 'application/json',
                data: JSON.stringify(listParam),
                reset: self.reset,
                beforeSend: function() {
                    Backbone.Loading.setLoading($('body'));
                    $('#pageLayout', self.el).hide();
                },
                success: function (data) {
                    /** 리스트 구하고 차트 그리던 순서를 변경함. 
                     * why ? 전체값에 대한 각 트래픽의 비율을 구하기 위해  
                     * 과거 : 리스트 조회, 리스트 어팬드 (collection의 add Event사용) -> 차트 -> 전체값 조회 및 어팬드
                     * 변경 : 리스트 조회 -> 전체값 조회 및 어팬드  -> 리스트 어팬드 -> 차트  
                     **/
                    if (data.length > 0) {
                        $('.btnExport').attr('disabled', false);
                        $('input:radio[name="exportRange"]').attr('disabled', false);
                        $('.btnGraph').attr('disabled', false);
                    } else {
                        $('.btnExport').attr('disabled', true);
                        $('input:radio[name="exportRange"]').attr('disabled', true);
                        $('.btnGraph').attr('disabled', true);
                    }
                    // 전체값 구하기 
                    self.getTotalData();
                },
                complete: function() {
                    if (self.ipCollection.length <= 0) {
                        Backbone.Loading.removeLoading($('body'));
                    }
                    $('#pageLayout', self.el).show();
                }
            });
        },
        getTotalData: function() {
            var self = this;
            if (self.ipCollection.length > 0) {
                self.$(".table").removeClass("nodata");
                this.pagination.setTotalRowSize(self.ipCollection.at(0).get('totalRowSize'));
                // 총 프로토콜수 
                $('#totalRowsize', self.el).text(self.ipCollection.at(0).get('totalRowSize'));
                
                $("#totalRowSize").digits();

                var listParam = this.model.toJSON();

                this.ipModel.fetch({
                    method: 'POST',
                    url: 'api/trafficAnalysis_ipTotal',
                    contentType: 'application/json',
                    data: JSON.stringify(listParam),
                    success: function (model) {
                        self.addOne();
                    },
                });
            } else {
                this.ipModel.clear();
                this.pagination.setTotalRowSize(0);
                this.$('#totalRowSize').text(0);
                this.$(".table").addClass("nodata");
            }
        },
        setTotalData: function() {
            var collectionLength = this.ipCollection.length;

            if (collectionLength > 0) {
                // 전체값 
                this.$('#totalBps').text(dataExpression.getFormatTrafficData(this.ipModel.get('bps')));
                this.$('#totalPps').text(dataExpression.getFormatTrafficData(this.ipModel.get('pps')));

            } else {
                // 전체값 BPS
                this.$('#totalBps').text(0);
                this.$('#totalPps').text(0);
            }
        },
        addOne: function() {
            var self = this;
//            console.log('this.model >>>>>>>>>> '+ JSON.stringify(this.model));
            for (var i = 0; self.ipCollection.length > i; i++) {
                self.ipCollection.at(i).set({
                    startDateInput: self.model.get('startDateInput'),
                    endDateInput: self.model.get('endDateInput'),
                    longIp: Backbone.Utils.Casting.convertIpToLong(self.ipCollection.at(i).get('ip')),
                    transformativeBps: dataExpression.getFormatTrafficData(self.ipCollection.at(i).get("bps")),
                    sumBps: dataExpression.getFormatPercent(self.ipCollection.at(i).get("bps"), self.ipModel.get('bps')),
                    transformativePps: dataExpression.getFormatTrafficData(self.ipCollection.at(i).get("pps")),
                    sumPps: dataExpression.getFormatPercent(self.ipCollection.at(i).get("pps"), self.ipModel.get('pps'))
                });

                var ipItem = new IpItem({
                    model: self.ipCollection.at(i),
                    searchModel: this.model,
                    parentEl: this.el
                });
                self.$('#ipListTbody').append(ipItem.render().el);
            }

            // 차트 그리기 
            if (self.ipCollection.length > 0) {
                self.$('#totalRowSize').text(self.ipCollection.at(0).get("totalRowSize"));
                $("#totalRowSize").digits();

                if (self.paging.startRowSize == 0) {
                    // 그래프 그리기 
                    // 전체 체크박스 체크
                    self.$("#graphItemCheckTotal").eq(0).prop('checked', true);
                    // 체크박스 TOP5 체크
                    for (var i = 0; i < 5; i++) {
                        if (i < self.ipCollection.length) {
                            self.$("input[name='graphItemCheck']").eq(i).attr('checked', true);
                            self.$("td[name='graphItemCheckTd']").eq(i).addClass('trend-blue-bg-lv' + (i + 1));

                        }
                    }
                    self.chartFlow.makeGraph();
                }
            } else {
                self.$('#totalRowSize').text(0);
            }
        },
        addAll: function() {
            var self = this;
            this.$("#ipListTbody").empty();
            this.ipCollection.each(this.addOne, self);
        }
    });
});