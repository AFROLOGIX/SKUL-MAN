(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('evenement', {
            parent: 'entity',
            url: '/evenement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.evenement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evenement/evenements.html',
                    controller: 'EvenementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evenement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('evenement-detail', {
            parent: 'entity',
            url: '/evenement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.evenement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evenement/evenement-detail.html',
                    controller: 'EvenementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evenement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Evenement', function($stateParams, Evenement) {
                    return Evenement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'evenement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('evenement-detail.edit', {
            parent: 'evenement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evenement/evenement-dialog.html',
                    controller: 'EvenementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Evenement', function(Evenement) {
                            return Evenement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evenement.new', {
            parent: 'evenement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evenement/evenement-dialog.html',
                    controller: 'EvenementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                dateDeb: null,
                                dateFin: null,
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('evenement', null, { reload: true });
                }, function() {
                    $state.go('evenement');
                });
            }]
        })
        .state('evenement.edit', {
            parent: 'evenement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evenement/evenement-dialog.html',
                    controller: 'EvenementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Evenement', function(Evenement) {
                            return Evenement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('evenement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evenement.delete', {
            parent: 'evenement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evenement/evenement-delete-dialog.html',
                    controller: 'EvenementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Evenement', function(Evenement) {
                            return Evenement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('evenement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
