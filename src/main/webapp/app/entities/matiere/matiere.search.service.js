(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('MatiereSearch', MatiereSearch);

    MatiereSearch.$inject = ['$resource'];

    function MatiereSearch($resource) {
        var resourceUrl =  'api/_search/matieres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
