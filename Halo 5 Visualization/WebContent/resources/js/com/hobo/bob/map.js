// Map views always need a projection.  Here we just want to map image
      // coordinates directly to map coordinates, so we create a projection that uses
      // the image extent in pixels.
//      var extent = [-1075, -1220, 920, 725];
//var extent = [-113.25, -76.5, 86.75, 123.5];


var extent = [-113.25, -90, 86.75, 130];
//var extent = [-140, -76.5, 110, 123.5];
      var projection = new ol.proj.Projection({
        code: 'halo-image',
        units: 'pixels',
        extent: extent
      });
      
      var identity = function(coordinate) {
    	  return coordinate;
      }
      
      ol.proj.addCoordinateTransforms(projection, projection, identity, identity);
      
      var mousePositionControl = new ol.control.MousePosition({
          coordinateFormat: ol.coordinate.createStringXY(4),
          projection: projection,
          // comment the following two lines to have the mouse position
          // be placed within the map.
//          className: 'custom-mouse-position',
//          target: document.getElementById('mouse-position'),
          undefinedHTML: '&nbsp;'
        });

      var map = new ol.Map({
    		 controls: ol.control.defaults({
    	          attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
    	              collapsible: false
    	            })
    	          }).extend([mousePositionControl]),
        layers: [
          new ol.layer.Image({
            source: new ol.source.ImageStatic({
              //attributions: '© <a href="http://xkcd.com/license.html">xkcd</a>',
              //url: 'http://imgs.xkcd.com/comics/online_communities.png',
              url: 'resources/images/maps/Array.png',
              projection: projection,
              imageExtent: extent
            })
          })
        ],
        target: 'map',
        view: new ol.View({
          projection: projection,
          center: ol.extent.getCenter(extent),
          zoom: 2,
          maxZoom: 8
        })
      });