(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tarif-bus', {
            parent: 'entity',
            url: '/tarif-bus',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.tarifBus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarif-bus/tarif-buses.html',
                    controller: 'TarifBusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tarifBus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tarif-bus-detail', {
            parent: 'entity',
            url: '/tarif-bus/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.tarifBus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarif-bus/tarif-bus-detail.html',
                    controller: 'TarifBusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tarifBus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TarifBus', function($stateParams, TarifBus) {
                    return TarifBus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tarif-bus',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tarif-bus-detail.edit', {
            parent: 'tarif-bus-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarif-bus/tarif-bus-dialog.html',
                    controller: 'TarifBusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TarifBus', function(TarifBus) {
                            return TarifBus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tarif-bus.new', {
            parent: 'tarif-bus',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarif-bus/tarif-bus-dialog.html',
                    controller: 'TarifBusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                periode: null,
                                tarif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tarif-bus', null, { reload: true });
                }, function() {
                    $state.go('tarif-bus');
                });
            }]
        })
        .state('tarif-bus.edit', {
            parent: 'tarif-bus',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarif-bus/tarif-bus-dialog.html',
                    controller: 'TarifBusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TarifBus', function(TarifBus) {
                            return TarifBus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarif-bus', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tarif-bus.delete', {
            parent: 'tarif-bus',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarif-bus/tarif-bus-delete-dialog.html',
                    controller: 'TarifBusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TarifBus', function(TarifBus) {
                            return TarifBus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarif-bus', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
