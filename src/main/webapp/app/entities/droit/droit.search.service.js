(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('DroitSearch', DroitSearch);

    DroitSearch.$inject = ['$resource'];

    function DroitSearch($resource) {
        var resourceUrl =  'api/_search/droits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
