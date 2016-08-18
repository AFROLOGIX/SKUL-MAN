(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ProjetPedagogiqueDialogController', ProjetPedagogiqueDialogController);

    ProjetPedagogiqueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ProjetPedagogique', 'Classe', 'Enseignant'];

    function ProjetPedagogiqueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ProjetPedagogique, Classe, Enseignant) {
        var vm = this;

        vm.projetPedagogique = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.classes = Classe.query({filter: 'projetpedagogique-is-null'});
        $q.all([vm.projetPedagogique.$promise, vm.classes.$promise]).then(function() {
            if (!vm.projetPedagogique.classe || !vm.projetPedagogique.classe.id) {
                return $q.reject();
            }
            return Classe.get({id : vm.projetPedagogique.classe.id}).$promise;
        }).then(function(classe) {
            vm.classes.push(classe);
        });
        vm.enseignants = Enseignant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projetPedagogique.id !== null) {
                ProjetPedagogique.update(vm.projetPedagogique, onSaveSuccess, onSaveError);
            } else {
                ProjetPedagogique.save(vm.projetPedagogique, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:projetPedagogiqueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDeb = false;
        vm.datePickerOpenStatus.dateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
