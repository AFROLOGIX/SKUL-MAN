(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Deliberation', Deliberation);

    Deliberation.$inject = ['$resource', 'DateUtils'];

    function Deliberation ($resource, DateUtils) {
        var resourceUrl =  'api/deliberations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createAt = DateUtils.convertDateTimeFromServer(data.createAt);
                        data.updateAt = DateUtils.convertDateTimeFromServer(data.updateAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
