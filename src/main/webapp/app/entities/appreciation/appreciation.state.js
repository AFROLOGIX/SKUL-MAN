(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('appreciation', {
            parent: 'entity',
            url: '/appreciation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.appreciation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appreciation/appreciations.html',
                    controller: 'AppreciationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('appreciation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('appreciation-detail', {
            parent: 'entity',
            url: '/appreciation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.appreciation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appreciation/appreciation-detail.html',
                    controller: 'AppreciationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('appreciation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Appreciation', function($stateParams, Appreciation) {
                    return Appreciation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'appreciation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('appreciation-detail.edit', {
            parent: 'appreciation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appreciation/appreciation-dialog.html',
                    controller: 'AppreciationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appreciation', function(Appreciation) {
                            return Appreciation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appreciation.new', {
            parent: 'appreciation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appreciation/appreciation-dialog.html',
                    controller: 'AppreciationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                minNote: null,
                                maxNote: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('appreciation', null, { reload: true });
                }, function() {
                    $state.go('appreciation');
                });
            }]
        })
        .state('appreciation.edit', {
            parent: 'appreciation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appreciation/appreciation-dialog.html',
                    controller: 'AppreciationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appreciation', function(Appreciation) {
                            return Appreciation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appreciation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appreciation.delete', {
            parent: 'appreciation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appreciation/appreciation-delete-dialog.html',
                    controller: 'AppreciationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Appreciation', function(Appreciation) {
                            return Appreciation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appreciation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
