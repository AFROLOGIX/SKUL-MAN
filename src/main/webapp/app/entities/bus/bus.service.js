(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Bus', Bus);

    Bus.$inject = ['$resource', 'DateUtils'];

    function Bus ($resource, DateUtils) {
        var resourceUrl =  'api/buses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateAcquisition = DateUtils.convertLocalDateFromServer(data.dateAcquisition);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateAcquisition = DateUtils.convertLocalDateToServer(data.dateAcquisition);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateAcquisition = DateUtils.convertLocalDateToServer(data.dateAcquisition);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
