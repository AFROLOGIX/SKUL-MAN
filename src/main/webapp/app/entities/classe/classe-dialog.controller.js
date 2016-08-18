(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ClasseDialogController', ClasseDialogController);

    ClasseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Classe', 'Serie', 'Niveau'];

    function ClasseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Classe, Serie, Niveau) {
        var vm = this;

        vm.classe = entity;
        vm.clear = clear;
        vm.save = save;
        vm.series = Serie.query({filter: 'classe-is-null'});
        $q.all([vm.classe.$promise, vm.series.$promise]).then(function() {
            if (!vm.classe.serie || !vm.classe.serie.id) {
                return $q.reject();
            }
            return Serie.get({id : vm.classe.serie.id}).$promise;
        }).then(function(serie) {
            vm.series.push(serie);
        });
        vm.niveaus = Niveau.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.classe.id !== null) {
                Classe.update(vm.classe, onSaveSuccess, onSaveError);
            } else {
                Classe.save(vm.classe, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:classeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
