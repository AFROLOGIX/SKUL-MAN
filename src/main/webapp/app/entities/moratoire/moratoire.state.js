(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('moratoire', {
            parent: 'entity',
            url: '/moratoire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.moratoire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/moratoire/moratoires.html',
                    controller: 'MoratoireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('moratoire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('moratoire-detail', {
            parent: 'entity',
            url: '/moratoire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.moratoire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/moratoire/moratoire-detail.html',
                    controller: 'MoratoireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('moratoire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Moratoire', function($stateParams, Moratoire) {
                    return Moratoire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'moratoire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('moratoire-detail.edit', {
            parent: 'moratoire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moratoire/moratoire-dialog.html',
                    controller: 'MoratoireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Moratoire', function(Moratoire) {
                            return Moratoire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('moratoire.new', {
            parent: 'moratoire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moratoire/moratoire-dialog.html',
                    controller: 'MoratoireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                motif: null,
                                anneeScolaire: null,
                                nouvelleDate: null,
                                type: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('moratoire', null, { reload: true });
                }, function() {
                    $state.go('moratoire');
                });
            }]
        })
        .state('moratoire.edit', {
            parent: 'moratoire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moratoire/moratoire-dialog.html',
                    controller: 'MoratoireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Moratoire', function(Moratoire) {
                            return Moratoire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('moratoire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('moratoire.delete', {
            parent: 'moratoire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moratoire/moratoire-delete-dialog.html',
                    controller: 'MoratoireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Moratoire', function(Moratoire) {
                            return Moratoire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('moratoire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
