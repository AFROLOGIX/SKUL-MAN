(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('droit', {
            parent: 'entity',
            url: '/droit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.droit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/droit/droits.html',
                    controller: 'DroitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('droit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('droit-detail', {
            parent: 'entity',
            url: '/droit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.droit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/droit/droit-detail.html',
                    controller: 'DroitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('droit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Droit', function($stateParams, Droit) {
                    return Droit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'droit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('droit-detail.edit', {
            parent: 'droit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droit/droit-dialog.html',
                    controller: 'DroitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Droit', function(Droit) {
                            return Droit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('droit.new', {
            parent: 'droit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droit/droit-dialog.html',
                    controller: 'DroitDialogController',
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
                    $state.go('droit', null, { reload: true });
                }, function() {
                    $state.go('droit');
                });
            }]
        })
        .state('droit.edit', {
            parent: 'droit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droit/droit-dialog.html',
                    controller: 'DroitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Droit', function(Droit) {
                            return Droit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('droit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('droit.delete', {
            parent: 'droit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droit/droit-delete-dialog.html',
                    controller: 'DroitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Droit', function(Droit) {
                            return Droit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('droit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
