(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('MoyenneTableauHonneur', MoyenneTableauHonneur);

    MoyenneTableauHonneur.$inject = ['$resource'];

    function MoyenneTableauHonneur ($resource) {
        var resourceUrl =  'api/moyenne-tableau-honneurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
