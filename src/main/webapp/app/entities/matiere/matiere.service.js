(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Matiere', Matiere);

    Matiere.$inject = ['$resource'];

    function Matiere ($resource) {
        var resourceUrl =  'api/matieres/:id';

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
