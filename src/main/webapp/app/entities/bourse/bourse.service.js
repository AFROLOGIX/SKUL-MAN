(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Bourse', Bourse);

    Bourse.$inject = ['$resource'];

    function Bourse ($resource) {
        var resourceUrl =  'api/bourses/:id';

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
