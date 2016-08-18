(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('AbsenceEleve', AbsenceEleve);

    AbsenceEleve.$inject = ['$resource', 'DateUtils'];

    function AbsenceEleve ($resource, DateUtils) {
        var resourceUrl =  'api/absence-eleves/:id';

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
