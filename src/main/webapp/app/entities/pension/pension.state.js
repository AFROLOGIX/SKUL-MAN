(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pension', {
            parent: 'entity',
            url: '/pension',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.pension.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pension/pensions.html',
                    controller: 'PensionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pension');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pension-detail', {
            parent: 'entity',
            url: '/pension/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.pension.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pension/pension-detail.html',
                    controller: 'PensionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pension');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pension', function($stateParams, Pension) {
                    return Pension.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pension',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pension-detail.edit', {
            parent: 'pension-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pension/pension-dialog.html',
                    controller: 'PensionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pension', function(Pension) {
                            return Pension.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pension.new', {
            parent: 'pension',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pension/pension-dialog.html',
                    controller: 'PensionDialogController',
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
                    $state.go('pension', null, { reload: true });
                }, function() {
                    $state.go('pension');
                });
            }]
        })
        .state('pension.edit', {
            parent: 'pension',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pension/pension-dialog.html',
                    controller: 'PensionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pension', function(Pension) {
                            return Pension.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pension', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pension.delete', {
            parent: 'pension',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pension/pension-delete-dialog.html',
                    controller: 'PensionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pension', function(Pension) {
                            return Pension.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pension', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
