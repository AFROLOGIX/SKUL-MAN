(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TypeChambre', TypeChambre);

    TypeChambre.$inject = ['$resource'];

    function TypeChambre ($resource) {
        var resourceUrl =  'api/type-chambres/:id';

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
