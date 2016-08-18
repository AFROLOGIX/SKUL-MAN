(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('ChambreEleveSearch', ChambreEleveSearch);

    ChambreEleveSearch.$inject = ['$resource'];

    function ChambreEleveSearch($resource) {
        var resourceUrl =  'api/_search/chambre-eleves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
