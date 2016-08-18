(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Droit', Droit);

    Droit.$inject = ['$resource'];

    function Droit ($resource) {
        var resourceUrl =  'api/droits/:id';

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
