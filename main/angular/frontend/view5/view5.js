'use strict';


angular.module('myApp.view5', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/view5', {
            templateUrl: 'view5/view5.html',
            controller: 'View5Ctrl'
        });
    }])
    .controller('View5Ctrl',  function($scope, $http) {

        $scope.data2 = [];

        $scope.config = {

            title: 'Teams',
            tooltips: true,
            labels: false,
            mouseover: function() {},
            mouseout: function() {},
            click: function(d) {

                $http.get('http://localhost:8090/teams/'+d.data.x).success(function(data, status, headers, config){
                    $scope.data2 = data;

                    $scope.members = "Team members for project code " + d.data.x;

                    //$scope.config2.title = "Grade Distribution for Project "+ d.data.tooltip;

                }).error(function(data, status, headers, config) {
                    // log error
                    console.log("Error- status "+status+"!")
                });


            },
            legend: {
                display: false,
                //could be 'left, right'
                position: 'right'
            }};

        //$scope.config2 = {
        //
        //    tooltips: true,
        //    labels:false,
        //    mouseover: function() {},
        //    mouseout: function() {},
        //    click: function(d) {
        //        console.log("Clicked tooltip " + d.data.tooltip);
        //        console.log("x is " + d.data.x)
        //
        //    },
        //    legend: {
        //        display: true,
        //        //could be 'left, right'
        //        position: 'right'
        //    }
        //};

        $http.get('http://localhost:8090/teams').success(function(data){
            $scope.data = data;

        }).error(function(data, status, headers, config) {
            // log error
            console.log("Error getting teams- status "+status+"!")
        });


    });
