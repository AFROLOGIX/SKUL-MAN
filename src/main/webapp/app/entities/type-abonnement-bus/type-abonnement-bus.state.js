(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-abonnement-bus', {
            parent: 'entity',
            url: '/type-abonnement-bus',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeAbonnementBus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-abonnement-bus/type-abonnement-buses.html',
                    controller: 'TypeAbonnementBusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeAbonnementBus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-abonnement-bus-detail', {
            parent: 'entity',
            url: '/type-abonnement-bus/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeAbonnementBus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-abonnement-bus/type-abonnement-bus-detail.html',
                    controller: 'TypeAbonnementBusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeAbonnementBus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeAbonnementBus', function($stateParams, TypeAbonnementBus) {
                    return TypeAbonnementBus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-abonnement-bus',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-abonnement-bus-detail.edit', {
            parent: 'type-abonnement-bus-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-abonnement-bus/type-abonnement-bus-dialog.html',
                    controller: 'TypeAbonnementBusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeAbonnementBus', function(TypeAbonnementBus) {
                            return TypeAbonnementBus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-abonnement-bus.new', {
            parent: 'type-abonnement-bus',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-abonnement-bus/type-abonnement-bus-dialog.html',
                    controller: 'TypeAbonnementBusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                anneeScolaire: null,
                                montantAbonnement: null,
                                dureeAbonnement: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-abonnement-bus', null, { reload: true });
                }, function() {
                    $state.go('type-abonnement-bus');
                });
            }]
        })
        .state('type-abonnement-bus.edit', {
            parent: 'type-abonnement-bus',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-abonnement-bus/type-abonnement-bus-dialog.html',
                    controller: 'TypeAbonnementBusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeAbonnementBus', function(TypeAbonnementBus) {
                            return TypeAbonnementBus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-abonnement-bus', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-abonnement-bus.delete', {
            parent: 'type-abonnement-bus',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-abonnement-bus/type-abonnement-bus-delete-dialog.html',
                    controller: 'TypeAbonnementBusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeAbonnementBus', function(TypeAbonnementBus) {
                            return TypeAbonnementBus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-abonnement-bus', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
