'use strict';
angular.module(
		'droneSelfieUiApp',
		[ 'ngAnimate', 'ngCookies', 'ngResource', 'ngRoute', 'ngSanitize',
				'ngTouch', 'toaster', 'ngWebSocket' ]).directive('dsnavbar',
		function() {
			return {
				replace : true,
				restrict : 'E',
				templateUrl : "directives/dsNavbar.html"
			};
		}).directive('dsinfo', function() {
	return {
		replace : true,
		restrict : 'E',
		templateUrl : "directives/dsInfo.html"
	};
}).config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'home.html',
		controller : 'MainCtrl'
	}).when('/about', {
		templateUrl : 'home.html',
		controller : 'AboutCtrl'
	}).otherwise({
		redirectTo : '/'
	});
});
