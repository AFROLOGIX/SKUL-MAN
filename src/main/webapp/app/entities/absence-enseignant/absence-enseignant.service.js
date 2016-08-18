(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('AbsenceEnseignant', AbsenceEnseignant);

    AbsenceEnseignant.$inject = ['$resource', 'DateUtils'];

    function AbsenceEnseignant ($resource, DateUtils) {
        var resourceUrl =  'api/absence-enseignants/:id';

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
