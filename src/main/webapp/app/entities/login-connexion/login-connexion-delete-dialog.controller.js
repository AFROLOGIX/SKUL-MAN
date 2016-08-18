(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginConnexionDeleteController',LoginConnexionDeleteController);

    LoginConnexionDeleteController.$inject = ['$uibModalInstance', 'entity', 'LoginConnexion'];

    function LoginConnexionDeleteController($uibModalInstance, entity, LoginConnexion) {
        var vm = this;

        vm.loginConnexion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LoginConnexion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
