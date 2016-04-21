var core = [ -6.14136648, 54.54802 ];

function addLayer(points, style) {
	var coreStyle = new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : 'red'
			}),
			stroke : new ol.style.Stroke({
				color : 'green',
				width : 1
			})
		})
	});

	var killStyle = new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : 'red'
			}),
			stroke : new ol.style.Stroke({
				color : 'yellow',
				width : 1
			})
		})
	});

	var features = new Array(points.length);
	// features[0] = new ol.Feature(new ol.geom.Point([-6.14136648,54.54802]))
	// features[0].setStyle(coreStyle);
	for (var i = 0; i < points.length; i++) {
		features[i] = new ol.Feature(new ol.geom.Point(points[i]));
		if (points[i][0] === core[0] && points[i][1] === core[1]) {
			features[i].setStyle(coreStyle);
		} else if (style) {
			features[i].setStyle(style);
		} else {
			features[i].setStyle(killStyle);
		}
	}

	var source = new ol.source.Vector({
		features : features
	});

	var vectorLayer = new ol.layer.Vector({
		source : source
	});

	map.addLayer(vectorLayer);

}
