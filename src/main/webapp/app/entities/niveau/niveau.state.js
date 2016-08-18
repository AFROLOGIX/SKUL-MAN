(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('niveau', {
            parent: 'entity',
            url: '/niveau',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.niveau.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/niveau/niveaus.html',
                    controller: 'NiveauController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('niveau');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('niveau-detail', {
            parent: 'entity',
            url: '/niveau/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.niveau.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/niveau/niveau-detail.html',
                    controller: 'NiveauDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('niveau');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Niveau', function($stateParams, Niveau) {
                    return Niveau.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'niveau',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('niveau-detail.edit', {
            parent: 'niveau-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/niveau/niveau-dialog.html',
                    controller: 'NiveauDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Niveau', function(Niveau) {
                            return Niveau.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('niveau.new', {
            parent: 'niveau',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/niveau/niveau-dialog.html',
                    controller: 'NiveauDialogController',
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
                    $state.go('niveau', null, { reload: true });
                }, function() {
                    $state.go('niveau');
                });
            }]
        })
        .state('niveau.edit', {
            parent: 'niveau',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/niveau/niveau-dialog.html',
                    controller: 'NiveauDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Niveau', function(Niveau) {
                            return Niveau.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('niveau', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('niveau.delete', {
            parent: 'niveau',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/niveau/niveau-delete-dialog.html',
                    controller: 'NiveauDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Niveau', function(Niveau) {
                            return Niveau.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('niveau', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
