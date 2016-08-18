(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TypeEpreuveSearch', TypeEpreuveSearch);

    TypeEpreuveSearch.$inject = ['$resource'];

    function TypeEpreuveSearch($resource) {
        var resourceUrl =  'api/_search/type-epreuves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
