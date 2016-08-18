(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TypeAbonnementBus', TypeAbonnementBus);

    TypeAbonnementBus.$inject = ['$resource'];

    function TypeAbonnementBus ($resource) {
        var resourceUrl =  'api/type-abonnement-buses/:id';

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
