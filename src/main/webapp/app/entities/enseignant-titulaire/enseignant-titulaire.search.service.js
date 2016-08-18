(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('EnseignantTitulaireSearch', EnseignantTitulaireSearch);

    EnseignantTitulaireSearch.$inject = ['$resource'];

    function EnseignantTitulaireSearch($resource) {
        var resourceUrl =  'api/_search/enseignant-titulaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
