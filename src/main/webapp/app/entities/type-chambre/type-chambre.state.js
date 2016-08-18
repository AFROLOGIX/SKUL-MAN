(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-chambre', {
            parent: 'entity',
            url: '/type-chambre',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeChambre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-chambre/type-chambres.html',
                    controller: 'TypeChambreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeChambre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-chambre-detail', {
            parent: 'entity',
            url: '/type-chambre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeChambre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-chambre/type-chambre-detail.html',
                    controller: 'TypeChambreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeChambre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeChambre', function($stateParams, TypeChambre) {
                    return TypeChambre.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-chambre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-chambre-detail.edit', {
            parent: 'type-chambre-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-chambre/type-chambre-dialog.html',
                    controller: 'TypeChambreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeChambre', function(TypeChambre) {
                            return TypeChambre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-chambre.new', {
            parent: 'type-chambre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-chambre/type-chambre-dialog.html',
                    controller: 'TypeChambreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                montant: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-chambre', null, { reload: true });
                }, function() {
                    $state.go('type-chambre');
                });
            }]
        })
        .state('type-chambre.edit', {
            parent: 'type-chambre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-chambre/type-chambre-dialog.html',
                    controller: 'TypeChambreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeChambre', function(TypeChambre) {
                            return TypeChambre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-chambre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-chambre.delete', {
            parent: 'type-chambre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-chambre/type-chambre-delete-dialog.html',
                    controller: 'TypeChambreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeChambre', function(TypeChambre) {
                            return TypeChambre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-chambre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
