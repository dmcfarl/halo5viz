var multiKillStyles = [
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
new ol.style.Style({
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
];

function loadMultiKills(data) {
	for (var idx in data) {
		addMultiKillLayer(data[idx], multiKillStyles[idx]);
	}
}