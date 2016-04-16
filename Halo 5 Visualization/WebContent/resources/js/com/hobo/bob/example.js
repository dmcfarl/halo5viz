var pixelProjection = new ol.proj.Projection({
  code: 'pixel',
  units: 'pixels',
  extent: [0, 0, 1389, 1070]
});


var map = new ol.Map({
  layers: [
    new ol.layer.Image({
      source: new ol.source.ImageStatic({
        url: 'http://s25.postimg.org/4o15oqbmn/jdgf.jpg',
        imageSize: [1389, 1070],
        projection: pixelProjection,
        imageExtent: pixelProjection.getExtent()
      })
    })
  ],
  target: 'map',
  view: new ol.View2D({
    projection: pixelProjection,
    center: ol.extent.getCenter(pixelProjection.getExtent()),
    zoom: 2
  }) 
});