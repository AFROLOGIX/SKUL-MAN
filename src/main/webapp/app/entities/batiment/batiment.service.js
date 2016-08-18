(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Batiment', Batiment);

    Batiment.$inject = ['$resource'];

    function Batiment ($resource) {
        var resourceUrl =  'api/batiments/:id';

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
