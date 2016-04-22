var multiKillStyles = {
	killLineStyleFunction : function(resolution) {
		var geometry = this.getGeometry();
		var styles = [
		// linestring
		new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : 'black',
				width : 2,
				lineDash : [ 5, 5 ]
			})
		}) ];

		var arrowCenter = geometry.getCoordinateAt(0.98);

		geometry.forEachSegment(function(start, end) {
			var startPixel = map.getPixelFromCoordinate(start);
			var endPixel = map.getPixelFromCoordinate(end);
			var distance = [ Math.abs(endPixel[0] - startPixel[0]),
					Math.abs(endPixel[1] - startPixel[1]) ];

			if (distance[0] > 20 || distance[1] > 20) {
				var dx = end[0] - start[0];
				var dy = end[1] - start[1];
				var rotation = -Math.atan2(dy, dx) + (Math.PI / 2);

				// arrows
				styles.push(new ol.style.Style({
					geometry : new ol.geom.Point(end),
					image : new ol.style.Icon({
						src : 'resources/images/arrow_sml.png',
						anchor : [ 0.5, -5 ],
						anchorYUnits : 'pixels',
						rotateWithView : true,
						snapToPixel : false,
						rotation : rotation
					})
				}));
			}
		});

		return styles;
	},
	multiKillLineStyleFunction : function(resolution) {

		var geometry = this.getGeometry();
		var styles = [
		// linestring
		new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : 'black',
				width : 2
			})
		}) ];

		geometry.forEachSegment(function(start, end) {
			var startPixel = map.getPixelFromCoordinate(start);
			var endPixel = map.getPixelFromCoordinate(end);
			var distance = [ Math.abs(endPixel[0] - startPixel[0]),
					Math.abs(endPixel[1] - startPixel[1]) ];

			if (distance[0] > 25 || distance[1] > 25) {
				var dx = end[0] - start[0];
				var dy = end[1] - start[1];
				var rotation = -Math.atan2(dy, dx) + (Math.PI / 2);
				// arrows
				styles.push(new ol.style.Style({
					geometry : new ol.geom.Point(end),
					image : new ol.style.Icon({
						src : 'resources/images/arrow_sml.png',
						anchor : [ 0.5, -5 ],
						anchorYUnits : 'pixels',
						rotateWithView : true,
						snapToPixel : false,
						rotation : rotation
					})
				}));
			}
		});

		return styles;
	},
	1 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#FFFFFF'
			}),
			stroke : new ol.style.Stroke({
				color : '#87CEEB',
				width : 1
			})
		})
	}),
	2 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#000080'
			}),
			stroke : new ol.style.Stroke({
				color : '#87CEEB',
				width : 1
			})
		})
	}),
	3 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#008080'
			}),
			stroke : new ol.style.Stroke({
				color : '#2F4F4F',
				width : 1
			})
		})
	}),
	4 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#2E8B57'
			}),
			stroke : new ol.style.Stroke({
				color : '#00FF7F',
				width : 1
			})
		})
	}),
	5 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#32CD32'
			}),
			stroke : new ol.style.Stroke({
				color : '#ADFF2F',
				width : 1
			})
		})
	}),
	6 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#FFFF00'
			}),
			stroke : new ol.style.Stroke({
				color : '#808000',
				width : 1
			})
		})
	}),
	7 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#FFD700'
			}),
			stroke : new ol.style.Stroke({
				color : '#B8860B',
				width : 1
			})
		})
	}),
	8 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#D2691E'
			}),
			stroke : new ol.style.Stroke({
				color : '#8B4513',
				width : 1
			})
		})
	}),
	9 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#FF4500'
			}),
			stroke : new ol.style.Stroke({
				color : '#A52A2A',
				width : 1
			})
		})
	}),
	10 : new ol.style.Style({
		image : new ol.style.Circle({
			radius : 5,
			snapToPixel : false,
			fill : new ol.style.Fill({
				color : '#800000'
			}),
			stroke : new ol.style.Stroke({
				color : '#A52A2A',
				width : 1
			})
		})
	})
};

var multiKillLayers = {};
for (var i = 1; i <= 10; i++) {
	multiKillLayers[i] = {
		sources : {
			killLines : new ol.source.Vector({}),
			multiKillLines : new ol.source.Vector({}),
			points : new ol.source.Vector({})
		}
	};
}

for ( var layer in multiKillLayers) {
	multiKillLayers[layer].layers = {};

	for ( var type in multiKillLayers[layer].sources) {
		multiKillLayers[layer].layers[type] = new ol.layer.Vector({
			source : multiKillLayers[layer].sources[type]
		});
		map.addLayer(multiKillLayers[layer].layers[type]);
	}
}

var multiKillInput = document.getElementById('multikill');
if (multiKillInput != null) {
	multiKillInput.addEventListener('input', function() {
		for (var i = 1; i < multiKillInput.value; i++) {
			for ( var type in multiKillLayers[layer].layers) {
				multiKillLayers[i].layers[type].setVisible(false);
			}
		}
		for (var i = multiKillInput.value; i <= 10; i++) {
			for ( var type in multiKillLayers[layer].layers) {
				multiKillLayers[i].layers[type].setVisible(true);
			}
		}
	});
}

function loadMultiKills(data) {
	addMultiKillLayer(data);
}

function addMultiKillLayer(multiKillData) {

	for (var multiKillSize = 0; multiKillSize < multiKillData.length; multiKillSize++) {
		var multiKills = multiKillData[multiKillSize];
		var points = new Array(0);
		var killLines = new Array(0);
		var multiKillLines = new Array(0);

		for ( var multiKillNum in multiKills) {
			var multiKill = multiKills[multiKillNum];
			for ( var killNum in multiKill) {
				var kill = multiKill[killNum];
				for ( var killLoc in kill) {
					var loc = new ol.Feature(new ol.geom.Point(kill[killLoc]));
					loc.setStyle(multiKillStyles[multiKillSize + 1]);
					points.push(loc);
				}

				var killLine = new ol.Feature(new ol.geom.LineString(kill));
				killLine.setStyle(multiKillStyles.killLineStyleFunction);
				killLines.push(killLine);
				if (killNum > 0) {
					var multiKillLine = new ol.Feature(new ol.geom.LineString([
							multiKill[killNum - 1][0], multiKill[killNum][0] ]));
					multiKillLine
							.setStyle(multiKillStyles.multiKillLineStyleFunction);
					multiKillLines.push(multiKillLine);
				}
			}
		}
		multiKillLayers[multiKillSize + 1].sources.points.addFeatures(points);
		multiKillLayers[multiKillSize + 1].sources.killLines
				.addFeatures(killLines);
		multiKillLayers[multiKillSize + 1].sources.multiKillLines
				.addFeatures(multiKillLines);
	}

}