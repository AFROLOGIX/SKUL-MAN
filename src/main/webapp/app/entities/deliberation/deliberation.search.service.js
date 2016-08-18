(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('DeliberationSearch', DeliberationSearch);

    DeliberationSearch.$inject = ['$resource'];

    function DeliberationSearch($resource) {
        var resourceUrl =  'api/_search/deliberations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
