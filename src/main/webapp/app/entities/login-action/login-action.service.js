(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('LoginAction', LoginAction);

    LoginAction.$inject = ['$resource'];

    function LoginAction ($resource) {
        var resourceUrl =  'api/login-actions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
