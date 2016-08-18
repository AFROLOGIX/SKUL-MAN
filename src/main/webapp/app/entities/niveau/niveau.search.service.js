(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('NiveauSearch', NiveauSearch);

    NiveauSearch.$inject = ['$resource'];

    function NiveauSearch($resource) {
        var resourceUrl =  'api/_search/niveaus/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
