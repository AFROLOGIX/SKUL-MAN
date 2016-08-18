(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('PaiementPersonnelSearch', PaiementPersonnelSearch);

    PaiementPersonnelSearch.$inject = ['$resource'];

    function PaiementPersonnelSearch($resource) {
        var resourceUrl =  'api/_search/paiement-personnels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
