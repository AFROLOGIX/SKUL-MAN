(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('RegimePensionSearch', RegimePensionSearch);

    RegimePensionSearch.$inject = ['$resource'];

    function RegimePensionSearch($resource) {
        var resourceUrl =  'api/_search/regime-pensions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
