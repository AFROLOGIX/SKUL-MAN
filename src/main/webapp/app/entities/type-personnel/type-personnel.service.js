(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TypePersonnel', TypePersonnel);

    TypePersonnel.$inject = ['$resource'];

    function TypePersonnel ($resource) {
        var resourceUrl =  'api/type-personnels/:id';

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
