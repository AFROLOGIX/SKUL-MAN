(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('EpreuveSearch', EpreuveSearch);

    EpreuveSearch.$inject = ['$resource'];

    function EpreuveSearch($resource) {
        var resourceUrl =  'api/_search/epreuves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
