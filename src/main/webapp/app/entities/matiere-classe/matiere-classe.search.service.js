(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('MatiereClasseSearch', MatiereClasseSearch);

    MatiereClasseSearch.$inject = ['$resource'];

    function MatiereClasseSearch($resource) {
        var resourceUrl =  'api/_search/matiere-classes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
