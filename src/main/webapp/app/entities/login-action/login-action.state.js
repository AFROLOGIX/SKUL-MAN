(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('login-action', {
            parent: 'entity',
            url: '/login-action',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.loginAction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/login-action/login-actions.html',
                    controller: 'LoginActionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('loginAction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('login-action-detail', {
            parent: 'entity',
            url: '/login-action/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.loginAction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/login-action/login-action-detail.html',
                    controller: 'LoginActionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('loginAction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LoginAction', function($stateParams, LoginAction) {
                    return LoginAction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'login-action',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('login-action-detail.edit', {
            parent: 'login-action-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-action/login-action-dialog.html',
                    controller: 'LoginActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LoginAction', function(LoginAction) {
                            return LoginAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('login-action.new', {
            parent: 'login-action',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-action/login-action-dialog.html',
                    controller: 'LoginActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                action: null,
                                createBy: null,
                                role: null,
                                adresseIp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('login-action', null, { reload: true });
                }, function() {
                    $state.go('login-action');
                });
            }]
        })
        .state('login-action.edit', {
            parent: 'login-action',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-action/login-action-dialog.html',
                    controller: 'LoginActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LoginAction', function(LoginAction) {
                            return LoginAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('login-action', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('login-action.delete', {
            parent: 'login-action',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-action/login-action-delete-dialog.html',
                    controller: 'LoginActionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LoginAction', function(LoginAction) {
                            return LoginAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('login-action', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
