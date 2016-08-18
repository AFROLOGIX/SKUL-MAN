(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('MatiereClasse', MatiereClasse);

    MatiereClasse.$inject = ['$resource'];

    function MatiereClasse ($resource) {
        var resourceUrl =  'api/matiere-classes/:id';

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
