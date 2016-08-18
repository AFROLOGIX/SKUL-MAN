(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('StatutSearch', StatutSearch);

    StatutSearch.$inject = ['$resource'];

    function StatutSearch($resource) {
        var resourceUrl =  'api/_search/statuts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
