(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pause', {
            parent: 'entity',
            url: '/pause',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.pause.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pause/pauses.html',
                    controller: 'PauseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pause');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pause-detail', {
            parent: 'entity',
            url: '/pause/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.pause.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pause/pause-detail.html',
                    controller: 'PauseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pause');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pause', function($stateParams, Pause) {
                    return Pause.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pause',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pause-detail.edit', {
            parent: 'pause-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pause/pause-dialog.html',
                    controller: 'PauseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pause', function(Pause) {
                            return Pause.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pause.new', {
            parent: 'pause',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pause/pause-dialog.html',
                    controller: 'PauseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                heureDeb: null,
                                heureFin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pause', null, { reload: true });
                }, function() {
                    $state.go('pause');
                });
            }]
        })
        .state('pause.edit', {
            parent: 'pause',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pause/pause-dialog.html',
                    controller: 'PauseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pause', function(Pause) {
                            return Pause.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pause', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pause.delete', {
            parent: 'pause',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pause/pause-delete-dialog.html',
                    controller: 'PauseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pause', function(Pause) {
                            return Pause.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pause', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
