(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cycle', {
            parent: 'entity',
            url: '/cycle',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.cycle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle/cycles.html',
                    controller: 'CycleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cycle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cycle-detail', {
            parent: 'entity',
            url: '/cycle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.cycle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle/cycle-detail.html',
                    controller: 'CycleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cycle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cycle', function($stateParams, Cycle) {
                    return Cycle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cycle',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cycle-detail.edit', {
            parent: 'cycle-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle/cycle-dialog.html',
                    controller: 'CycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cycle', function(Cycle) {
                            return Cycle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cycle.new', {
            parent: 'cycle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle/cycle-dialog.html',
                    controller: 'CycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cycle', null, { reload: true });
                }, function() {
                    $state.go('cycle');
                });
            }]
        })
        .state('cycle.edit', {
            parent: 'cycle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle/cycle-dialog.html',
                    controller: 'CycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cycle', function(Cycle) {
                            return Cycle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cycle.delete', {
            parent: 'cycle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle/cycle-delete-dialog.html',
                    controller: 'CycleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cycle', function(Cycle) {
                            return Cycle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
