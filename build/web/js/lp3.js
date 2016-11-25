var meuMapa;
function init() {
    meuMapa = new ol.Map({
        target: 'MeuMapa',
        renderer: 'canvas',
        view: new ol.View({
            projection: 'EPSG:900913',
            center: [-5193252.61684, -2698365.38923],
            zoom: 18
        })
    });
    var openStreetMapLayer = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    meuMapa.addLayer(openStreetMapLayer);


    var bingLayer = new ol.layer.Tile({
        source: new ol.source.BingMaps({
            imagerySet: 'Aerial',
            key: 'Ak-dzM4wZjSqTlzveKz5u0d4IQ4bRzVI309GxmkgSVr1ewS6iPSrOvOKhA-CJlm3'
        })
    });
    bingLayer.setOpacity(.3);
    meuMapa.addLayer(bingLayer);

    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point([-5193252.61684, -2698365.38923]),
        name: 'R2D2'
    });
    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 0.75,
            src: 'dados/r2d2.png'
        }))
    });
    iconFeature.setStyle(iconStyle);
    var vectorSource = new ol.source.Vector({
        features: [iconFeature]
    });
    var vectorLayer = new ol.layer.Vector({
        source: vectorSource
    });
    meuMapa.addLayer(vectorLayer);
    var element = document.getElementById('popup');
    var popup = new ol.Overlay({
        element: element,
        positioning: 'bottom-center',
        stopEvent: false
    });
    meuMapa.addOverlay(popup);
    meuMapa.on('click', function (evt) {
        var feature = meuMapa.forEachFeatureAtPixel(evt.pixel,
                function (feature, layer) {
                    return feature;
                });
        if (feature) {
            popup.setPosition(evt.coordinate);
            $(element).popover({
                'placement': 'top',
                'html': true,
                'content': feature.get('name')
            });
            $(element).popover('show');
        } else {
            $(element).popover('destroy');
        }
    });
    meuMapa.on('pointermove', function (e) {
        if (e.dragging) {
            $(element).popover('destroy');
            return;
        }
        var pixel = meuMapa.getEventPixel(e.originalEvent);
        var hit = meuMapa.hasFeatureAtPixel(pixel);
        meuMapa.getTarget().style.cursor = hit ? 'pointer' : '';
    });

}