(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Bulletin', Bulletin);

    Bulletin.$inject = ['$resource'];

    function Bulletin ($resource) {
        var resourceUrl =  'api/bulletins/:id';

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
