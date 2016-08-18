(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Moratoire', Moratoire);

    Moratoire.$inject = ['$resource', 'DateUtils'];

    function Moratoire ($resource, DateUtils) {
        var resourceUrl =  'api/moratoires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.nouvelleDate = DateUtils.convertLocalDateFromServer(data.nouvelleDate);
                        data.createAt = DateUtils.convertDateTimeFromServer(data.createAt);
                        data.updateAt = DateUtils.convertDateTimeFromServer(data.updateAt);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.nouvelleDate = DateUtils.convertLocalDateToServer(data.nouvelleDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.nouvelleDate = DateUtils.convertLocalDateToServer(data.nouvelleDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
