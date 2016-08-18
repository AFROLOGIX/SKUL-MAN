(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('regime-pension', {
            parent: 'entity',
            url: '/regime-pension',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.regimePension.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/regime-pension/regime-pensions.html',
                    controller: 'RegimePensionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('regimePension');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('regime-pension-detail', {
            parent: 'entity',
            url: '/regime-pension/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.regimePension.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/regime-pension/regime-pension-detail.html',
                    controller: 'RegimePensionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('regimePension');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RegimePension', function($stateParams, RegimePension) {
                    return RegimePension.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'regime-pension',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('regime-pension-detail.edit', {
            parent: 'regime-pension-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/regime-pension/regime-pension-dialog.html',
                    controller: 'RegimePensionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RegimePension', function(RegimePension) {
                            return RegimePension.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('regime-pension.new', {
            parent: 'regime-pension',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/regime-pension/regime-pension-dialog.html',
                    controller: 'RegimePensionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                anneeScolaire: null,
                                nbTotalTranches: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('regime-pension', null, { reload: true });
                }, function() {
                    $state.go('regime-pension');
                });
            }]
        })
        .state('regime-pension.edit', {
            parent: 'regime-pension',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/regime-pension/regime-pension-dialog.html',
                    controller: 'RegimePensionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RegimePension', function(RegimePension) {
                            return RegimePension.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('regime-pension', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('regime-pension.delete', {
            parent: 'regime-pension',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/regime-pension/regime-pension-delete-dialog.html',
                    controller: 'RegimePensionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RegimePension', function(RegimePension) {
                            return RegimePension.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('regime-pension', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
