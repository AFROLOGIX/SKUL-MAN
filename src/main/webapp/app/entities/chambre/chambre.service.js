(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Chambre', Chambre);

    Chambre.$inject = ['$resource'];

    function Chambre ($resource) {
        var resourceUrl =  'api/chambres/:id';

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
