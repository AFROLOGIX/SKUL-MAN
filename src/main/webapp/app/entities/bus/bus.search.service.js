(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('BusSearch', BusSearch);

    BusSearch.$inject = ['$resource'];

    function BusSearch($resource) {
        var resourceUrl =  'api/_search/buses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
