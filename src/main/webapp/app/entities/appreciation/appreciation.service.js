(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Appreciation', Appreciation);

    Appreciation.$inject = ['$resource'];

    function Appreciation ($resource) {
        var resourceUrl =  'api/appreciations/:id';

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
