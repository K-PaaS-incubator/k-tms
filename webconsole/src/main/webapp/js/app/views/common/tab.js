/**
 * Tab View 
 * Interface - render - addTab - removeTab
 * 초기 생성시 데이터로 탭이 완성
 * 유형 별 탭을 포함하는 유틸 구현 (옵션에 따라 탭 컨텐츠의 추가, 삭제가 가능 해야함) 
 * 탭컨텐츠별 이벤트를 처리해야함
 * 
 * var data = [
 * 	  {title:"title", viewObj: new View(), active: true, removable: false, icon:'info'}
 * ];
 * 
 * var tabs = new Tabs({
 *   el : '#content',  // tabView가 출력될 target 
 *   data : data       // tab의 초기 데이터
 * }); 
 *
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone'),
		underscore 	= require('underscore'),
		bootstrap 	= require('bootstrap'),
		locale 		= require('i18n!nls/str'),
		tmsjs 		= require('tmsjs');
	
	// require template
	var tpl = require('text!tpl/common/tab.html'),
		template = _.template(tpl);
	
	return Backbone.View.extend({
		headEl 		: '.nav.nav-tabs.nav-tabs-icon.float-clear',
		bodyEl		: '.tab-content',
		template	: _.template([
		        	  "<ul class='nav nav-tabs' role='tablist'>",
		        	  "<% _.each(tabs, function(tab) { %>",
		        	  "<%= headTemplate(_.extend(tab, {genId:generateId})) %>",
		        	  "<% }) %>",
		        	  "</ul>",
		        	  "<div id='tabContent' class='tab-content tabContent'>",
		        	  "<% _.each(tabs, function(tab) { %>",
		        	  "<%= bodyTemplate(_.extend(tab, {getId:getRemainId})) %>",
		        	  "<% }) %>",
		        	  "</div>"
		].join('')),
		headTemplate: _.template([
		              "<li <% if(active) { %> class='active' <%}%> >",
		              "<% if(hidden) { %>",
		              "<a href='#<%=genId()%>' role='tab' data-toggle='tab'>",
		              //"<span class='<%=icon%>'></span>",
		              "<% if(icon) { %> <span class='<%=icon%>'></span> <%}%>",
		              "<%= title %>",
		              "<% if(removable) { %> <button type='button' class='icon-tab-btn closetab'></button> <%}%>",
		              "</a>",
                              "<% if (WRITE_MODE == 1) { %>",
		              "<% if(changeable) { %> <li class='pull-right margin-r5' id='tabBtn'><button type='button' class='btn btn-default btn-sm margin-t5 changeBtn'>변경</button> <%}%>",
		              "<%}%>",
		              "<%}%>",
		              "</li>" 
		             
		].join('')),
		bodyTemplate: _.template([
		              "<div class='tab-pane <% if(active) { %> active <%}%>' id='<%=getId()%>'>",
		              "<div id='tab-content'></div>",
		              "</div>"
		].join('')),
		initialize: function(options) {
			this.el = options.el || '#tabs';
			this.prefix = this.el.replace(/^(#|\.)/,''); 	// this.el 에서 # 또는 .를 제거함. tabId 생성시 prefix로 사용됨.
			this.id = options.id || this.id;
			this.className = options.className || '';
			this.index = {i:0, r:0};	// i : index , r : remain(index 생성후 사용하지 않은 index 갯수)
			this.data = options.data || [];
			//this.tabs = options.tabs || [];
			this.tabs = $.map(this.data, function(v){
				return v.viewObj;
			});
		},
		events : {
			'click .closetab' : 'removeTab'
		},
		onClose : function() {
			var tab = null;
			// 탭뷰에 대한 이벤트 clear
			while((tab = this.tabs.pop()) != undefined) {
				tab.close(); 
			}
		},
		render: function() {
			var that = this;
			var view = this.template({
				tabs : this.data,
				headTemplate: this.headTemplate,
				bodyTemplate: this.bodyTemplate,
				generateId: _.bind(this.generateTabId, that),
				getRemainId: _.bind(this.getRemainTabId, that)
			});
			this.$el.html(view);
			
			var tabs = that.data;
			_.each(tabs, function(tab, i) {
				var index = i;
				//index = this.getActiveHead().index();
				var content = tab.viewObj.render().el;					
				that.$el.find(that.bodyEl + " > div:eq("+index+")").append(content);
			});
			return this;
		},
		attach: function() {
			_.each(this.tabs, function(tab, i) {
				tab.delegateEvents();
			});
		},
		renderHead: function(tab) {
			var html = this.headTemplate(tab);
			this.$el.find(this.headEl).append(html);
		},
		renderBody: function(tab) {
			var html = this.bodyTemplate(tab);
			this.$el.find(this.bodyEl).append(html);
		},
		setBody: function(content, number) {
			var index = number;
			if(number == undefined) {
				index = this.getActiveHead().index();
			}
			this.$el.find(this.bodyEl + " > div:eq("+index+")").html(content);
		},
		addTab: function(tab) {
			var that = this;
			_.extend(tab, {genId: _.bind(this.generateTabId, that),
						   getId: _.bind(this.getRemainTabId, that)
			});
			this.renderHead(tab);
			this.renderBody(tab);
			//tab instance를 tabs에 담는다.(tabview에서 tab instance를 관리 하기 위한 목적)
			this.tabs.push(tab.viewObj);
			tab.delegateEvents();
		},
		generateTabId: function() {
			++this.index.r;      
	        return this.prefix + "_" + (this.index.i++);
		},
		getRemainTabId: function() {
			return this.prefix + "_" + (this.index.i -(this.index.r--));
		},
		getHeads: function() {
			return this.$el.find(this.headEl +" > li");
		},
		getActiveHead: function() {
			return this.$el.find(this.headEl +" > li.active");
		},
		getSelectHead: function(e) {
			if(typeof e !=="object") return;
			var $heads = this.getHeads();
			return $heads.index($(e.target).closest('li'));
		},
		getPosition: function(totalSize, showPos, delPos) {
			return (showPos == delPos && showPos == totalSize) ? delPos -1
					: (showPos == delPos && showPos  < totalSize) ? delPos
					: (showPos > delPos) ? showPos-1 : showPos;	
		},
		removeTab: function(e) {
			if(e) e.preventDefault();
			var $head = this.getHeads();
			var $activeHead = this.getActiveHead();
			var totalSize = $head.length - 1;
			var showTabIndex = $activeHead.index();
			var delTabIndex = this.getSelectHead(e);
			
			this.$el.find(this.headEl +" > li:eq("+delTabIndex+")," + this.bodyEl + " > div:eq("+delTabIndex+")").remove();
			// 탭 정보를 담은 배열에서 삭제탭을 제거한다.
			var tab = this.tabs.splice(delTabIndex, 1);
			tab[0].close();
			
			this.activeTab(this.getPosition(totalSize, showTabIndex, delTabIndex));
		},
		activeTab: function(e) {
			var tabIndex = (typeof e ==="number") ? e : this.getHeads().index($(e.target).closest("li"));
			if(tabIndex < 0) return;
			this.deActiveTab();
			
			this.$el.find(this.headEl + "> li,"+ this.bodyEl + "> div" ).removeClass("active");
		},
        deActiveTab: function() {
            this.$el.find(this.headEl +" > li," 
                    + this.bodyEl +" > div").removeClass('active');
        },
		getActiveTab: function() {
			var activeTabIndex =  $("#tabs .nav > li.active").index();
			return this.tabs[activeTabIndex];
		}
	});
});