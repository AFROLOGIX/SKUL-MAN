(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('CycleSearch', CycleSearch);

    CycleSearch.$inject = ['$resource'];

    function CycleSearch($resource) {
        var resourceUrl =  'api/_search/cycles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
