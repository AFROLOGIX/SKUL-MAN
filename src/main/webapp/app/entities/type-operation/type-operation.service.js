(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TypeOperation', TypeOperation);

    TypeOperation.$inject = ['$resource'];

    function TypeOperation ($resource) {
        var resourceUrl =  'api/type-operations/:id';

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
