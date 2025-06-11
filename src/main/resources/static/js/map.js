ymaps.ready(init);

function init() {
    var myMap = new ymaps.Map('map', {
        center: [55.753994, 37.622093],
        zoom: 9,
        controls: ['searchControl']
    });

    var searchControl = myMap.controls.get('searchControl');

    searchControl.events.add('resultselect', function (e) {
        var index = e.get('index');

        searchControl.getResult(index).then(function (result) {
            var geoObjectType = result.properties.get('metaDataProperty').GeocoderMetaData.kind;

            if (geoObjectType === 'house') {
                myMap.geoObjects.removeAll();
                myMap.geoObjects.add(result);

                var bounds = result.properties.get('boundedBy');
                myMap.setBounds(bounds, { checkZoomRange: true });

                result.options.set('preset', 'islands#darkBlueDotIconWithCaption');
                result.properties.set('iconCaption', result.getAddressLine());

                document.getElementById('selectedAddress').value = result.getAddressLine();
            } else {
                alert('Пожалуйста, выберите точный адрес с домом.');
            }
        }).catch(function (err) {
            alert('Ошибка получения результата: ' + err.message);
        });
    });

    searchControl.events.add('searchstart', function () {
        myMap.geoObjects.removeAll();
        document.getElementById('selectedAddress').value = 'Не выбран';
    });
}