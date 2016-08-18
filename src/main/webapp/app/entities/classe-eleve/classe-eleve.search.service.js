(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('ClasseEleveSearch', ClasseEleveSearch);

    ClasseEleveSearch.$inject = ['$resource'];

    function ClasseEleveSearch($resource) {
        var resourceUrl =  'api/_search/classe-eleves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
