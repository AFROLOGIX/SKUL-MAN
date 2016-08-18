(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Utilisateur', Utilisateur);

    Utilisateur.$inject = ['$resource', 'DateUtils'];

    function Utilisateur ($resource, DateUtils) {
        var resourceUrl =  'api/utilisateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateConnexion = DateUtils.convertDateTimeFromServer(data.dateConnexion);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
