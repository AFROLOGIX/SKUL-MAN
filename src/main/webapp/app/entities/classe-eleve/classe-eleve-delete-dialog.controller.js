(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ClasseEleveDeleteController',ClasseEleveDeleteController);

    ClasseEleveDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClasseEleve'];

    function ClasseEleveDeleteController($uibModalInstance, entity, ClasseEleve) {
        var vm = this;

        vm.classeEleve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClasseEleve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
