(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('SalleSearch', SalleSearch);

    SalleSearch.$inject = ['$resource'];

    function SalleSearch($resource) {
        var resourceUrl =  'api/_search/salles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
