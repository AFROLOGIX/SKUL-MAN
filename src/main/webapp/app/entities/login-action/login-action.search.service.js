(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('LoginActionSearch', LoginActionSearch);

    LoginActionSearch.$inject = ['$resource'];

    function LoginActionSearch($resource) {
        var resourceUrl =  'api/_search/login-actions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
