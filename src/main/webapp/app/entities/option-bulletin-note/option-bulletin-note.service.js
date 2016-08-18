(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('OptionBulletinNote', OptionBulletinNote);

    OptionBulletinNote.$inject = ['$resource'];

    function OptionBulletinNote ($resource) {
        var resourceUrl =  'api/option-bulletin-notes/:id';

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
