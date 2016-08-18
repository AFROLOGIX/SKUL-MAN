(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Salaire', Salaire);

    Salaire.$inject = ['$resource'];

    function Salaire ($resource) {
        var resourceUrl =  'api/salaires/:id';

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
