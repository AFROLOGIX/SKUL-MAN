(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-operation', {
            parent: 'entity',
            url: '/type-operation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeOperation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-operation/type-operations.html',
                    controller: 'TypeOperationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeOperation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-operation-detail', {
            parent: 'entity',
            url: '/type-operation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeOperation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-operation/type-operation-detail.html',
                    controller: 'TypeOperationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeOperation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeOperation', function($stateParams, TypeOperation) {
                    return TypeOperation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-operation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-operation-detail.edit', {
            parent: 'type-operation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-operation/type-operation-dialog.html',
                    controller: 'TypeOperationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeOperation', function(TypeOperation) {
                            return TypeOperation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-operation.new', {
            parent: 'type-operation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-operation/type-operation-dialog.html',
                    controller: 'TypeOperationDialogController',
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
                    $state.go('type-operation', null, { reload: true });
                }, function() {
                    $state.go('type-operation');
                });
            }]
        })
        .state('type-operation.edit', {
            parent: 'type-operation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-operation/type-operation-dialog.html',
                    controller: 'TypeOperationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeOperation', function(TypeOperation) {
                            return TypeOperation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-operation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-operation.delete', {
            parent: 'type-operation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-operation/type-operation-delete-dialog.html',
                    controller: 'TypeOperationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeOperation', function(TypeOperation) {
                            return TypeOperation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-operation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
