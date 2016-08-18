(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('FormatMatricule', FormatMatricule);

    FormatMatricule.$inject = ['$resource'];

    function FormatMatricule ($resource) {
        var resourceUrl =  'api/format-matricules/:id';

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
