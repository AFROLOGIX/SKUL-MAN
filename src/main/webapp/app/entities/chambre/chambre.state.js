(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chambre', {
            parent: 'entity',
            url: '/chambre',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.chambre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chambre/chambres.html',
                    controller: 'ChambreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chambre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('chambre-detail', {
            parent: 'entity',
            url: '/chambre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.chambre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chambre/chambre-detail.html',
                    controller: 'ChambreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chambre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Chambre', function($stateParams, Chambre) {
                    return Chambre.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'chambre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('chambre-detail.edit', {
            parent: 'chambre-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre/chambre-dialog.html',
                    controller: 'ChambreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chambre', function(Chambre) {
                            return Chambre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chambre.new', {
            parent: 'chambre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre/chambre-dialog.html',
                    controller: 'ChambreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelle: null,
                                nombreMaxPersonne: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('chambre', null, { reload: true });
                }, function() {
                    $state.go('chambre');
                });
            }]
        })
        .state('chambre.edit', {
            parent: 'chambre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre/chambre-dialog.html',
                    controller: 'ChambreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chambre', function(Chambre) {
                            return Chambre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chambre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chambre.delete', {
            parent: 'chambre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre/chambre-delete-dialog.html',
                    controller: 'ChambreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Chambre', function(Chambre) {
                            return Chambre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chambre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
