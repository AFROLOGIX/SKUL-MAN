(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('statut-eleve', {
            parent: 'entity',
            url: '/statut-eleve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.statutEleve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statut-eleve/statut-eleves.html',
                    controller: 'StatutEleveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statutEleve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('statut-eleve-detail', {
            parent: 'entity',
            url: '/statut-eleve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.statutEleve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statut-eleve/statut-eleve-detail.html',
                    controller: 'StatutEleveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statutEleve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StatutEleve', function($stateParams, StatutEleve) {
                    return StatutEleve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'statut-eleve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('statut-eleve-detail.edit', {
            parent: 'statut-eleve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-eleve/statut-eleve-dialog.html',
                    controller: 'StatutEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StatutEleve', function(StatutEleve) {
                            return StatutEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statut-eleve.new', {
            parent: 'statut-eleve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-eleve/statut-eleve-dialog.html',
                    controller: 'StatutEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                redouble: null,
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('statut-eleve', null, { reload: true });
                }, function() {
                    $state.go('statut-eleve');
                });
            }]
        })
        .state('statut-eleve.edit', {
            parent: 'statut-eleve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-eleve/statut-eleve-dialog.html',
                    controller: 'StatutEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StatutEleve', function(StatutEleve) {
                            return StatutEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statut-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statut-eleve.delete', {
            parent: 'statut-eleve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut-eleve/statut-eleve-delete-dialog.html',
                    controller: 'StatutEleveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StatutEleve', function(StatutEleve) {
                            return StatutEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statut-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
