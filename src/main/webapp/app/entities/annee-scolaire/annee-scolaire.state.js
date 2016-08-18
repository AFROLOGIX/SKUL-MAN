(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('annee-scolaire', {
            parent: 'entity',
            url: '/annee-scolaire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.anneeScolaire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/annee-scolaire/annee-scolaires.html',
                    controller: 'AnneeScolaireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('anneeScolaire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('annee-scolaire-detail', {
            parent: 'entity',
            url: '/annee-scolaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.anneeScolaire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/annee-scolaire/annee-scolaire-detail.html',
                    controller: 'AnneeScolaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('anneeScolaire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AnneeScolaire', function($stateParams, AnneeScolaire) {
                    return AnneeScolaire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'annee-scolaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('annee-scolaire-detail.edit', {
            parent: 'annee-scolaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-scolaire/annee-scolaire-dialog.html',
                    controller: 'AnneeScolaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnneeScolaire', function(AnneeScolaire) {
                            return AnneeScolaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee-scolaire.new', {
            parent: 'annee-scolaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-scolaire/annee-scolaire-dialog.html',
                    controller: 'AnneeScolaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                annee: null,
                                codeAnnee: null,
                                dateDeb: null,
                                dateFin: null,
                                isActive: null,
                                objectifs: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('annee-scolaire', null, { reload: true });
                }, function() {
                    $state.go('annee-scolaire');
                });
            }]
        })
        .state('annee-scolaire.edit', {
            parent: 'annee-scolaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-scolaire/annee-scolaire-dialog.html',
                    controller: 'AnneeScolaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnneeScolaire', function(AnneeScolaire) {
                            return AnneeScolaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('annee-scolaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee-scolaire.delete', {
            parent: 'annee-scolaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-scolaire/annee-scolaire-delete-dialog.html',
                    controller: 'AnneeScolaireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AnneeScolaire', function(AnneeScolaire) {
                            return AnneeScolaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('annee-scolaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
