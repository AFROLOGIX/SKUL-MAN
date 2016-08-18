(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('serie', {
            parent: 'entity',
            url: '/serie',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.serie.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serie/series.html',
                    controller: 'SerieController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('serie-detail', {
            parent: 'entity',
            url: '/serie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.serie.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serie/serie-detail.html',
                    controller: 'SerieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serie');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Serie', function($stateParams, Serie) {
                    return Serie.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'serie',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('serie-detail.edit', {
            parent: 'serie-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie/serie-dialog.html',
                    controller: 'SerieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Serie', function(Serie) {
                            return Serie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serie.new', {
            parent: 'serie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie/serie-dialog.html',
                    controller: 'SerieDialogController',
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
                    $state.go('serie', null, { reload: true });
                }, function() {
                    $state.go('serie');
                });
            }]
        })
        .state('serie.edit', {
            parent: 'serie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie/serie-dialog.html',
                    controller: 'SerieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Serie', function(Serie) {
                            return Serie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serie', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serie.delete', {
            parent: 'serie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie/serie-delete-dialog.html',
                    controller: 'SerieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Serie', function(Serie) {
                            return Serie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serie', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
