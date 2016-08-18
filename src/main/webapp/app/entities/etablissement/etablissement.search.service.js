(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('EtablissementSearch', EtablissementSearch);

    EtablissementSearch.$inject = ['$resource'];

    function EtablissementSearch($resource) {
        var resourceUrl =  'api/_search/etablissements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
