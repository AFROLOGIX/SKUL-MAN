(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Niveau', Niveau);

    Niveau.$inject = ['$resource'];

    function Niveau ($resource) {
        var resourceUrl =  'api/niveaus/:id';

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
