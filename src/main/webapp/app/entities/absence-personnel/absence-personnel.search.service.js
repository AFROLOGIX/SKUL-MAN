(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('AbsencePersonnelSearch', AbsencePersonnelSearch);

    AbsencePersonnelSearch.$inject = ['$resource'];

    function AbsencePersonnelSearch($resource) {
        var resourceUrl =  'api/_search/absence-personnels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
