(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Cycle', Cycle);

    Cycle.$inject = ['$resource'];

    function Cycle ($resource) {
        var resourceUrl =  'api/cycles/:id';

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
