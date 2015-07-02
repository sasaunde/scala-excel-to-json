'use strict';

function NameWrapper(id, list) {
	this.id = id;
	this.list = list;
	
}
NameWrapper.prototype.toString = function() {return "NameWrapper[ID: " + this.id + ", names : " + this.list.innerHTML+"]"};

angular.module('myApp.view2', ['ngRoute','uiGmapgoogle-maps', 'angularCharts'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view2', {
    templateUrl: 'view2/view2.html',
    controller: 'View2Ctrl'
  });
}])
.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}])

.controller('View2Ctrl', function($scope, $http) {

	 var markers = [];
	
	
		var getMarkers = function(data, mapp, infoWindow) {
			
			var officeLocations = ['UK-LON-HOLBORN', 'UK-BHM-ASTON', 'UK-WKG-WOKING', 'UK-RTH-ROTHERHM', 'UK-SAE-SALECHES', 'UK-LON-SOUTHBNK', 'UK-BSO-TOLTEC','UK-SWA-SWANSEA', 'UK-EDI-EDINBURG', 'UK-GLW-GLASGOW', 'UK-XUK-UNDEFINE']
			var officeLocationsLat = [51.517974, 52.495372, 52.496492, 53.447303, 53.428356, 51.484766, 51.541879, 51.651700, 55.930853, 55.857282, 55]
			var officeLocationsLong = [-0.107251,-1.884734, -1.885391, -1.325697, -2.322980, -0.126619, -2.582532, -3.913899,  -3.300516, -4.260425, 0] 
			
	     
	      // create marker
	     
		  var allNames = [];

	      for(var idKey = 0; idKey < data.length; idKey++) {
	    	  
	    	  var person = data[idKey];
			// Look up corpdir entry based on id
	    	  if(person.id != undefined && person.id.trim() != "") {

				//$http( {
				//	method : 'POST',
				//	url: 'http://localhost:8090/find/'+person.id,
				//	data: { uri : person.name.substring(person.name.lastIndexOf(' '), person.name.length).trim() + ", " + person.name.substring(0, person.name.lastIndexOf(' ')).trim(),
				//		id : person.id },
				//	headers: {'Content-Type':'application/json'}
				//})
				////$http.post('http://localhost:8090/find/'+person.id,
				////	{ uri : person.name.substring(person.name.lastIndexOf(' '), person.name.length).trim() + ", " + person.name.substring(0, person.name.lastIndexOf(' ')).trim(),
				////		id : Number(person.id) }).
				//	.success(function(looky, status, headers, config) {

					// get base location
					// look up base location coordinates
			    	  var idx = officeLocations.indexOf(person.baseLocation)
				  if(idx != -1) {
					  var latitude = officeLocationsLat[idx]
					  var longitude = officeLocationsLong[idx]

					  var homeLatLng = new google.maps.LatLng(latitude, longitude);

					  var names = document.createElement("div")
					  names.id = 'infoWindowStyle'

					  var alreadyGotThisLocation = _.find(markers, function (e) {
						  return e.position.equals(homeLatLng)
					  })

					  if (alreadyGotThisLocation != undefined) {

						  var nameDiv = _.find(allNames, function (e) {
							  return e.id == person.baseLocation
						  })
						  if (nameDiv != undefined && nameDiv.list.innerHTML.indexOf(person.name) == -1) {
							  //console.log("**********************Found this one that we already have: "+nameDiv.list.innerHTML)
							  nameDiv.list.innerHTML = nameDiv.list.innerHTML + ", <br/>" + person.name + "("+person.team+")";
						  }
					  } else {
						  // a new person
						  names.innerHTML = person.name + "("+person.team+")"
						  var nameWrapper = new NameWrapper(person.baseLocation, names);

						  allNames.push(nameWrapper)
						  // create pin
						  var i = new google.maps.Marker({//MarkerWithLabel({
							  id: person.id,
							  title: person.name,
							  position: homeLatLng,
							  map: mapp,
							  //draggable: true,
							  //labelContent: names,
							  //labelAnchor: new google.maps.Point(0, 0),
							  //labelClass: "labels", // the CSS class for the label
							  //labelStyle: {opacity: 0.50}
						  });

						  google.maps.event.addListener(i, 'click', (function (marker, i) {
							  return function () {

								  infoWindow.setContent(_.find(allNames, function (e) {
									  return e.id == i
								  }).list);
								  infoWindow.open(mapp, marker);
							  }
						  })(i, person.baseLocation));

						  markers.push(i)
					  }
					  //}).
					  //error(function(d, status, headers, config) {
					  //	// log error
					  //	console.log("Error- status "+status+"!")
					  //});
				  }
	    	  }
	      }
		     
		}; 
		
		$scope.projectCode = '';
		
		$scope.config = {
											
			    title: 'Projects',
			    tooltips: true,
			    labels: false,
			    mouseover: function() {},
			    mouseout: function() {},
			    click: function(d) {

					console.log(d.data);

			    	$http.get('http://localhost:8090/people/graph/'+d.data.x).success(function(data, status, headers, config){
			    		$scope.data2 = data;
			    		$scope.projectCode = d.data.x;
			    		$scope.mapTitle = "Distribution of Home Location for Project " + d.data.tooltip;
			    		
						$scope.config2.title = "Grade Distribution for Project "+ d.data.tooltip;
			    		
			    	}).error(function(data, status, headers, config) {
						// log error
						console.log("Error- status "+status+"!")
					});


			    	$http.get('http://localhost:8090/people/'+d.data.x).
					success(function(data, status, headers, config) {
							
						var mapp = new google.maps.Map(document.getElementById("map_canvas"), { center: new google.maps.LatLng(52.496492, -1.885391), zoom: 6 })

					    var infoWindow = new google.maps.InfoWindow();
						
						getMarkers(data, mapp, infoWindow)
						
						
					}).
					error(function(data, status, headers, config) {
						// log error
						console.log("Error- status "+status+"!")
					});
			    	
			    },
			    legend: {
			      display: false,
			      //could be 'left, right'
			      position: 'right'
			    }};
			       

		$scope.config2 = {

			tooltips: true,
			labels:false,
			mouseover: function() {},
		    mouseout: function() {},
		    click: function(d) {
		    	console.log("Clicked tooltip " + d.data.tooltip);
		    	console.log("x is " + d.data.x)
		    	var pCode = $scope.projectCode
		    	console.log("Project code " + pCode)
		    	
		    	$http.post('http://localhost:8090/people/'+pCode, {name : "", id : "", projectCode: pCode, level : d.data.tooltip, baseLocation : 'n/a', team : 'n/a'})
		    	.success(function(data, status, headers, config) {
		    		var mapp = new google.maps.Map(document.getElementById("map_canvas"), { center: new google.maps.LatLng(52.496492, -1.885391), zoom: 6 })

				    var infoWindow = new google.maps.InfoWindow();
											
		    		$scope.mapTitle = "Distribution of home location of people of grade " + data[0].level
				   
		    		// remove previous markers
		    		for(var i = 0; i < markers.length; i++) {
		    			markers[i].setMap(null);
		    		}
		    		markers = []
		    		
		    		//add ones for just this grade
					getMarkers(data, mapp, infoWindow)
		    	})
		    	.error(function(data, status, headers, config) {
					// log error
					console.log("Error- status "+status+"!")
				});
		    },
		    legend: {
			      display: true,
			      //could be 'left, right'
			      position: 'right'
			    }
		};
		
		$http.get('http://localhost:8090/pie').
			success(function(data, status, headers, config) {
					
					$scope.data = data;
			}).
			error(function(data, status, headers, config) {
				// log error
				console.log("Error- status "+status+"!")
			});
		
	
	})
	

