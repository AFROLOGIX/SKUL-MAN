(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tranche-horaire', {
            parent: 'entity',
            url: '/tranche-horaire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.trancheHoraire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranche-horaire/tranche-horaires.html',
                    controller: 'TrancheHoraireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trancheHoraire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tranche-horaire-detail', {
            parent: 'entity',
            url: '/tranche-horaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.trancheHoraire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranche-horaire/tranche-horaire-detail.html',
                    controller: 'TrancheHoraireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trancheHoraire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrancheHoraire', function($stateParams, TrancheHoraire) {
                    return TrancheHoraire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tranche-horaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tranche-horaire-detail.edit', {
            parent: 'tranche-horaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche-horaire/tranche-horaire-dialog.html',
                    controller: 'TrancheHoraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TrancheHoraire', function(TrancheHoraire) {
                            return TrancheHoraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranche-horaire.new', {
            parent: 'tranche-horaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche-horaire/tranche-horaire-dialog.html',
                    controller: 'TrancheHoraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tranche-horaire', null, { reload: true });
                }, function() {
                    $state.go('tranche-horaire');
                });
            }]
        })
        .state('tranche-horaire.edit', {
            parent: 'tranche-horaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche-horaire/tranche-horaire-dialog.html',
                    controller: 'TrancheHoraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TrancheHoraire', function(TrancheHoraire) {
                            return TrancheHoraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranche-horaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranche-horaire.delete', {
            parent: 'tranche-horaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranche-horaire/tranche-horaire-delete-dialog.html',
                    controller: 'TrancheHoraireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TrancheHoraire', function(TrancheHoraire) {
                            return TrancheHoraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranche-horaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
