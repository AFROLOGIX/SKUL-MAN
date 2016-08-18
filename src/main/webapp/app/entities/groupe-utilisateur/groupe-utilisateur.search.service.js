(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('GroupeUtilisateurSearch', GroupeUtilisateurSearch);

    GroupeUtilisateurSearch.$inject = ['$resource'];

    function GroupeUtilisateurSearch($resource) {
        var resourceUrl =  'api/_search/groupe-utilisateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
