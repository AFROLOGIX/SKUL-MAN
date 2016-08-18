(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transaction-diverses', {
            parent: 'entity',
            url: '/transaction-diverses',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.transactionDiverses.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction-diverses/transaction-diverses.html',
                    controller: 'TransactionDiversesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transactionDiverses');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('transaction-diverses-detail', {
            parent: 'entity',
            url: '/transaction-diverses/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.transactionDiverses.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction-diverses/transaction-diverses-detail.html',
                    controller: 'TransactionDiversesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transactionDiverses');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TransactionDiverses', function($stateParams, TransactionDiverses) {
                    return TransactionDiverses.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transaction-diverses',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transaction-diverses-detail.edit', {
            parent: 'transaction-diverses-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-diverses/transaction-diverses-dialog.html',
                    controller: 'TransactionDiversesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransactionDiverses', function(TransactionDiverses) {
                            return TransactionDiverses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction-diverses.new', {
            parent: 'transaction-diverses',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-diverses/transaction-diverses-dialog.html',
                    controller: 'TransactionDiversesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                typeUsager: null,
                                isCredit: null,
                                usagerId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transaction-diverses', null, { reload: true });
                }, function() {
                    $state.go('transaction-diverses');
                });
            }]
        })
        .state('transaction-diverses.edit', {
            parent: 'transaction-diverses',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-diverses/transaction-diverses-dialog.html',
                    controller: 'TransactionDiversesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransactionDiverses', function(TransactionDiverses) {
                            return TransactionDiverses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction-diverses', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction-diverses.delete', {
            parent: 'transaction-diverses',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-diverses/transaction-diverses-delete-dialog.html',
                    controller: 'TransactionDiversesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransactionDiverses', function(TransactionDiverses) {
                            return TransactionDiverses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction-diverses', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
