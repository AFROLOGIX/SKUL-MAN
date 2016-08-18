(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('enseignant-titulaire', {
            parent: 'entity',
            url: '/enseignant-titulaire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.enseignantTitulaire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enseignant-titulaire/enseignant-titulaires.html',
                    controller: 'EnseignantTitulaireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enseignantTitulaire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('enseignant-titulaire-detail', {
            parent: 'entity',
            url: '/enseignant-titulaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.enseignantTitulaire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enseignant-titulaire/enseignant-titulaire-detail.html',
                    controller: 'EnseignantTitulaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enseignantTitulaire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EnseignantTitulaire', function($stateParams, EnseignantTitulaire) {
                    return EnseignantTitulaire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'enseignant-titulaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('enseignant-titulaire-detail.edit', {
            parent: 'enseignant-titulaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enseignant-titulaire/enseignant-titulaire-dialog.html',
                    controller: 'EnseignantTitulaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnseignantTitulaire', function(EnseignantTitulaire) {
                            return EnseignantTitulaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enseignant-titulaire.new', {
            parent: 'enseignant-titulaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enseignant-titulaire/enseignant-titulaire-dialog.html',
                    controller: 'EnseignantTitulaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('enseignant-titulaire', null, { reload: true });
                }, function() {
                    $state.go('enseignant-titulaire');
                });
            }]
        })
        .state('enseignant-titulaire.edit', {
            parent: 'enseignant-titulaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enseignant-titulaire/enseignant-titulaire-dialog.html',
                    controller: 'EnseignantTitulaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnseignantTitulaire', function(EnseignantTitulaire) {
                            return EnseignantTitulaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enseignant-titulaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enseignant-titulaire.delete', {
            parent: 'enseignant-titulaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enseignant-titulaire/enseignant-titulaire-delete-dialog.html',
                    controller: 'EnseignantTitulaireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EnseignantTitulaire', function(EnseignantTitulaire) {
                            return EnseignantTitulaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enseignant-titulaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
