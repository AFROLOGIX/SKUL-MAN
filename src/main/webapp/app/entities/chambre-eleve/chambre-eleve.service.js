(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('ChambreEleve', ChambreEleve);

    ChambreEleve.$inject = ['$resource'];

    function ChambreEleve ($resource) {
        var resourceUrl =  'api/chambre-eleves/:id';

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
