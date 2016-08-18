(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TypeMoratoire', TypeMoratoire);

    TypeMoratoire.$inject = ['$resource'];

    function TypeMoratoire ($resource) {
        var resourceUrl =  'api/type-moratoires/:id';

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
