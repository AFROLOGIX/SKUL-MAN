(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('ParametreEtablissementSearch', ParametreEtablissementSearch);

    ParametreEtablissementSearch.$inject = ['$resource'];

    function ParametreEtablissementSearch($resource) {
        var resourceUrl =  'api/_search/parametre-etablissements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
