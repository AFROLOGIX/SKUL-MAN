(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TypeTrancheHoraire', TypeTrancheHoraire);

    TypeTrancheHoraire.$inject = ['$resource', 'DateUtils'];

    function TypeTrancheHoraire ($resource, DateUtils) {
        var resourceUrl =  'api/type-tranche-horaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.heureDeb = DateUtils.convertDateTimeFromServer(data.heureDeb);
                        data.heureFin = DateUtils.convertDateTimeFromServer(data.heureFin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
