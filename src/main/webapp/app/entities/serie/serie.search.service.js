(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('SerieSearch', SerieSearch);

    SerieSearch.$inject = ['$resource'];

    function SerieSearch($resource) {
        var resourceUrl =  'api/_search/series/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
