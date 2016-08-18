(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chambre-eleve', {
            parent: 'entity',
            url: '/chambre-eleve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.chambreEleve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chambre-eleve/chambre-eleves.html',
                    controller: 'ChambreEleveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chambreEleve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('chambre-eleve-detail', {
            parent: 'entity',
            url: '/chambre-eleve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.chambreEleve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chambre-eleve/chambre-eleve-detail.html',
                    controller: 'ChambreEleveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chambreEleve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChambreEleve', function($stateParams, ChambreEleve) {
                    return ChambreEleve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'chambre-eleve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('chambre-eleve-detail.edit', {
            parent: 'chambre-eleve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre-eleve/chambre-eleve-dialog.html',
                    controller: 'ChambreEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChambreEleve', function(ChambreEleve) {
                            return ChambreEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chambre-eleve.new', {
            parent: 'chambre-eleve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre-eleve/chambre-eleve-dialog.html',
                    controller: 'ChambreEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('chambre-eleve', null, { reload: true });
                }, function() {
                    $state.go('chambre-eleve');
                });
            }]
        })
        .state('chambre-eleve.edit', {
            parent: 'chambre-eleve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre-eleve/chambre-eleve-dialog.html',
                    controller: 'ChambreEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChambreEleve', function(ChambreEleve) {
                            return ChambreEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chambre-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chambre-eleve.delete', {
            parent: 'chambre-eleve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chambre-eleve/chambre-eleve-delete-dialog.html',
                    controller: 'ChambreEleveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChambreEleve', function(ChambreEleve) {
                            return ChambreEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chambre-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
