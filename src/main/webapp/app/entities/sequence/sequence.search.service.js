(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('SequenceSearch', SequenceSearch);

    SequenceSearch.$inject = ['$resource'];

    function SequenceSearch($resource) {
        var resourceUrl =  'api/_search/sequences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
