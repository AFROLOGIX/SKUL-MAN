(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Enseignant', Enseignant);

    Enseignant.$inject = ['$resource'];

    function Enseignant ($resource) {
        var resourceUrl =  'api/enseignants/:id';

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
