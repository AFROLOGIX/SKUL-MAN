(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('option-bulletin-note', {
            parent: 'entity',
            url: '/option-bulletin-note',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.optionBulletinNote.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/option-bulletin-note/option-bulletin-notes.html',
                    controller: 'OptionBulletinNoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('optionBulletinNote');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('option-bulletin-note-detail', {
            parent: 'entity',
            url: '/option-bulletin-note/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.optionBulletinNote.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/option-bulletin-note/option-bulletin-note-detail.html',
                    controller: 'OptionBulletinNoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('optionBulletinNote');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OptionBulletinNote', function($stateParams, OptionBulletinNote) {
                    return OptionBulletinNote.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'option-bulletin-note',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('option-bulletin-note-detail.edit', {
            parent: 'option-bulletin-note-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option-bulletin-note/option-bulletin-note-dialog.html',
                    controller: 'OptionBulletinNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OptionBulletinNote', function(OptionBulletinNote) {
                            return OptionBulletinNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('option-bulletin-note.new', {
            parent: 'option-bulletin-note',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option-bulletin-note/option-bulletin-note-dialog.html',
                    controller: 'OptionBulletinNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nomEnseignant: null,
                                coef: null,
                                noteMin: null,
                                noteMax: null,
                                rangMatiere: null,
                                moyenneMatiere: null,
                                appreciation: null,
                                moyenneGeneraleClasse: null,
                                groupeMatiere: null,
                                photo: null,
                                totalEleve: null,
                                afficherSanction: null,
                                afficherMatricule: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('option-bulletin-note', null, { reload: true });
                }, function() {
                    $state.go('option-bulletin-note');
                });
            }]
        })
        .state('option-bulletin-note.edit', {
            parent: 'option-bulletin-note',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option-bulletin-note/option-bulletin-note-dialog.html',
                    controller: 'OptionBulletinNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OptionBulletinNote', function(OptionBulletinNote) {
                            return OptionBulletinNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('option-bulletin-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('option-bulletin-note.delete', {
            parent: 'option-bulletin-note',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option-bulletin-note/option-bulletin-note-delete-dialog.html',
                    controller: 'OptionBulletinNoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OptionBulletinNote', function(OptionBulletinNote) {
                            return OptionBulletinNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('option-bulletin-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
