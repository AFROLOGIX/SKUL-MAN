(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('deliberation', {
            parent: 'entity',
            url: '/deliberation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.deliberation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deliberation/deliberations.html',
                    controller: 'DeliberationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deliberation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('deliberation-detail', {
            parent: 'entity',
            url: '/deliberation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.deliberation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deliberation/deliberation-detail.html',
                    controller: 'DeliberationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deliberation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Deliberation', function($stateParams, Deliberation) {
                    return Deliberation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'deliberation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('deliberation-detail.edit', {
            parent: 'deliberation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliberation/deliberation-dialog.html',
                    controller: 'DeliberationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deliberation', function(Deliberation) {
                            return Deliberation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deliberation.new', {
            parent: 'deliberation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliberation/deliberation-dialog.html',
                    controller: 'DeliberationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                motif: null,
                                decision: null,
                                isActive: null,
                                anneeScolaire: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('deliberation', null, { reload: true });
                }, function() {
                    $state.go('deliberation');
                });
            }]
        })
        .state('deliberation.edit', {
            parent: 'deliberation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliberation/deliberation-dialog.html',
                    controller: 'DeliberationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deliberation', function(Deliberation) {
                            return Deliberation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deliberation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deliberation.delete', {
            parent: 'deliberation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliberation/deliberation-delete-dialog.html',
                    controller: 'DeliberationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Deliberation', function(Deliberation) {
                            return Deliberation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deliberation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
