(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('AgentAdministratifSearch', AgentAdministratifSearch);

    AgentAdministratifSearch.$inject = ['$resource'];

    function AgentAdministratifSearch($resource) {
        var resourceUrl =  'api/_search/agent-administratifs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
