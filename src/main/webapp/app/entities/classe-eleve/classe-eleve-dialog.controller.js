(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ClasseEleveDialogController', ClasseEleveDialogController);

    ClasseEleveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ClasseEleve', 'Classe', 'Eleve'];

    function ClasseEleveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ClasseEleve, Classe, Eleve) {
        var vm = this;

        vm.classeEleve = entity;
        vm.clear = clear;
        vm.save = save;
        vm.classes = Classe.query({filter: 'classeeleve-is-null'});
        $q.all([vm.classeEleve.$promise, vm.classes.$promise]).then(function() {
            if (!vm.classeEleve.classe || !vm.classeEleve.classe.id) {
                return $q.reject();
            }
            return Classe.get({id : vm.classeEleve.classe.id}).$promise;
        }).then(function(classe) {
            vm.classes.push(classe);
        });
        vm.eleves = Eleve.query({filter: 'classeeleve-is-null'});
        $q.all([vm.classeEleve.$promise, vm.eleves.$promise]).then(function() {
            if (!vm.classeEleve.eleve || !vm.classeEleve.eleve.id) {
                return $q.reject();
            }
            return Eleve.get({id : vm.classeEleve.eleve.id}).$promise;
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
            if (vm.classeEleve.id !== null) {
                ClasseEleve.update(vm.classeEleve, onSaveSuccess, onSaveError);
            } else {
                ClasseEleve.save(vm.classeEleve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:classeEleveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
