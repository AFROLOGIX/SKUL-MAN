(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TypeChambreSearch', TypeChambreSearch);

    TypeChambreSearch.$inject = ['$resource'];

    function TypeChambreSearch($resource) {
        var resourceUrl =  'api/_search/type-chambres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
