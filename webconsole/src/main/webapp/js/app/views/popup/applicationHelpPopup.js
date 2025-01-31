/*
 * 
 */
define(function(require) {
	
	"use strict";
	
	var $ 							= require('jquery'),
		Backbone 					= require('backbone'),
		locale 						= require('i18n!nls/str'),
		dataExpression 				= require('utils/dataExpression'),
		ApplicationHelpPopupModel 	= require('models/detectionAnalysis/applicationPopupModel'),
		TbIpRetrieve 				= require('views/tools/ipRetrievePopup');

	// require template
	var Tpl 						= require('text!tpl/detectionAnalysis/applicationDefinition.html');
	
	return Backbone.View.extend({
		className: 'tab-pane padding-r15 padding-l15',
		template: _.template(Tpl),
		
		initialize: function(options) {
			this.menuType 			= options.menuType;
			this.monitoringModel 	= options.monitoringModel;
			this.model 				= new ApplicationHelpPopupModel();
			this.applicationModel 	= new ApplicationHelpPopupModel();
			this.searchCondition 	= options.searchCondition;
			this.listModel 			= options.model;
			this.type 				= options.type;
		},
		events: {
			'click .sipRetrievePopupBtn' : 'showsIpRetrievePopup',
			'click .dipRetrievePopupBtn' : 'showdIpRetrievePopup'
		},
		render: function() {
			this.$el.html(this.template({
				locale : locale,
				model : this.searchCondition
			}));
			if(this.menuType == "ApplicationDetectionMonitoring") {
				this.setMonitoringDefinition();
			} else if(this.menuType == "ApplicationDetection") {
				//this.applicationDefinition();
				this.setMonitoringDefinition();
			}
			return this;
		},
		applicationDefinition: function() {
			var thisView = this;
			
			_.extend(this.searchCondition, this.listModel.toJSON(), {
				strDestIp : this.listModel.get('deDestinationIp'),
				strSrcIp : this.listModel.get('dwSourceIp')
			});
			
			this.applicationModel.fetch({
				method: 'POST',
				url: 'api/detectionAnalysis/selectApplicationHelpPopupList',
				contentType: 'application/json',
				async: false,
				data: JSON.stringify(this.searchCondition),
				success: function(model) {
					thisView.setDetectionAttackHelp(model);
				}
			});
		},
		setDetectionAttackHelp: function(model) {
			var strSrcIp = model.get('bIpType') == 4 ? model.get('dwSourceIp') : model.get('strSourceIp');
			var strDestIp = model.get('bIpType') == 4 ? model.get('deDestinationIp') : model.get('strDestinationIp');
			
			$('#bTypeName', this.el).append(dataExpression.getbTypeName(model.get('bType')));
			$('#tmLogTime', this.el).append(model.get('tmLogTime'));
			$('#bType', this.el).append(dataExpression.getbTypeName(model.get('bType')));
			$('#sip', this.el).append(strSrcIp);
			$('#dip', this.el).append(strDestIp);
			$('#sPort', this.el).append(model.get('nSourcePort'));
			$('#dPort', this.el).append(model.get('nDestinationPort'));
			$('#vsensorName', this.el).append(model.get('vsensorName'));
			$('#sensorName', this.el).append(model.get('sensorName'));
                        if (model.get('sData') == null || model.get('sData') == '') {
                            $('#sdataTitle', this.el).hide();
                            $('#sdataContent', this.el).hide();
                        } else {
                            $('#sdataTitle', this.el).show();
                            $('#sdataContent', this.el).show();
                            $("#sData", this.el).append(model.get('sData'));
                        }
		},
		setMonitoringDefinition: function() {
//			var strSrcIp = this.monitoringModel.get('bIpType') == 4 ? this.monitoringModel.get('dwSourceIp') : this.monitoringModel.get('strSourceIp');
//			var strDestIp = this.monitoringModel.get('bIpType') == 4 ? this.monitoringModel.get('destinationIp') : this.monitoringModel.get('strDestinationIp');
//			
			var strSrcIp = this.monitoringModel.get('bIpType') == 4 ? this.monitoringModel.get('dwSourceIp') : this.monitoringModel.get('strSourceIp');
			var strDestIp = this.monitoringModel.get('bIpType') == 4 ? this.monitoringModel.get('deDestinationIp') : this.monitoringModel.get('strDestinationIp');
			
			$('#bTypeName', this.el).append(dataExpression.getbTypeName(this.monitoringModel.get('bType')));
			$('#tmLogTime', this.el).append(this.monitoringModel.get('tmLogTime'));
			$('#bType', this.el).append(dataExpression.getbTypeName(this.monitoringModel.get('bType')));
			$('#sip', this.el).append(strSrcIp);
			$('#dip', this.el).append(strDestIp);
			$('#sPort', this.el).append(this.monitoringModel.get('nSourcePort'));
			$('#dPort', this.el).append(this.monitoringModel.get('nDestinationPort'));
			$('#vsensorName', this.el).append(this.monitoringModel.get('vsensorName'));
			$('#sensorName', this.el).append(this.monitoringModel.get('sensorName'));
//			$("#sData", this.el).append(this.monitoringModel.get('sData'));
                        
                    if (this.monitoringModel.get('sData') == null || this.monitoringModel.get('sData') == '') {
                            $('#sdataTitle', this.el).hide();
                            $('#sdataContent', this.el).hide();
                        } else {
                            $('#sdataTitle', this.el).show();
                            $('#sdataContent', this.el).show();
                            $("#sData", this.el).append(this.monitoringModel.get('sData'));
                        }
		},
		showsIpRetrievePopup: function() {
			
//			if(this.menuType == "ApplicationDetectionMonitoring") {
//				var ipValue =  this.monitoringModel.get('dwSourceIp');
//			} else if(this.menuType == "ApplicationDetection") {
//				var ipValue =  this.applicationModel.get('dwSourceIp');
//			}
			var ipValue = $('#sip', this.el).text();
		
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ipValue//this.ipValue //this.applicationModel.get('dwSourceIp') 
				})
			});
		},
		showdIpRetrievePopup: function() {
			
//			if(this.menuType == "ApplicationDetectionMonitoring") {
//				this.ipValue =  this.monitoringModel.get('deDestinationIp');
//			} else if(this.menuType == "ApplicationDetection") {
//				this.ipValue =  this.applicationModel.get('deDestinationIp');
//			}
			var ipValue = $('#dip', this.el).text();
			
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + locale.retrieve,
				body: new TbIpRetrieve({
					ip : ipValue//this.ipValue //this.applicationModel.get('deDestinationIp')
				})
			});
		}
	});
});