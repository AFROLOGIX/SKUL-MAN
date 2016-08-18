(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('NoteDialogController', NoteDialogController);

    NoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Note', 'Sequence', 'Cours'];

    function NoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Note, Sequence, Cours) {
        var vm = this;

        vm.note = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sequences = Sequence.query({filter: 'note-is-null'});
        $q.all([vm.note.$promise, vm.sequences.$promise]).then(function() {
            if (!vm.note.sequence || !vm.note.sequence.id) {
                return $q.reject();
            }
            return Sequence.get({id : vm.note.sequence.id}).$promise;
        }).then(function(sequence) {
            vm.sequences.push(sequence);
        });
        vm.cours = Cours.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.note.id !== null) {
                Note.update(vm.note, onSaveSuccess, onSaveError);
            } else {
                Note.save(vm.note, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:noteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createAt = false;
        vm.datePickerOpenStatus.updateAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
