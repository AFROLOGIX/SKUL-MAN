(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('LoginConnexion', LoginConnexion);

    LoginConnexion.$inject = ['$resource', 'DateUtils'];

    function LoginConnexion ($resource, DateUtils) {
        var resourceUrl =  'api/login-connexions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.loginTime = DateUtils.convertDateTimeFromServer(data.loginTime);
                        data.dateEchec = DateUtils.convertDateTimeFromServer(data.dateEchec);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
