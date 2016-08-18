(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('AbsenceEnseignantSearch', AbsenceEnseignantSearch);

    AbsenceEnseignantSearch.$inject = ['$resource'];

    function AbsenceEnseignantSearch($resource) {
        var resourceUrl =  'api/_search/absence-enseignants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
