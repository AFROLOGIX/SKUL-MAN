(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('PersonnelSearch', PersonnelSearch);

    PersonnelSearch.$inject = ['$resource'];

    function PersonnelSearch($resource) {
        var resourceUrl =  'api/_search/personnels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
