(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('BatimentSearch', BatimentSearch);

    BatimentSearch.$inject = ['$resource'];

    function BatimentSearch($resource) {
        var resourceUrl =  'api/_search/batiments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
