(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ParentDialogController', ParentDialogController);

    ParentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Parent', 'Eleve'];

    function ParentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Parent, Eleve) {
        var vm = this;

        vm.parent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.eleves = Eleve.query({filter: 'parent-is-null'});
        $q.all([vm.parent.$promise, vm.eleves.$promise]).then(function() {
            if (!vm.parent.eleve || !vm.parent.eleve.id) {
                return $q.reject();
            }
            return Eleve.get({id : vm.parent.eleve.id}).$promise;
        }).then(function(eleve) {
            vm.eleves.push(eleve);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.parent.id !== null) {
                Parent.update(vm.parent, onSaveSuccess, onSaveError);
            } else {
                Parent.save(vm.parent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:parentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
