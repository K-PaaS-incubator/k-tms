/**
 * Pagination
 *
 * paginationType 종류
 * 1. readMorePaginator (더보기)
 * 2. basicPaginator ( < 1 2 3 ... 6 7 > )
 *
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            alertMessage = require('utils/AlertMessage'),
            errorLocale = require('i18n!nls/error'),
            locale = require('i18n!nls/str');

    var readMoreTpl = require('text!tpl/common/readMorePagination.html'),
            basicPaginationTpl = require('text!tpl/common/basicPagination.html');

    var Pagination = Backbone.View.extend({
        readMoreTemplate: _.template(readMoreTpl),
        basicPaginationTemplate: _.template(basicPaginationTpl),
        maxRowSize : '500',
        initialize: function (options) {
            this.startRowSize = 0; 	// 쿼리 리스트범위조회 시작조건 값
            this.endRowSize = 0; 	// 쿼리 리스트범위조회 끝조건 값
            this.totalRowSize = null; // 쿼리 리스트 총 개수 값
            this.rowSize = 30; 		// 조회 리스트 개수값
            this.changeValue = 10; 	// 플러스버튼, 마이너스버튼 이벤트시 증가, 감소되는 값
            // 이벤트 적용1 
            // 이벤트를 등록한다. 
            //this.evt = options.evt;
        },
        events: {
            'click #rowSizePlusBtn': 'rowSizePlusBtn',
            'click #rowSizeMinusBtn': 'rowSizeMinusBtn',
            'keyup #listRowSizeInput': 'changeListRowSize',
            'click #backToTopBtn': 'backToTop',
            //'click #rowSizeAllBtn': 'rowSizeAllBtn',	// AllBtn : 리스트 총개수와 상관없이 최대 500건까지 조회 가능토록함 
            // 이벤트 적용2
            // 더보기 실행시 readMorePagination()을 호출
            //'click #readMoreBtn' : 'readMorePagination'
        },
        render: function () {

            $("[data-toggle='tooltip']", this.el).tooltip();

            return this;
        },
        renderPaginationType: function (paginationType) {

            var thisView = this;
            // paginationType에 따라 template를 달리한다.
            if (paginationType === 'readMorePaginator') {
                thisView.$el.html(this.readMoreTemplate({locale:locale}));
                $('#listRowSizeInput', this.el).val(this.rowSize);
            } else if (paginationType === 'basicPaginator') {
                thisView.$el.html(this.basicPaginationTemplate({locale:locale}));
            }
            return thisView;
        },
        rowSizeAllBtn: function () {
            // this.totalRowSize가 500 이상일경우 All버튼을 누르면 500까지만 보여줌
            if (this.totalRowSize > this.maxRowSize) {
                $('#listRowSizeInput', this.el).val(this.maxRowSize);
                this.rowSize = this.maxRowSize;
            } else {
                $('#listRowSizeInput', this.el).val(this.totalRowSize);
                this.rowSize = this.totalRowSize;
            }
            Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), true);
        },
        rowSizePlusBtn: function () {
            //숫자가 아닌 다른 문자열이 들어갔을경우 확인  
            if ($('#listRowSizeInput', this.el).val() != 0) {
                this.changeListRowSize();
            }
            // this.rowSize input 에 +10을 함
            var listCountValue = parseInt($('#listRowSizeInput', this.el).val()) + parseInt(this.changeValue);
            if (listCountValue > this.maxRowSize) {
                listCountValue = this.maxRowSize;
            }
            var result = Backbone.Utils.validation.validateNativeNumber(listCountValue);
            if (result != true) {
                this.rowSize = 0;
                $('#listRowSizeInput', this.el).val(listCountValue);
                Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), result);
                return false;
            } else {

                Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), result);
                $('#listRowSizeInput', this.el).val(listCountValue);
                this.rowSize = listCountValue;
            }
        },
        rowSizeMinusBtn: function () {
            // 숫자가 아닌 다른 문자열이 들어갔을경우 확인
            this.changeListRowSize();
            // this.rowSize input 에 -10을 함
            var listCountValue = parseInt($('#listRowSizeInput', this.el).val()) - parseInt(this.changeValue);

            // 음수 값 체크 
            var resultValue = Backbone.Utils.validation.validateNativeNumber(listCountValue);
            if (resultValue == true) {
                $('#listRowSizeInput', this.el).val(listCountValue);
                this.rowSize = listCountValue;

                Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), resultValue);
            } else {
                $('#listRowSizeInput', this.el).val(0);
                this.rowSize = 0;
                Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), resultValue);

                return false;
            }

        },
        changeListRowSize: function () {

            var listCountValue = $('#listRowSizeInput', this.el).val();
            // 숫자인지 아닌지 (0부터 무한대 체크)
            // 2018.03.06 Excel Download 기능오류로 인해 무한대-> 500 count 체크 추가
            // validateNumber 함수내 문자열 정규식 체크기능으로 인한 문자열로 선언
            if(Number(listCountValue) > this.maxRowSize){
            	listCountValue = "500";
            }
            var result = Backbone.Utils.validation.validateNumber(listCountValue);
            if (result != true) {
                this.rowSize = 0;
                Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), result);
                return false;

            } else {
                var zeroCheckResult = Backbone.Utils.validation.validateNativeNumber(listCountValue);
                if (zeroCheckResult != true) {
                    this.rowSize = 0;
                    Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), zeroCheckResult);
                    return false;

                } else {
                    Backbone.Utils.Tip.validationTooltip($('#listRowSizeInput', this.el), zeroCheckResult);
                    this.rowSize = listCountValue;
                }
            }
            $('#listRowSizeInput', this.el).val(listCountValue);//input 값변경
            

        },
        backToTop: function () {
            $('#content, html').animate({
                scrollTop: 0
            }, 500);
        },
        hidePaginationView: function () {
            $('#readMoreBtn', this.el).css("visibility", "hidden");
            $('#listRowSizeInput', this.el).css("visibility", "hidden");
            $('#rowSizePlusBtn', this.el).css("visibility", "hidden");
            $('#rowSizeMinusBtn', this.el).css("visibility", "hidden");
            $('#rowSizeAllBtn', this.el).css("visibility", "hidden");
            $("#backToTopBtn", this.el).css("visibility", "hidden");
        },
        showPaginationView: function () {

            $('#readMoreBtn', this.el).css("visibility", "");
            $('#listRowSizeInput', this.el).css("visibility", "");
            $('#rowSizePlusBtn', this.el).css("visibility", "");
            $('#rowSizeMinusBtn', this.el).css("visibility", "");
            $('#rowSizeAllBtn', this.el).css("visibility", "");
            $("#backToTopBtn", this.el).css("visibility", "");
        },
        getPaginationType: function (paginationType) {
            return {
                'type': this.renderPaginationType(paginationType)
            };
        },
        setTotalRowSize: function (setTotalRowSize1) {
            this.totalRowSize = parseInt(setTotalRowSize1);
            this.showPaginationView();

            if (parseInt(this.totalRowSize) == parseInt(0)) {
                this.startRowSize = 0;
                this.endRowSize = 0;

                var thisView = this;
                thisView.hidePaginationView();

            } else if (parseInt(this.totalRowSize) <= parseInt(this.endRowSize)) {
                var thisView = this;
                thisView.hidePaginationView();
            }
        },
        readMorePagination: function () {
            var thisView = this;
            thisView.showPaginationView();
            if (this.totalRowSize != 0 && this.totalRowSize != null) {
                if (this.rowSize == 0) {
                    alertMessage.infoMessage(errorLocale.validation.inputErrorMsg, 'info', '', 'small');
                    return false;

                } else {

                    this.startRowSize = parseInt(this.endRowSize);
                    this.endRowSize = parseInt(this.endRowSize) + parseInt(this.rowSize);
                    if (this.endRowSize >= this.totalRowSize) {
                        this.endRowSize = this.totalRowSize;
                        thisView.hidePaginationView();
                    }
                }
            } else if (this.totalRowSize == null) {
                // 최초 리스트를 조회할때 필요한 endRowSize, startRowSize
                this.startRowSize = parseInt(0);

                this.endRowSize = parseInt(this.rowSize);

                this.totalRowSize = null;

            }

            return {
                'endRowSize': this.endRowSize,
                'startRowSize': this.startRowSize,
                'rowSize': this.rowSize
            };

            // 이벤트 적용3
            //			this.evt.trigger('pagination', {
            //				'endRowSize': this.endRowSize,
            //				'startRowSize': this.startRowSize
            //			});
        },
        setInitialization: function () {

            this.totalRowSize = null;
            this.endRowSize = 0;
            this.startRowSize = 0;
//			if($('#listRowSizeInput', this.el).val() > 0){
//				this.rowSize = $('#listRowSizeInput', this.el).val();
//			}else{
            this.rowSize = 30;
            $('#listRowSizeInput', this.el).val(this.rowSize);
//			}

        },
        getPaginationRowValue: function () {

            this.startRowSize = 0;
            this.endRowSize = this.rowSize;

            return {
                'startRowSize': this.startRowSize,
                'endRowSize': this.endRowSize,
                'rowSize': this.rowSize
            };

        },
        //최대 노출갯수 시 호출(이전 뷰는 초기화)
        setMaxListPageValue : function(limit){
        	if(limit < this.endRowSize){
        		this.startRowSize = this.endRowSize - limit;
        	}
        }
    });

    return Pagination;

});
