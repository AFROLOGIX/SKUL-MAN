(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bourse', {
            parent: 'entity',
            url: '/bourse',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.bourse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bourse/bourses.html',
                    controller: 'BourseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bourse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bourse-detail', {
            parent: 'entity',
            url: '/bourse/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.bourse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bourse/bourse-detail.html',
                    controller: 'BourseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bourse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bourse', function($stateParams, Bourse) {
                    return Bourse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bourse',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bourse-detail.edit', {
            parent: 'bourse-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourse/bourse-dialog.html',
                    controller: 'BourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bourse', function(Bourse) {
                            return Bourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bourse.new', {
            parent: 'bourse',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourse/bourse-dialog.html',
                    controller: 'BourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                motif: null,
                                montant: null,
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bourse', null, { reload: true });
                }, function() {
                    $state.go('bourse');
                });
            }]
        })
        .state('bourse.edit', {
            parent: 'bourse',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourse/bourse-dialog.html',
                    controller: 'BourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bourse', function(Bourse) {
                            return Bourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bourse', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bourse.delete', {
            parent: 'bourse',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourse/bourse-delete-dialog.html',
                    controller: 'BourseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bourse', function(Bourse) {
                            return Bourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bourse', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
