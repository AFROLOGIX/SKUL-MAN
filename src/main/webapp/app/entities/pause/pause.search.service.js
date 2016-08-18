(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('PauseSearch', PauseSearch);

    PauseSearch.$inject = ['$resource'];

    function PauseSearch($resource) {
        var resourceUrl =  'api/_search/pauses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
