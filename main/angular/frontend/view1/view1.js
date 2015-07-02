'use strict';

angular.module('myApp.view1', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
    	
  });
}])
//.config(['$httpProvider', function ($httpProvider) {
//    $httpProvider.defaults.useXDomain = true;
//    delete $httpProvider.defaults.headers.common['X-Requested-With'];
//}])

.controller("View1Ctrl", function($scope, $http) {
        	
	$scope.person = undefined
	
	$scope.updatePerson = function(p) {
		
		$scope.person = p
		// Look up corpdir entry based on id
		if(p.id != undefined && p.id.trim() != "") {
			$http.post('http://localhost:8090/find/'+p.id, { uri : p.name.substring(p.name.lastIndexOf(' '), p.name.length).trim() + ", " + p.name.substring(0, p.name.lastIndexOf(' ')).trim(), id : Number(p.id) }).
			success(function(data, status, headers, config) {
				// create image
				console.log("Got pic for " + data.photoUrl);
				$scope.person.imgUrl = data.photoUrl;
			})
			.error(function(data, status, headers, config) {
				// log error
				console.log("Error- status "+status+"!")
			});
		}
	};
	
	$http.get('http://localhost:8090/person')
	.success(function(data, status, headers, config) {
								
		$scope.posts = data;
	   
	})
	.error(function(data, status, headers, config) {
		// log error
		console.log("Error- status "+status+"!")
	});
	
        });