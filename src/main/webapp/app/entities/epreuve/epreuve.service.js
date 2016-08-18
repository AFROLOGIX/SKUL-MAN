(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Epreuve', Epreuve);

    Epreuve.$inject = ['$resource'];

    function Epreuve ($resource) {
        var resourceUrl =  'api/epreuves/:id';

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
