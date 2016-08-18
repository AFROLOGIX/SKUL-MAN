(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Personnel', Personnel);

    Personnel.$inject = ['$resource', 'DateUtils'];

    function Personnel ($resource, DateUtils) {
        var resourceUrl =  'api/personnels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateAdmission = DateUtils.convertLocalDateFromServer(data.dateAdmission);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateAdmission = DateUtils.convertLocalDateToServer(data.dateAdmission);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateAdmission = DateUtils.convertLocalDateToServer(data.dateAdmission);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
