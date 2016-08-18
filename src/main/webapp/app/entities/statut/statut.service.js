(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Statut', Statut);

    Statut.$inject = ['$resource'];

    function Statut ($resource) {
        var resourceUrl =  'api/statuts/:id';

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
