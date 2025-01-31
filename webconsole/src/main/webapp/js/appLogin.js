require.config({
    baseUrl: 'js/lib',
    paths: {
        app: '../appLoginInit',
        utils: '../utils',
        security: '../utils/security',
        spin: './spin',
        jqueryspin: './jquery.spin',
        jqueryui: './jquery-ui',
        views: '../app/views',
        models: '../app/models',
        collections: '../app/collections',
        routers: '../app/routers',
        tpl: '../tpl',
        nls: '../nls',
        bootstrap: '../../bootstrap/js/bootstrap',
        serialize: 'jquery.serialize-object',
        backbone: './backbone',
        datetimepicker: './jquery.datetimepicker',
        apps: 'apps.min',
    },
    // Set the config for the i18n
//    locale: LOCALE,

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
        'spin': {
            deps: ['jquery'],
            exports: 'spin'
        },
        'apps': {
            deps: ['jquery'],
            exports: 'apps'
        },
        'jqueryspin': {
            deps: ['jquery'],
            exports: 'jqueryspin'
        },
        'jqueryui': {
            deps: ['jquery'],
            exports: 'jqueryui'
        },
        'serialize': {
            deps: ['jquery']
        }
    }
});

require(['jquery', 'backbone', '../appLoginInit', 'utils/sessionManager', 'views/login', 'spin', 'jqueryui', 'jqueryspin'],
        function ($, Backbone, app, sessionManager, LoginView) {
            app.initialize();
            sessionManager.isLogin ? location.href = 'home.html#dashboard' : new LoginView({
//            sessionManager.isLogin ? location.href = 'home.html#securityPolicy_intrusionDetectionPolicy' : new LoginView({
                el: $('#content')
            });
        });
