(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('motif-exclusion', {
            parent: 'entity',
            url: '/motif-exclusion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.motifExclusion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/motif-exclusion/motif-exclusions.html',
                    controller: 'MotifExclusionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('motifExclusion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('motif-exclusion-detail', {
            parent: 'entity',
            url: '/motif-exclusion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.motifExclusion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/motif-exclusion/motif-exclusion-detail.html',
                    controller: 'MotifExclusionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('motifExclusion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MotifExclusion', function($stateParams, MotifExclusion) {
                    return MotifExclusion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'motif-exclusion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('motif-exclusion-detail.edit', {
            parent: 'motif-exclusion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motif-exclusion/motif-exclusion-dialog.html',
                    controller: 'MotifExclusionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MotifExclusion', function(MotifExclusion) {
                            return MotifExclusion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('motif-exclusion.new', {
            parent: 'motif-exclusion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motif-exclusion/motif-exclusion-dialog.html',
                    controller: 'MotifExclusionDialogController',
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
                    $state.go('motif-exclusion', null, { reload: true });
                }, function() {
                    $state.go('motif-exclusion');
                });
            }]
        })
        .state('motif-exclusion.edit', {
            parent: 'motif-exclusion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motif-exclusion/motif-exclusion-dialog.html',
                    controller: 'MotifExclusionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MotifExclusion', function(MotifExclusion) {
                            return MotifExclusion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('motif-exclusion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('motif-exclusion.delete', {
            parent: 'motif-exclusion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motif-exclusion/motif-exclusion-delete-dialog.html',
                    controller: 'MotifExclusionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MotifExclusion', function(MotifExclusion) {
                            return MotifExclusion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('motif-exclusion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
