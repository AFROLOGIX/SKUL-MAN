(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tranche', {
            parent: 'entity',
            url: '/tranche',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.tranche.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranche/tranches.html',
                    controller: 'TrancheController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tranche');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tranche-detail', {
            parent: 'entity',
            url: '/tranche/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.tranche.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranche/tranche-detail.html',
                    controller: 'TrancheDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tranche');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tranche', function($stateParams, Tranche) {
                    return Tranche.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tranche',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tranche-detail.edit', {
            parent: 'tranche-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche/tranche-dialog.html',
                    controller: 'TrancheDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tranche', function(Tranche) {
                            return Tranche.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranche.new', {
            parent: 'tranche',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche/tranche-dialog.html',
                    controller: 'TrancheDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                numero: null,
                                dateDeb: null,
                                dateFin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tranche', null, { reload: true });
                }, function() {
                    $state.go('tranche');
                });
            }]
        })
        .state('tranche.edit', {
            parent: 'tranche',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche/tranche-dialog.html',
                    controller: 'TrancheDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tranche', function(Tranche) {
                            return Tranche.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranche', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranche.delete', {
            parent: 'tranche',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche/tranche-delete-dialog.html',
                    controller: 'TrancheDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tranche', function(Tranche) {
                            return Tranche.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranche', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
