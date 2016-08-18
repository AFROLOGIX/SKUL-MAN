(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('ChambreSearch', ChambreSearch);

    ChambreSearch.$inject = ['$resource'];

    function ChambreSearch($resource) {
        var resourceUrl =  'api/_search/chambres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
