(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BusDialogController', BusDialogController);

    BusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Bus', 'TarifBus'];

    function BusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Bus, TarifBus) {
        var vm = this;

        vm.bus = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tarifbuses = TarifBus.query({filter: 'bus-is-null'});
        $q.all([vm.bus.$promise, vm.tarifbuses.$promise]).then(function() {
            if (!vm.bus.tarifBus || !vm.bus.tarifBus.id) {
                return $q.reject();
            }
            return TarifBus.get({id : vm.bus.tarifBus.id}).$promise;
        }).then(function(tarifBus) {
            vm.tarifbuses.push(tarifBus);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bus.id !== null) {
                Bus.update(vm.bus, onSaveSuccess, onSaveError);
            } else {
                Bus.save(vm.bus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:busUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateAcquisition = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
