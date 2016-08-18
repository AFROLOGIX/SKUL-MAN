(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('MotifExclusionSearch', MotifExclusionSearch);

    MotifExclusionSearch.$inject = ['$resource'];

    function MotifExclusionSearch($resource) {
        var resourceUrl =  'api/_search/motif-exclusions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
