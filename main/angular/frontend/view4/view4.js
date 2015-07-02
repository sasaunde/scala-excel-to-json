'use strict';


angular.module('myApp.view4', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/view4', {
            templateUrl: 'view4/view4.html',
            controller: 'View4Ctrl'
        });
    }])
    .controller('View4Ctrl',  function($scope) {


        // Extent of the map in units of the projection (these match our base map)
        var extent = [-3276800, -3276800, 3276800, 3276800];

        // Fixed resolutions to display the map at (pixels per ground unit (meters when
        // the projection is British National Grid))
        var resolutions = [1600,800,400,200,100,50,25,10,5,2.5,1,0.5,0.25,0.125,0.0625];

        // Define British National Grid Proj4js projection (copied from http://epsg.io/27700.js)
        proj4.defs("EPSG:27700","+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 +x_0=400000 +y_0=-100000 +ellps=airy +towgs84=446.448,-125.157,542.06,0.15,0.247,0.842,-20.489 +units=m +no_defs");

        // Define an OL3 projection based on the included Proj4js projection
        // definition and set it's extent.
        var bng = ol.proj.get('EPSG:27700');
        bng.setExtent(extent);

        // Define a TileGrid to ensure that WMS requests are made for
        // tiles at the correct resolutions and tile boundaries
        var tileGrid = new ol.tilegrid.TileGrid({
            origin: extent.slice(0, 2),
            resolutions: resolutions
        });


        var iconStyle = new ol.style.Style({
        image : new ol.style.Icon({
            src : 'http://icons.iconarchive.com/icons/icons-land/vista-map-markers/48/Map-Marker-Ball-Right-Chartreuse-icon.png',
            size : [100,150]

        })});

        var gjsonFile = new ol.layer.Vector({
            source: new ol.source.GeoJSON({
                url: 'data/address.json',
                projection: 'EPSG:27700'
            }),
            style : iconStyle
        });

        $scope.map = new ol.Map({
            target: 'map',  // The DOM element that will contains the map
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.TileWMS({
                        url: 'http://ws3.ws.emapsite.com/view/wms/1.0/cap_gemini_uk_01_17f8683e73ea',
                        params: {
                            'SERVICE': 'WMS',
                            'REQUEST':'GetMap',
                            'version': '1.1.1',
                            'LAYERS': 'scaledependent_opendata',
                            'TRANSPARENT': 'true',
                            'FORMAT':'image/png',
                            'hq':'true',
                            'direct':'true'

                        },
                        tileGrid : tileGrid
                    })
                }),
                gjsonFile

            ],
            // Create a view centered on the specified location and zoom level
            view: new ol.View({
                projection: bng,
                resolutions: resolutions,
                center: [413674, 289141],
                zoom: 0
            })
        });

        var element = document.getElementById('popup');

        var popup = new ol.Overlay({
            element: element,
            positioning: 'bottom-center',
            stopEvent: false
        });
        $scope.map.addOverlay(popup);

        // display popup on click
        $scope.map.on('click', function(evt) {

            var feature = $scope.map.forEachFeatureAtPixel(evt.pixel,
                function(feature, layer) {

                    return feature;
                });
            if (feature) {
                var geometry = feature.getGeometry();
                var coord = geometry.getCoordinates();
                popup.setPosition(coord);
                $(element).popover({
                    'placement': 'top',
                    'html': true,
                    'content': feature.get('name') + "<br/> " + feature.get('grade')
                });
                $(element).popover('show');
            } else {
                $(element).popover('destroy');
            }
        });

    });

