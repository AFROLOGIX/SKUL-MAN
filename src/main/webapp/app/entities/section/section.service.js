(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Section', Section);

    Section.$inject = ['$resource'];

    function Section ($resource) {
        var resourceUrl =  'api/sections/:id';

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
