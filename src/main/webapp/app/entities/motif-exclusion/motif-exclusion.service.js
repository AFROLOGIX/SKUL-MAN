(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('MotifExclusion', MotifExclusion);

    MotifExclusion.$inject = ['$resource'];

    function MotifExclusion ($resource) {
        var resourceUrl =  'api/motif-exclusions/:id';

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
