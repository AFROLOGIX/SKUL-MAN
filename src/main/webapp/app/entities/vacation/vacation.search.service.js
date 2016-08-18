(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('VacationSearch', VacationSearch);

    VacationSearch.$inject = ['$resource'];

    function VacationSearch($resource) {
        var resourceUrl =  'api/_search/vacations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
