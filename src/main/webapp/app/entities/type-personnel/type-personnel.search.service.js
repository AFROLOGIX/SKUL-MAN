(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TypePersonnelSearch', TypePersonnelSearch);

    TypePersonnelSearch.$inject = ['$resource'];

    function TypePersonnelSearch($resource) {
        var resourceUrl =  'api/_search/type-personnels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
