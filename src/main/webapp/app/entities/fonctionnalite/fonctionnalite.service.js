(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Fonctionnalite', Fonctionnalite);

    Fonctionnalite.$inject = ['$resource'];

    function Fonctionnalite ($resource) {
        var resourceUrl =  'api/fonctionnalites/:id';

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
