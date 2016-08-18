(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Serie', Serie);

    Serie.$inject = ['$resource'];

    function Serie ($resource) {
        var resourceUrl =  'api/series/:id';

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
