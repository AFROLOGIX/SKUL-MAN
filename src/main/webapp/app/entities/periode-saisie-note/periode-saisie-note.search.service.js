(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('PeriodeSaisieNoteSearch', PeriodeSaisieNoteSearch);

    PeriodeSaisieNoteSearch.$inject = ['$resource'];

    function PeriodeSaisieNoteSearch($resource) {
        var resourceUrl =  'api/_search/periode-saisie-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
