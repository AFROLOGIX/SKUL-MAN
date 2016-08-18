(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fonctionnalite', {
            parent: 'entity',
            url: '/fonctionnalite',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.fonctionnalite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fonctionnalite/fonctionnalites.html',
                    controller: 'FonctionnaliteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fonctionnalite');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fonctionnalite-detail', {
            parent: 'entity',
            url: '/fonctionnalite/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.fonctionnalite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fonctionnalite/fonctionnalite-detail.html',
                    controller: 'FonctionnaliteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fonctionnalite');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fonctionnalite', function($stateParams, Fonctionnalite) {
                    return Fonctionnalite.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fonctionnalite',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fonctionnalite-detail.edit', {
            parent: 'fonctionnalite-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fonctionnalite/fonctionnalite-dialog.html',
                    controller: 'FonctionnaliteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fonctionnalite', function(Fonctionnalite) {
                            return Fonctionnalite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fonctionnalite.new', {
            parent: 'fonctionnalite',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fonctionnalite/fonctionnalite-dialog.html',
                    controller: 'FonctionnaliteDialogController',
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
                    $state.go('fonctionnalite', null, { reload: true });
                }, function() {
                    $state.go('fonctionnalite');
                });
            }]
        })
        .state('fonctionnalite.edit', {
            parent: 'fonctionnalite',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fonctionnalite/fonctionnalite-dialog.html',
                    controller: 'FonctionnaliteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fonctionnalite', function(Fonctionnalite) {
                            return Fonctionnalite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fonctionnalite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fonctionnalite.delete', {
            parent: 'fonctionnalite',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fonctionnalite/fonctionnalite-delete-dialog.html',
                    controller: 'FonctionnaliteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fonctionnalite', function(Fonctionnalite) {
                            return Fonctionnalite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fonctionnalite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
