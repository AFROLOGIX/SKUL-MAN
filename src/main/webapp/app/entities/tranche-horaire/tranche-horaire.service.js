(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TrancheHoraire', TrancheHoraire);

    TrancheHoraire.$inject = ['$resource'];

    function TrancheHoraire ($resource) {
        var resourceUrl =  'api/tranche-horaires/:id';

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
