(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('ClasseEleve', ClasseEleve);

    ClasseEleve.$inject = ['$resource'];

    function ClasseEleve ($resource) {
        var resourceUrl =  'api/classe-eleves/:id';

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
