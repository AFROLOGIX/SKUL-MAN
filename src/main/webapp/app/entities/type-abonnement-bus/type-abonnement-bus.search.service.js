(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TypeAbonnementBusSearch', TypeAbonnementBusSearch);

    TypeAbonnementBusSearch.$inject = ['$resource'];

    function TypeAbonnementBusSearch($resource) {
        var resourceUrl =  'api/_search/type-abonnement-buses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
