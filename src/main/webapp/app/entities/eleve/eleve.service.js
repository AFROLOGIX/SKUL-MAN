(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Eleve', Eleve);

    Eleve.$inject = ['$resource', 'DateUtils'];

    function Eleve ($resource, DateUtils) {
        var resourceUrl =  'api/eleves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateNaiss = DateUtils.convertLocalDateFromServer(data.dateNaiss);
                        data.createAt = DateUtils.convertDateTimeFromServer(data.createAt);
                        data.updateAt = DateUtils.convertDateTimeFromServer(data.updateAt);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateNaiss = DateUtils.convertLocalDateToServer(data.dateNaiss);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateNaiss = DateUtils.convertLocalDateToServer(data.dateNaiss);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
