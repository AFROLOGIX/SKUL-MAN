(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TarifBusSearch', TarifBusSearch);

    TarifBusSearch.$inject = ['$resource'];

    function TarifBusSearch($resource) {
        var resourceUrl =  'api/_search/tarif-buses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
