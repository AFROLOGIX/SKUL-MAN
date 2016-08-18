(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginActionDeleteController',LoginActionDeleteController);

    LoginActionDeleteController.$inject = ['$uibModalInstance', 'entity', 'LoginAction'];

    function LoginActionDeleteController($uibModalInstance, entity, LoginAction) {
        var vm = this;

        vm.loginAction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LoginAction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
