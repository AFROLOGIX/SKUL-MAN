(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Salle', Salle);

    Salle.$inject = ['$resource'];

    function Salle ($resource) {
        var resourceUrl =  'api/salles/:id';

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
