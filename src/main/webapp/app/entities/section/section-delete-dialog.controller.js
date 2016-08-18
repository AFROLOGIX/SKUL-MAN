(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SectionDeleteController',SectionDeleteController);

    SectionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Section'];

    function SectionDeleteController($uibModalInstance, entity, Section) {
        var vm = this;

        vm.section = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Section.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
