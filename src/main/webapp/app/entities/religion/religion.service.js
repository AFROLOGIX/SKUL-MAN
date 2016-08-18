(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Religion', Religion);

    Religion.$inject = ['$resource'];

    function Religion ($resource) {
        var resourceUrl =  'api/religions/:id';

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
