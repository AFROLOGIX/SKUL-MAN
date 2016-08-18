(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('Jour', Jour);

    Jour.$inject = ['$resource'];

    function Jour ($resource) {
        var resourceUrl =  'api/jours/:id';

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