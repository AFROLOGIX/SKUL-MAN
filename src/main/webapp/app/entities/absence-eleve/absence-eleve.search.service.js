(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('AbsenceEleveSearch', AbsenceEleveSearch);

    AbsenceEleveSearch.$inject = ['$resource'];

    function AbsenceEleveSearch($resource) {
        var resourceUrl =  'api/_search/absence-eleves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
