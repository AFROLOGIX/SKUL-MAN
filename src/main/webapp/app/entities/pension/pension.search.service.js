(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('PensionSearch', PensionSearch);

    PensionSearch.$inject = ['$resource'];

    function PensionSearch($resource) {
        var resourceUrl =  'api/_search/pensions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
