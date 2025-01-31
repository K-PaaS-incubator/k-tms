require.config({

	baseUrl: 'js/lib',

	paths: {
		app: '../app',
		utils: '../utils',
                security: '../utils/security',
		views: '../app/views',
		models: '../app/models',
		collections: '../app/collections',
		routers: '../app/routers',
		tpl: '../tpl',
		nls: '../nls',
		bootstrap: '../../bootstrap/js/bootstrap',
		backbone: './backbone',
		datetimepicker: './jquery.datetimepicker',
		simpledatetimepicker: './jquery.simple-dtpicker',
		monthpicker: './jquery.monthpicker',
		spin: './spin',
		jqueryspin: './jquery.spin',
		jqueryui: './jquery-ui',
		jquerySlimScroll: './jquery.slimscroll.min',
		apps: 'apps.min',
		bootstrapTable: 'bootstrap-table',
		jqueryScrollTabs: 'jquery.scrolltabs',
		jqueryMouseWheel: 'jquery.mousewheel',
		tmsjs: '../lib/tms-js',
		serialize:'jquery.serialize-object',
		validation : './backbone-validation-amd',
		stickit:'./backbone.stickit'
	},

	// Set the config for the i18n
	locale : LOCALE,

	shim: {
		'underscore': {
			exports: '_'
		},
		'backbone': {
			deps: ['underscore', 'jquery'],
			exports: 'Backbone'
		},
		'bootstrap': {
			deps: ['jquery'],
			exports: 'bootstrap'
		},
		'bootstrapTable': {
			deps: ['jquery'],
			exports: 'bootstrapTable'
		},
		'd3': {
			exports: 'd3.min'
		},
		'spin': {
			deps: ['jquery'],
			exports: 'spin'
		},
		'apps': {
			deps: ['jquery'],
			exports: 'apps'
		},
		'datetimepicker': {
			deps: ['jquery'],
			exports: 'datetimepicker'
		},
		'simpledatetimepicker': {
			deps: ['jquery'],
			exports: 'simpledatetimepicker'
		},
		'monthpicker': {
			deps: ['jquery'],
			exports: 'monthpicker'
		},
		'jqueryui': {
			deps: ['jquery'],
			exports: 'jqueryui'
		},
		'jquerySlimScroll': {
			deps: ['jquery'],
			exports: 'jquerySlimScroll'
		},
		'jqueryMouseWheel': {
			deps: ['jquery'],
			exports: 'jqueryMouseWheel'
		},
		'jqueryScrollTabs': {
			deps: ['jquery'],
			exports: 'jqueryScrollTabs'
		},
		'serialize': {
			deps: ['jquery']
		},
		'validation':{
			deps: ['jquery'],
			exports: 'backbone-validation'
		},
		'stickit':{
			deps: ['jquery'],
			exports: 'Stickit'
		},
		'tmsjs': {
			deps: ['jquery'],
			exports: 'tmsjs'
		}
	}
});

require(['jquery', 'backbone', 'datetimepicker', 'simpledatetimepicker', 'monthpicker', 'jqueryui', 'jqueryspin', 'spin', '../app', 'utils/sessionManager', 'routers/router', 'stickit', 'tmsjs'],
	function($, Backbone, datetimepicker, simpledatetimepicker, monthpicker, jqueryui, jqueryspin, spin, app, sessionManager, router, Apps, stickit, tmsjs) {
		app.initialize();
		sessionManager.isLogin ? (router.initialize(), router.initHistoryApi()) : location.href = '/';
	});
