(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('paiement-personnel', {
            parent: 'entity',
            url: '/paiement-personnel',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.paiementPersonnel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paiement-personnel/paiement-personnels.html',
                    controller: 'PaiementPersonnelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paiementPersonnel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('paiement-personnel-detail', {
            parent: 'entity',
            url: '/paiement-personnel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.paiementPersonnel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paiement-personnel/paiement-personnel-detail.html',
                    controller: 'PaiementPersonnelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paiementPersonnel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PaiementPersonnel', function($stateParams, PaiementPersonnel) {
                    return PaiementPersonnel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'paiement-personnel',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('paiement-personnel-detail.edit', {
            parent: 'paiement-personnel-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement-personnel/paiement-personnel-dialog.html',
                    controller: 'PaiementPersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaiementPersonnel', function(PaiementPersonnel) {
                            return PaiementPersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paiement-personnel.new', {
            parent: 'paiement-personnel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement-personnel/paiement-personnel-dialog.html',
                    controller: 'PaiementPersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                montant: null,
                                dette: null,
                                anneeScolaire: null,
                                periode: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('paiement-personnel', null, { reload: true });
                }, function() {
                    $state.go('paiement-personnel');
                });
            }]
        })
        .state('paiement-personnel.edit', {
            parent: 'paiement-personnel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement-personnel/paiement-personnel-dialog.html',
                    controller: 'PaiementPersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaiementPersonnel', function(PaiementPersonnel) {
                            return PaiementPersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paiement-personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paiement-personnel.delete', {
            parent: 'paiement-personnel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement-personnel/paiement-personnel-delete-dialog.html',
                    controller: 'PaiementPersonnelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaiementPersonnel', function(PaiementPersonnel) {
                            return PaiementPersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paiement-personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
