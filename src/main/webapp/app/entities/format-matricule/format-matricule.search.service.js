(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('FormatMatriculeSearch', FormatMatriculeSearch);

    FormatMatriculeSearch.$inject = ['$resource'];

    function FormatMatriculeSearch($resource) {
        var resourceUrl =  'api/_search/format-matricules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
