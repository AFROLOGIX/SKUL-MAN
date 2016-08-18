(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('AnneeScolaireSearch', AnneeScolaireSearch);

    AnneeScolaireSearch.$inject = ['$resource'];

    function AnneeScolaireSearch($resource) {
        var resourceUrl =  'api/_search/annee-scolaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
