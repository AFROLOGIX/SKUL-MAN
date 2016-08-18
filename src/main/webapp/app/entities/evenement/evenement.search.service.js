(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('EvenementSearch', EvenementSearch);

    EvenementSearch.$inject = ['$resource'];

    function EvenementSearch($resource) {
        var resourceUrl =  'api/_search/evenements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
