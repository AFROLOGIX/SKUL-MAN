(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('matiere-classe', {
            parent: 'entity',
            url: '/matiere-classe',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.matiereClasse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/matiere-classe/matiere-classes.html',
                    controller: 'MatiereClasseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('matiereClasse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('matiere-classe-detail', {
            parent: 'entity',
            url: '/matiere-classe/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.matiereClasse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/matiere-classe/matiere-classe-detail.html',
                    controller: 'MatiereClasseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('matiereClasse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MatiereClasse', function($stateParams, MatiereClasse) {
                    return MatiereClasse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'matiere-classe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('matiere-classe-detail.edit', {
            parent: 'matiere-classe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere-classe/matiere-classe-dialog.html',
                    controller: 'MatiereClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MatiereClasse', function(MatiereClasse) {
                            return MatiereClasse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('matiere-classe.new', {
            parent: 'matiere-classe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere-classe/matiere-classe-dialog.html',
                    controller: 'MatiereClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                coef: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('matiere-classe', null, { reload: true });
                }, function() {
                    $state.go('matiere-classe');
                });
            }]
        })
        .state('matiere-classe.edit', {
            parent: 'matiere-classe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere-classe/matiere-classe-dialog.html',
                    controller: 'MatiereClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MatiereClasse', function(MatiereClasse) {
                            return MatiereClasse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('matiere-classe', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('matiere-classe.delete', {
            parent: 'matiere-classe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere-classe/matiere-classe-delete-dialog.html',
                    controller: 'MatiereClasseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MatiereClasse', function(MatiereClasse) {
                            return MatiereClasse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('matiere-classe', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
