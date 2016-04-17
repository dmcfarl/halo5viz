function loadHeatmap(data) {

	var blur = document.getElementById('blur');
	var radius = document.getElementById('radius');

	var features = new Array(data.length);

	for ( var idx in data) {
		var points = data[idx];

		for (var i = 0; i < points.length; i++) {
			features[i] = new ol.Feature(new ol.geom.Point(points[i]));
			/*if (points[i][0] === core[0] && points[i][1] === core[1]) {
				features[i].setStyle(coreStyle);
			} else if (style) {
				features[i].setStyle(style);
			} else {
				features[i].setStyle(killStyle);
			}*/
			features[i].set('weight', idx + 2);
		}
	}

	var source = new ol.source.Vector({
		features : features
	});

	var vectorLayer = new ol.layer.Heatmap({
		source : source,
		blur : parseInt(blur.value, 10),
		radius : parseInt(radius.value, 10)
	});

	blur.addEventListener('input', function() {
		vectorLayer.setBlur(parseInt(blur.value, 10));
	});

	radius.addEventListener('input', function() {
		vectorLayer.setRadius(parseInt(radius.value, 10));
	});

	/*vector.getSource().on('addfeature', function(event) {
	  // 2012_Earthquakes_Mag5.kml stores the magnitude of each earthquake in a
	  // standards-violating <magnitude> tag in each Placemark.  We extract it from
	  // the Placemark's name instead.
	  var name = event.feature.get('name');
	  var magnitude = parseFloat(name.substr(2));
	  event.feature.set('weight', magnitude - 5);
	});*/
	map.addLayer(vectorLayer);
}