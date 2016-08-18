(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Pause', Pause);

    Pause.$inject = ['$resource', 'DateUtils'];

    function Pause ($resource, DateUtils) {
        var resourceUrl =  'api/pauses/:id';

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
