(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('StatutEleve', StatutEleve);

    StatutEleve.$inject = ['$resource'];

    function StatutEleve ($resource) {
        var resourceUrl =  'api/statut-eleves/:id';

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
