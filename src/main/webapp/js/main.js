'use strict';

angular
		.module('droneSelfieUiApp')
		.factory(
				'myData',
				function($websocket) {

					var dataStream = $websocket('ws://'
							+ window.document.location.host
							+ '/SocketEndpoint');

					var collection = [];
					var collection2 = [];

					dataStream.onMessage(function(message) {
						if (message.data.indexOf("f") === 0) {
							var url = message.data.substring(1,
									message.data.length)
							collection.push(url);
						} else {
							var url = message.data.substring(1,
									message.data.length)
							collection2.push(url);
						}
					});

					var methods = {
						collection : collection,
						collection2 : collection2,
						get : function() {
							dataStream.send(JSON.stringify({
								action : 'get'
							}));
						}
					};

					return methods;
				})
		.controller(
				'MainCtrl',
				function($scope, myData, $http, toaster) {

					$scope.myData = myData;
					$scope.myText = 'Picture taken via the #bluemix Selfie Drone';

					$scope.remove = function(element) {
						
						var index = myData.collection.indexOf(element);
						if (index > -1) {
							myData.collection.splice(index, 1);
						}
					};

					$scope.remove2 = function(element) {
						
						var index = myData.collection2.indexOf(element);
						if (index > -1) {
							myData.collection2.splice(index, 1);
						}
					};

					$scope.tweet = function(picUrl) {
						$http(
								{
									method : 'POST',
									url : 'tweet?id='
											+ picUrl
													.substring(7, picUrl.length)
											+ '&message='
											+ encodeURIComponent(document
													.getElementById("twittermessage").value)
								}).success(function() {
						}).error(function() {
						});
					};

					$scope.takeOff = function() {
						$http(
								{
									method : 'POST',
									url : 'navigate?c=takeoff'
								})
								.success(function() {})
								.error(function() {});
					};
					
					$scope.land = function() {
						$http(
								{
									method : 'POST',
									url : 'navigate?c=land'
								})
								.success(function() {})
								.error(function() {});
					};
					
					$scope.up = function() {
						$http(
								{
									method : 'POST',
									url : 'navigate?c=up'
								})
								.success(function() {})
								.error(function() {});
					};

					$scope.down = function() {
						$http(
								{
									method : 'POST',
									url : 'navigate?c=down'
								})
								.success(function() {})
								.error(function() {});
					};

					$scope.rotatec = function() {
						$http(
								{
									method : 'POST',
									url : 'navigate?c=rotatec'
								})
								.success(function() {})
								.error(function() {});
					};

					$scope.rotatecc = function() {
						$http(
								{
									method : 'POST',
									url : 'navigate?c=rotatecc'
								})
								.success(function() {})
								.error(function() {});
					};

					$scope.toBeDone = function() {
					};

					$scope.twitterMessage;

					$scope.takePicture = function() {
						$http({
							method : 'GET',
							url : 'takepic?id=' + new Date().getTime()
						}).success(function() {
						}).error(function() {
						});
					};
				});
