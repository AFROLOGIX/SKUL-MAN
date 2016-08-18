(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BulletinController', BulletinController);

    BulletinController.$inject = ['$scope', '$state', 'Bulletin', 'BulletinSearch'];

    function BulletinController ($scope, $state, Bulletin, BulletinSearch) {
        var vm = this;
        
        vm.bulletins = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Bulletin.query(function(result) {
                vm.bulletins = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BulletinSearch.query({query: vm.searchQuery}, function(result) {
                vm.bulletins = result;
            });
        }    }
})();
