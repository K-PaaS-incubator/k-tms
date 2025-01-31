/**
 * @description Menu View
 * 				tool bar 공격정보, IP조회, 트래픽로깅 
 */
define(function(require) {

	"use strict";

	// require library
	var $ 				= require('jquery'),
		_ 				= require('underscore'),
		Backbone 		= require('backbone'),
		TbAttackInfo 	= require('views/popup/attackInfoPopup'),
		TbIpRetrieve 	= require('views/tools/ipRetrievePopup'),
		TabView 		= require('views/popup/tab');

	var toolbarTpl 		= require('text!tpl/common/toolbar.html'),
		toolbarTemplate = _.template(toolbarTpl);

	// require i18n
	var locale 			= require('i18n!nls/menu'),
		strLocale 		= require('i18n!nls/str');

	var MenuView = Backbone.View.extend({
		tagName: "ul",
		className: "nav",
		parentMenuTemplate: _.template('<li class="has-sub <%= menu.menuKey %>"><a class="menu-icon cursor-pointer"><b class="caret pull-right"></b> <i class="fa fa-laptop"></i> <span><%= menuName %></span></a><ul class="sub-menu"></ul></li>'),
//		childMenuTemplate: _.template('<li class="<%= menu.url %>"><a href="#<%= menu.url %>"><%= menuName %></a></li>'),
		childMenuTemplate: _.template('<li class="<%= menu.url %>"><a class="menuLinkUrl" href ="#<%= menu.url %>" data-url="#<%= menu.url %>"><%= menuName %></a></li>'),
		initialize: function(options) {
			this.render();
		},
		events: {
			'click .toolbarAttackInfo'		: 'attackInfoPopup',
			'click .toolbarIpRetrieve'		: 'ipRetrievePopup',
		},
		render: function() {
			var view = this;

			this.$el.empty();

			_.each(this.collection, function(menu) {
				if (menu.upperMenuKey) {
					$('.' + menu.upperMenuKey + ' ul', view.el).append(view.childMenuTemplate({
						menu: menu,
						menuName: eval('locale.menu.' + menu.menuKey)
					}));
				} else {
					view.$el.append(view.parentMenuTemplate({
						menu: menu,
						menuName: eval('locale.menu.' + menu.menuKey)
					}));
				}
			});

			this.$el.append(toolbarTemplate({
				locale: locale
			}));

			return this;
		},
		attackInfoPopup: function() {
			var tabView = new TabView();
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: strLocale.attack+strLocale.information,
				body: tabView
			});

			var tbAttackInfo = new TbAttackInfo({
				tabView: tabView
			});

			tabView.addTab(tbAttackInfo, false);
		},
		ipRetrievePopup: function() {
			Backbone.ModalView.msg({
				size: 'medium-large',
				title: 'IP ' + strLocale.retrieve,
				body: new TbIpRetrieve({
					ip: null	// 각 메뉴의 ip팝업에 링크를 걸때 ip값을 넘겨야함, toolbar에서 ip를 null이라도 넘기지 않으면 에러
				})
			});
		},
	});

	return MenuView;
});
