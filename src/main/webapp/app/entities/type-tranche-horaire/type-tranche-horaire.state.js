(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-tranche-horaire', {
            parent: 'entity',
            url: '/type-tranche-horaire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeTrancheHoraire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-tranche-horaire/type-tranche-horaires.html',
                    controller: 'TypeTrancheHoraireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeTrancheHoraire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-tranche-horaire-detail', {
            parent: 'entity',
            url: '/type-tranche-horaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeTrancheHoraire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-tranche-horaire/type-tranche-horaire-detail.html',
                    controller: 'TypeTrancheHoraireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeTrancheHoraire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeTrancheHoraire', function($stateParams, TypeTrancheHoraire) {
                    return TypeTrancheHoraire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-tranche-horaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-tranche-horaire-detail.edit', {
            parent: 'type-tranche-horaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-tranche-horaire/type-tranche-horaire-dialog.html',
                    controller: 'TypeTrancheHoraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeTrancheHoraire', function(TypeTrancheHoraire) {
                            return TypeTrancheHoraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-tranche-horaire.new', {
            parent: 'type-tranche-horaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-tranche-horaire/type-tranche-horaire-dialog.html',
                    controller: 'TypeTrancheHoraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                heureDeb: null,
                                heureFin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-tranche-horaire', null, { reload: true });
                }, function() {
                    $state.go('type-tranche-horaire');
                });
            }]
        })
        .state('type-tranche-horaire.edit', {
            parent: 'type-tranche-horaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-tranche-horaire/type-tranche-horaire-dialog.html',
                    controller: 'TypeTrancheHoraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeTrancheHoraire', function(TypeTrancheHoraire) {
                            return TypeTrancheHoraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-tranche-horaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-tranche-horaire.delete', {
            parent: 'type-tranche-horaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-tranche-horaire/type-tranche-horaire-delete-dialog.html',
                    controller: 'TypeTrancheHoraireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeTrancheHoraire', function(TypeTrancheHoraire) {
                            return TypeTrancheHoraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-tranche-horaire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
