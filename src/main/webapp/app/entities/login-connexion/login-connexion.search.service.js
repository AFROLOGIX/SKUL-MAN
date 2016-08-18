(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('LoginConnexionSearch', LoginConnexionSearch);

    LoginConnexionSearch.$inject = ['$resource'];

    function LoginConnexionSearch($resource) {
        var resourceUrl =  'api/_search/login-connexions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
