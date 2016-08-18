(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TypeOperationSearch', TypeOperationSearch);

    TypeOperationSearch.$inject = ['$resource'];

    function TypeOperationSearch($resource) {
        var resourceUrl =  'api/_search/type-operations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
