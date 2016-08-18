(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('BulletinSearch', BulletinSearch);

    BulletinSearch.$inject = ['$resource'];

    function BulletinSearch($resource) {
        var resourceUrl =  'api/_search/bulletins/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
