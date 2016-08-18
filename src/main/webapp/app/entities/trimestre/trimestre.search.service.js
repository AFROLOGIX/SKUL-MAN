(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TrimestreSearch', TrimestreSearch);

    TrimestreSearch.$inject = ['$resource'];

    function TrimestreSearch($resource) {
        var resourceUrl =  'api/_search/trimestres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
