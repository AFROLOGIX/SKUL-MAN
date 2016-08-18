(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('CoursSearch', CoursSearch);

    CoursSearch.$inject = ['$resource'];

    function CoursSearch($resource) {
        var resourceUrl =  'api/_search/cours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
