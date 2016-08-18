(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('RegimePension', RegimePension);

    RegimePension.$inject = ['$resource'];

    function RegimePension ($resource) {
        var resourceUrl =  'api/regime-pensions/:id';

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
