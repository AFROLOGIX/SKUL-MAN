(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('salle', {
            parent: 'entity',
            url: '/salle',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.salle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/salle/salles.html',
                    controller: 'SalleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('salle-detail', {
            parent: 'entity',
            url: '/salle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.salle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/salle/salle-detail.html',
                    controller: 'SalleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Salle', function($stateParams, Salle) {
                    return Salle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'salle',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('salle-detail.edit', {
            parent: 'salle-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salle/salle-dialog.html',
                    controller: 'SalleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Salle', function(Salle) {
                            return Salle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('salle.new', {
            parent: 'salle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salle/salle-dialog.html',
                    controller: 'SalleDialogController',
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
                    $state.go('salle', null, { reload: true });
                }, function() {
                    $state.go('salle');
                });
            }]
        })
        .state('salle.edit', {
            parent: 'salle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salle/salle-dialog.html',
                    controller: 'SalleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Salle', function(Salle) {
                            return Salle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('salle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('salle.delete', {
            parent: 'salle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salle/salle-delete-dialog.html',
                    controller: 'SalleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Salle', function(Salle) {
                            return Salle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('salle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
