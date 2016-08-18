(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .factory('OptionBulletinNoteSearch', OptionBulletinNoteSearch);

    OptionBulletinNoteSearch.$inject = ['$resource'];

    function OptionBulletinNoteSearch($resource) {
        var resourceUrl =  'api/_search/option-bulletin-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
