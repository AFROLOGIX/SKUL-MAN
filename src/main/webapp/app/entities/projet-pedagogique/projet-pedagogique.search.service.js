(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('ProjetPedagogiqueSearch', ProjetPedagogiqueSearch);

    ProjetPedagogiqueSearch.$inject = ['$resource'];

    function ProjetPedagogiqueSearch($resource) {
        var resourceUrl =  'api/_search/projet-pedagogiques/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
