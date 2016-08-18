(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('TransactionDiversesSearch', TransactionDiversesSearch);

    TransactionDiversesSearch.$inject = ['$resource'];

    function TransactionDiversesSearch($resource) {
        var resourceUrl =  'api/_search/transaction-diverses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
