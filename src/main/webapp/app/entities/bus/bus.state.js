(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bus', {
            parent: 'entity',
            url: '/bus',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.bus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bus/buses.html',
                    controller: 'BusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bus-detail', {
            parent: 'entity',
            url: '/bus/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.bus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bus/bus-detail.html',
                    controller: 'BusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bus', function($stateParams, Bus) {
                    return Bus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bus',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bus-detail.edit', {
            parent: 'bus-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bus/bus-dialog.html',
                    controller: 'BusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bus', function(Bus) {
                            return Bus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bus.new', {
            parent: 'bus',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bus/bus-dialog.html',
                    controller: 'BusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                immatriculation: null,
                                libelleFr: null,
                                libelleEn: null,
                                capacite: null,
                                marque: null,
                                dateAcquisition: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bus', null, { reload: true });
                }, function() {
                    $state.go('bus');
                });
            }]
        })
        .state('bus.edit', {
            parent: 'bus',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bus/bus-dialog.html',
                    controller: 'BusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bus', function(Bus) {
                            return Bus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bus', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bus.delete', {
            parent: 'bus',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bus/bus-delete-dialog.html',
                    controller: 'BusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bus', function(Bus) {
                            return Bus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bus', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
