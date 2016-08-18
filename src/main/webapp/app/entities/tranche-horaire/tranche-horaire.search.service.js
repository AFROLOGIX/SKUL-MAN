(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TrancheHoraireSearch', TrancheHoraireSearch);

    TrancheHoraireSearch.$inject = ['$resource'];

    function TrancheHoraireSearch($resource) {
        var resourceUrl =  'api/_search/tranche-horaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
