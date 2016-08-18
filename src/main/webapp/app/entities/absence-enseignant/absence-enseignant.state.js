(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('absence-enseignant', {
            parent: 'entity',
            url: '/absence-enseignant',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.absenceEnseignant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/absence-enseignant/absence-enseignants.html',
                    controller: 'AbsenceEnseignantController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('absenceEnseignant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('absence-enseignant-detail', {
            parent: 'entity',
            url: '/absence-enseignant/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.absenceEnseignant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/absence-enseignant/absence-enseignant-detail.html',
                    controller: 'AbsenceEnseignantDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('absenceEnseignant');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AbsenceEnseignant', function($stateParams, AbsenceEnseignant) {
                    return AbsenceEnseignant.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'absence-enseignant',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('absence-enseignant-detail.edit', {
            parent: 'absence-enseignant-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-enseignant/absence-enseignant-dialog.html',
                    controller: 'AbsenceEnseignantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AbsenceEnseignant', function(AbsenceEnseignant) {
                            return AbsenceEnseignant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('absence-enseignant.new', {
            parent: 'absence-enseignant',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-enseignant/absence-enseignant-dialog.html',
                    controller: 'AbsenceEnseignantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                plageHoraire: null,
                                justifiee: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('absence-enseignant', null, { reload: true });
                }, function() {
                    $state.go('absence-enseignant');
                });
            }]
        })
        .state('absence-enseignant.edit', {
            parent: 'absence-enseignant',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-enseignant/absence-enseignant-dialog.html',
                    controller: 'AbsenceEnseignantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AbsenceEnseignant', function(AbsenceEnseignant) {
                            return AbsenceEnseignant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('absence-enseignant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('absence-enseignant.delete', {
            parent: 'absence-enseignant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-enseignant/absence-enseignant-delete-dialog.html',
                    controller: 'AbsenceEnseignantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AbsenceEnseignant', function(AbsenceEnseignant) {
                            return AbsenceEnseignant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('absence-enseignant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
