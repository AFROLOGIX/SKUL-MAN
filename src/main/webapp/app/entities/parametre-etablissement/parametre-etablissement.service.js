(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('ParametreEtablissement', ParametreEtablissement);

    ParametreEtablissement.$inject = ['$resource', 'DateUtils'];

    function ParametreEtablissement ($resource, DateUtils) {
        var resourceUrl =  'api/parametre-etablissements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.heureDebCours = DateUtils.convertDateTimeFromServer(data.heureDebCours);
                        data.heureFinCours = DateUtils.convertDateTimeFromServer(data.heureFinCours);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
