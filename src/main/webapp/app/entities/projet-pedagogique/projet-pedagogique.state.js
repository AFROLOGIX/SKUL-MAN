(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('projet-pedagogique', {
            parent: 'entity',
            url: '/projet-pedagogique',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.projetPedagogique.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/projet-pedagogique/projet-pedagogiques.html',
                    controller: 'ProjetPedagogiqueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projetPedagogique');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('projet-pedagogique-detail', {
            parent: 'entity',
            url: '/projet-pedagogique/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.projetPedagogique.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/projet-pedagogique/projet-pedagogique-detail.html',
                    controller: 'ProjetPedagogiqueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projetPedagogique');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjetPedagogique', function($stateParams, ProjetPedagogique) {
                    return ProjetPedagogique.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'projet-pedagogique',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('projet-pedagogique-detail.edit', {
            parent: 'projet-pedagogique-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet-pedagogique/projet-pedagogique-dialog.html',
                    controller: 'ProjetPedagogiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjetPedagogique', function(ProjetPedagogique) {
                            return ProjetPedagogique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('projet-pedagogique.new', {
            parent: 'projet-pedagogique',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet-pedagogique/projet-pedagogique-dialog.html',
                    controller: 'ProjetPedagogiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                elementProg: null,
                                volumeHoraire: null,
                                dateDeb: null,
                                dateFin: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('projet-pedagogique', null, { reload: true });
                }, function() {
                    $state.go('projet-pedagogique');
                });
            }]
        })
        .state('projet-pedagogique.edit', {
            parent: 'projet-pedagogique',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet-pedagogique/projet-pedagogique-dialog.html',
                    controller: 'ProjetPedagogiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjetPedagogique', function(ProjetPedagogique) {
                            return ProjetPedagogique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projet-pedagogique', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('projet-pedagogique.delete', {
            parent: 'projet-pedagogique',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet-pedagogique/projet-pedagogique-delete-dialog.html',
                    controller: 'ProjetPedagogiqueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjetPedagogique', function(ProjetPedagogique) {
                            return ProjetPedagogique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projet-pedagogique', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
