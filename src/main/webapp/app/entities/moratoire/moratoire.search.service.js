(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('MoratoireSearch', MoratoireSearch);

    MoratoireSearch.$inject = ['$resource'];

    function MoratoireSearch($resource) {
        var resourceUrl =  'api/_search/moratoires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
