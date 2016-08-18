(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('VersementEleveSearch', VersementEleveSearch);

    VersementEleveSearch.$inject = ['$resource'];

    function VersementEleveSearch($resource) {
        var resourceUrl =  'api/_search/versement-eleves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
