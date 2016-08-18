(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoratoireDialogController', MoratoireDialogController);

    MoratoireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Moratoire', 'TypeMoratoire', 'Eleve'];

    function MoratoireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Moratoire, TypeMoratoire, Eleve) {
        var vm = this;

        vm.moratoire = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.typemoratoires = TypeMoratoire.query({filter: 'moratoire-is-null'});
        $q.all([vm.moratoire.$promise, vm.typemoratoires.$promise]).then(function() {
            if (!vm.moratoire.typeMoratoire || !vm.moratoire.typeMoratoire.id) {
                return $q.reject();
            }
            return TypeMoratoire.get({id : vm.moratoire.typeMoratoire.id}).$promise;
        }).then(function(typeMoratoire) {
            vm.typemoratoires.push(typeMoratoire);
        });
        vm.eleves = Eleve.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.moratoire.id !== null) {
                Moratoire.update(vm.moratoire, onSaveSuccess, onSaveError);
            } else {
                Moratoire.save(vm.moratoire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:moratoireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.nouvelleDate = false;
        vm.datePickerOpenStatus.createAt = false;
        vm.datePickerOpenStatus.updateAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
