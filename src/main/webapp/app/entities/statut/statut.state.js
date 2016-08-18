(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('statut', {
            parent: 'entity',
            url: '/statut',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.statut.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statut/statuts.html',
                    controller: 'StatutController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statut');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('statut-detail', {
            parent: 'entity',
            url: '/statut/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.statut.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statut/statut-detail.html',
                    controller: 'StatutDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statut');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Statut', function($stateParams, Statut) {
                    return Statut.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'statut',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('statut-detail.edit', {
            parent: 'statut-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut/statut-dialog.html',
                    controller: 'StatutDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Statut', function(Statut) {
                            return Statut.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statut.new', {
            parent: 'statut',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut/statut-dialog.html',
                    controller: 'StatutDialogController',
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
                    $state.go('statut', null, { reload: true });
                }, function() {
                    $state.go('statut');
                });
            }]
        })
        .state('statut.edit', {
            parent: 'statut',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut/statut-dialog.html',
                    controller: 'StatutDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Statut', function(Statut) {
                            return Statut.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statut', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statut.delete', {
            parent: 'statut',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statut/statut-delete-dialog.html',
                    controller: 'StatutDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Statut', function(Statut) {
                            return Statut.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statut', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
