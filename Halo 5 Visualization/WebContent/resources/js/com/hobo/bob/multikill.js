function loadMultiKills() {
	addLayer(multiKill3, new ol.style.Style({
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
	}));
	
	addLayer(multiKill2, new ol.style.Style({
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
	}));
	
	addLayer(multiKill4, new ol.style.Style({
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
	}));
}