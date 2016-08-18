(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('login-connexion', {
            parent: 'entity',
            url: '/login-connexion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.loginConnexion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/login-connexion/login-connexions.html',
                    controller: 'LoginConnexionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('loginConnexion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('login-connexion-detail', {
            parent: 'entity',
            url: '/login-connexion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.loginConnexion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/login-connexion/login-connexion-detail.html',
                    controller: 'LoginConnexionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('loginConnexion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LoginConnexion', function($stateParams, LoginConnexion) {
                    return LoginConnexion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'login-connexion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('login-connexion-detail.edit', {
            parent: 'login-connexion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-connexion/login-connexion-dialog.html',
                    controller: 'LoginConnexionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LoginConnexion', function(LoginConnexion) {
                            return LoginConnexion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('login-connexion.new', {
            parent: 'login-connexion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-connexion/login-connexion-dialog.html',
                    controller: 'LoginConnexionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                role: null,
                                loginTime: null,
                                status: null,
                                addressIp: null,
                                nbEchecs: null,
                                dateEchec: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('login-connexion', null, { reload: true });
                }, function() {
                    $state.go('login-connexion');
                });
            }]
        })
        .state('login-connexion.edit', {
            parent: 'login-connexion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-connexion/login-connexion-dialog.html',
                    controller: 'LoginConnexionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LoginConnexion', function(LoginConnexion) {
                            return LoginConnexion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('login-connexion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('login-connexion.delete', {
            parent: 'login-connexion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/login-connexion/login-connexion-delete-dialog.html',
                    controller: 'LoginConnexionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LoginConnexion', function(LoginConnexion) {
                            return LoginConnexion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('login-connexion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
