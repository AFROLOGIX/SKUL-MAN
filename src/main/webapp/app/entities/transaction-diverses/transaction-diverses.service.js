(function() {
    'use strict';
    angular
        .module('skulmanApp')
        .factory('TransactionDiverses', TransactionDiverses);

    TransactionDiverses.$inject = ['$resource'];

    function TransactionDiverses ($resource) {
        var resourceUrl =  'api/transaction-diverses/:id';

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
