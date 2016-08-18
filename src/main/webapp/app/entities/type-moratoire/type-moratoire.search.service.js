(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TypeMoratoireSearch', TypeMoratoireSearch);

    TypeMoratoireSearch.$inject = ['$resource'];

    function TypeMoratoireSearch($resource) {
        var resourceUrl =  'api/_search/type-moratoires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
