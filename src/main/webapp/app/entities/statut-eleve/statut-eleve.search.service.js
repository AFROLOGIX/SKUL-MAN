(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('StatutEleveSearch', StatutEleveSearch);

    StatutEleveSearch.$inject = ['$resource'];

    function StatutEleveSearch($resource) {
        var resourceUrl =  'api/_search/statut-eleves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
