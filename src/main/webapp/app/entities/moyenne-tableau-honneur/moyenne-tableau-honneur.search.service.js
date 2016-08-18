(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('MoyenneTableauHonneurSearch', MoyenneTableauHonneurSearch);

    MoyenneTableauHonneurSearch.$inject = ['$resource'];

    function MoyenneTableauHonneurSearch($resource) {
        var resourceUrl =  'api/_search/moyenne-tableau-honneurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
