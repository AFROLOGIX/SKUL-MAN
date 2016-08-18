(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TrancheSearch', TrancheSearch);

    TrancheSearch.$inject = ['$resource'];

    function TrancheSearch($resource) {
        var resourceUrl =  'api/_search/tranches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
