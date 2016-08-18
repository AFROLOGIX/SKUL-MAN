(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TypeTrancheHoraireSearch', TypeTrancheHoraireSearch);

    TypeTrancheHoraireSearch.$inject = ['$resource'];

    function TypeTrancheHoraireSearch($resource) {
        var resourceUrl =  'api/_search/type-tranche-horaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
