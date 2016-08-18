(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('FichierSearch', FichierSearch);

    FichierSearch.$inject = ['$resource'];

    function FichierSearch($resource) {
        var resourceUrl =  'api/_search/fichiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
