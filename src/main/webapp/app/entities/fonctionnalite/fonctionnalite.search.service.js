(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('FonctionnaliteSearch', FonctionnaliteSearch);

    FonctionnaliteSearch.$inject = ['$resource'];

    function FonctionnaliteSearch($resource) {
        var resourceUrl =  'api/_search/fonctionnalites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
