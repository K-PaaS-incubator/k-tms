/**
 *
 * target view
 */
define(function(require) {

	"use strict";

	// require library
	var $ 				= require('jquery'),
		Backbone 		= require('backbone'),
		analysisDate 	= require('utils/analysisDate'),
		alertMessage 	= require('utils/AlertMessage'),
		errorLocale 	= require('i18n!nls/error'),
		locale 			= require('i18n!nls/str');

	// require template
	var tpl 			= require('text!tpl/common/period.html');

	var PeriodView = Backbone.View.extend({
		template: _.template(tpl),
		events: {
			"change .startDateInput"	: "compareToDate",
			// "change .endDateInput"		: "compareToDate",
			"change .simpleTimeSelect"	: "setPeriod",									// 4. change 날짜 조건이 변경되었을때  setPeriod 메서드 호출
			"click .refreshBtn"			: "setPeriod"
		},
		initialize: function(options) {													// 1. 초기화 
			options = options || {};
			this.periodUnit = options.periodUnit || 1;
			this.changeMin = options.changeMin || 0;
			this.category = options.category;
			this.menuOptions = options.menuOptions;
		},
		render: function() {															// 2. render		
			this.$el.html(this.template({				
				locale: locale,
				category: this.category
			}));
			$('.simpleTimeSelect', this.el).trigger('change');							// 3. change 이벤트 
			return this;
		},
		compareToDate: function (e) {
			var timeBeforeChange = new Date(analysisDate.getAnalysisTimeInitialization())
			var timeTryToChange = new Date(e.currentTarget.value)
			if (timeTryToChange > timeBeforeChange) {
				$('.xdsoft_datetimepicker').hide()
				alertMessage.infoMessage(errorLocale.validation.dateInputErrorMsg, 'info', '', 'small');
				this.setPeriod()
			}
		},
		setPeriod: function() {															// 5. 날짜 조건 변경시 호출		
			var selDateType = $('select.simpleTimeSelect', this.el).val();				// 6. period.html > selectbox	
			
			Backbone.Calendar.setDefault($('.startDateInput', this.el), {
				step: this.periodUnit
			});
			Backbone.Calendar.setDefault($('.endDateInput', this.el), {
				step: this.periodUnit
			});
			
			var intervalTime;
			if(selDateType == 'detail') {												// 7. 상세선택	
				intervalTime = 12 * 60 * 60;											// 8. 날짜 선택시 및 최초 화면 진입
			} else {
				var type = selDateType.charAt(selDateType.length - 1);
				var period = selDateType.substring(0, selDateType.length - 1);
				
				switch (type) {
				case "m":
					intervalTime = period * 60;
					break;
				case "h":
					intervalTime = period * 60 * 60;
					break;
				case "d":
					intervalTime = period * 24 * 60 * 60;
					break;
				default:
					intervalTime = this.interval * 60;
					break;
				}
			}
			this.tableNotAnalysisDate = analysisDate.getAnalysisTimeInitialization();
			var date;
			if ( this.menuOptions != undefined && this.menuOptions == "AUDIT") {
				date = analysisDate.getNotAnalysisListTime(this.tableNotAnalysisDate, intervalTime, this.periodUnit);
			} else if ( this.menuOptions != undefined && this.menuOptions == "CHANGEMIN") {
				date = analysisDate.getAnalysisListTime(analysisDate.getChangeMinute(this.changeMin), intervalTime, this.periodUnit);
			} else {
				date = analysisDate.getAnalysisListTime(analysisDate.getAnalysisTimeInitialization(), intervalTime, this.periodUnit);
			}
			this.$('.startDateInput', this.el).val(date[0]);
			this.$('.endDateInput', this.el).val(date[1]);
                        
//			date = this.menuOptions != undefined && this.menuOptions == "AUDIT" ? analysisDate.getNotAnalysisListTime(this.tableNotAnalysisDate, intervalTime, this.periodUnit) : analysisDate.getAnalysisListTime(analysisDate.getAnalysisTimeInitialization(this.periodUnit), intervalTime, this.periodUnit);
//			var date = analysisDate.getAnalysisListTime(analysisDate.getAnalysisTimeInitialization(this.periodUnit), intervalTime, this.periodUnit);
//			this.$('.startDateInput', this.el).val(date[0]);
//			this.$('.endDateInput', this.el).val(date[1]);
		},
		getPeriod: function() {
			return {
				periodType: $('select.simpleTimeSelect', this.el).val(),
				startDate: $(".startDateInput", this.el).val(),
				endDate: $(".endDateInput", this.el).val()
			};
		},
		setDate: function(options) {
			$('.simpleTimeSelect', this.el).val(options.periodType);
			$('.simpleTimeSelect', this.el).trigger('change');
			
			$('.startDateInput', this.el).val(options.startDate);
			$('.endDateInput', this.el).val(options.endDate);
		}
	});

	PeriodView.makeUI = function($el, options) {			// 1. 호출
		options.periodUnit = options.periodUnit || 1;
		var periodView = new PeriodView({					// 2. 생성 -> initial
			el: $el,
			periodUnit: options.periodUnit,
			category: options.category,
			menuOptions: options.menuOptions,
                        changeMin: options.changeMin
		}).render();
		return periodView;
	};

	return PeriodView;
});
