'use strict';


angular.module('myApp.view3', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view3', {
    templateUrl: 'view3/view3.html',
    controller: 'View3Ctrl'
  });
}])
.controller('View3Ctrl',  function($scope) {
	 
	
		 var options = {
			      elt: document.getElementById('divGoogleMapForSharePointList'),           // ID of map element on page
			      zoom: 10,                                      // initial zoom level of the map
			      latLng: { lat: 51.517974, lng: -0.107251 },  // center of map in latitude/longitude
			      mtype: 'map',                                  // map type (map, sat, hyb); defaults to map
			      bestFitMargin: 0,                              // margin offset from map viewport when applying a bestfit on shapes
			      zoomOnDoubleClick: true                        // enable map to be zoomed in when double-clicking on map
			    };
		 $scope.map = new LocalMap(new MQA.TileMap(options));
		
		 $scope.map.SetMarkerForGoogleMapForSharePointList(51.517974, -0.107251, 'Woking','This is our main office');

		 window.map = $scope.map.map
		 
		 MQA.withModule('largezoom', 'mousewheel', function() {
			 
			// enable zooming with your mouse
			    map.enableMouseWheelZoom();
			    
			    // add the Large Zoom control
			    map.addControl(
			      new MQA.LargeZoom(),
			      new MQA.MapCornerPlacement(MQA.MapCorner.TOP_LEFT, new MQA.Size(5,5))
			    );
		 })

})

function LocalMap(mqObject) {

	this.map = mqObject
	//this.markers = new Array();
	
	this.SetMarkerForGoogleMapForSharePointList = function(lat, lng, title, contentForInfoWindow)
	{
	 
	 var marker = new MQA.Poi({lat : lat, lng : lng});

	 marker.setDeclutterMode(true);
	 marker.setRolloverContent(title);
	 marker.setInfoContentHTML(contentForInfoWindow);

	 
	 //this.markers.push(marker);
	 this.map.addShape(marker);

	}
	
	
	
};

