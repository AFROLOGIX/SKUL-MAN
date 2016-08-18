(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('periode-saisie-note', {
            parent: 'entity',
            url: '/periode-saisie-note',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.periodeSaisieNote.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/periode-saisie-note/periode-saisie-notes.html',
                    controller: 'PeriodeSaisieNoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('periodeSaisieNote');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('periode-saisie-note-detail', {
            parent: 'entity',
            url: '/periode-saisie-note/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.periodeSaisieNote.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/periode-saisie-note/periode-saisie-note-detail.html',
                    controller: 'PeriodeSaisieNoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('periodeSaisieNote');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PeriodeSaisieNote', function($stateParams, PeriodeSaisieNote) {
                    return PeriodeSaisieNote.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'periode-saisie-note',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('periode-saisie-note-detail.edit', {
            parent: 'periode-saisie-note-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/periode-saisie-note/periode-saisie-note-dialog.html',
                    controller: 'PeriodeSaisieNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PeriodeSaisieNote', function(PeriodeSaisieNote) {
                            return PeriodeSaisieNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('periode-saisie-note.new', {
            parent: 'periode-saisie-note',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/periode-saisie-note/periode-saisie-note-dialog.html',
                    controller: 'PeriodeSaisieNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                typePeriode: null,
                                dateDeb: null,
                                dateFin: null,
                                anneeScolaire: null,
                                isClose: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('periode-saisie-note', null, { reload: true });
                }, function() {
                    $state.go('periode-saisie-note');
                });
            }]
        })
        .state('periode-saisie-note.edit', {
            parent: 'periode-saisie-note',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/periode-saisie-note/periode-saisie-note-dialog.html',
                    controller: 'PeriodeSaisieNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PeriodeSaisieNote', function(PeriodeSaisieNote) {
                            return PeriodeSaisieNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('periode-saisie-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('periode-saisie-note.delete', {
            parent: 'periode-saisie-note',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/periode-saisie-note/periode-saisie-note-delete-dialog.html',
                    controller: 'PeriodeSaisieNoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PeriodeSaisieNote', function(PeriodeSaisieNote) {
                            return PeriodeSaisieNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('periode-saisie-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
