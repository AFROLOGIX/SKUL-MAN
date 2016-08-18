(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Etablissement', Etablissement);

    Etablissement.$inject = ['$resource', 'DateUtils'];

    function Etablissement ($resource, DateUtils) {
        var resourceUrl =  'api/etablissements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCreation = DateUtils.convertLocalDateFromServer(data.dateCreation);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateCreation = DateUtils.convertLocalDateToServer(data.dateCreation);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateCreation = DateUtils.convertLocalDateToServer(data.dateCreation);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
