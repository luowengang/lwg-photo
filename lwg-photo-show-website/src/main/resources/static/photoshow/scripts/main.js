'use strict';

/**
 * @ngdoc function
 * @name yoApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the yoApp
 */
angular.module('photoShowApp', ["ngWaterfall", "ui.router"])
    // .config(['$stateProvider', '$urlRouterProvider',
    //     function($stateProvider, $urlRouterProvider) {
    //         $stateProvider.state("home", {
    //             url: "/home",
    //             templateUrl: "views/main.html",
    //             controller: "MainCtrl"
    //         })
    //         $urlRouterProvider.otherwise("/home");

//     }
// ])
.value('restUrlPrefix', 'http://' + window.location.hostname + ':8060/')
.factory("myService", ['$http','restUrlPrefix',function($http, restUrlPrefix) {
        return {
            getImages: function(param, cb) {
                $http({
                    method: "GET",
                    params: {
                        'pageNum': param.page || 1,
                        'pageSize': param.pageSize || 30,
                    },
                    url: restUrlPrefix + 'photo/getPhoto'
                }).
                success(function(data, status) {
                    cb(data, status);
                }).
                error(function(data, status) {});
            },
            getPhotoByTitle: function(title, cb) {
                $http({
                    method: "GET",
                    params: {
                        'title': title
                    },
                    url: restUrlPrefix + 'photo/getPhotoByTitle'
                }).
                success(function(data, status) {
                    cb(data, status);
                }).
                error(function(data, status) {});
            },
            getAllPhotoTitle: function(param, cb) {
                $http({
                    method: "GET",
                    url: restUrlPrefix + 'photo/getAllTitle'
                }).
                success(function(data, status) {
                    cb(data, status);
                }).
                error(function(data, status) {});
            }
        }
    }])
    .controller('MainCtrl', function($scope, $rootScope, $state, $location, $timeout, myService) {
    	$scope.imgUrlPrefix = 'http://' + window.location.hostname + ':8030';
        var page = 1;
        var pageSize = 30;

        // $scope.$on("photo-show", function(e, data) {
        //     page = 1;
        //     loadImages(data);
        // });

        var loadImages = function(data) {
            $scope.images = data;
            document.body.scrollTop = 0;
            document.scrollingElement.scrollTop = 0;
        }

        myService.getImages({ page, pageSize }, function(data) {
            loadImages(data);

        })

        $scope.text = "点我加载更多"
        $scope.loadMore = true;
        $scope.loadMoreData = function() {
            $scope.text = "加载中，请稍后···";
            $timeout(function() {
                page++;
                myService.getImages({ page, pageSize }, function(data) {
                    loadImages(data);
                    // $scope.results = data.slice(0, page * pageSize);
                    // if ($scope.results.length == 9030) {
                    //     $scope.text = "内容已经全部加载完毕"
                    // }
                });
                $scope.text = "点我加载更多···";
            }, 1500);
        };
        //        $scope.$on("waterfall:loadMore", function() { //滚动自动填充事件
        //            $scope.loadMoreData();
        //        })


        $scope.showPhoto = function(title) {
            myService.getPhotoByTitle(title, function(data) {
                $scope.showNav = false;
                $scope.currentTitle = title;
                // 在主图区域中显示图片
                loadImages(data);

            });
        };

        $scope.initAllPhotoTitle = function() {
            myService.getAllPhotoTitle({}, function(data) {
                $scope.showNav = true;
                $scope.topics = data;
                document.body.scrollTop = 0;
                document.scrollingElement.scrollTop = 0;
            });
        };
    })