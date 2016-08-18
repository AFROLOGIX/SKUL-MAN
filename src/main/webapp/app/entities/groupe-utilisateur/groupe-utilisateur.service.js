(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('GroupeUtilisateur', GroupeUtilisateur);

    GroupeUtilisateur.$inject = ['$resource'];

    function GroupeUtilisateur ($resource) {
        var resourceUrl =  'api/groupe-utilisateurs/:id';

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
