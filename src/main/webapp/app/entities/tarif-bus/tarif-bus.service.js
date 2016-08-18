(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TarifBus', TarifBus);

    TarifBus.$inject = ['$resource'];

    function TarifBus ($resource) {
        var resourceUrl =  'api/tarif-buses/:id';

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
