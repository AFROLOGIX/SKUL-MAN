(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('ReligionSearch', ReligionSearch);

    ReligionSearch.$inject = ['$resource'];

    function ReligionSearch($resource) {
        var resourceUrl =  'api/_search/religions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
