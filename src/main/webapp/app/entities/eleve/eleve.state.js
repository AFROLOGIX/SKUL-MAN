(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('eleve', {
            parent: 'entity',
            url: '/eleve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.eleve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/eleve/eleves.html',
                    controller: 'EleveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('eleve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('eleve-detail', {
            parent: 'entity',
            url: '/eleve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.eleve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/eleve/eleve-detail.html',
                    controller: 'EleveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('eleve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Eleve', function($stateParams, Eleve) {
                    return Eleve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'eleve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('eleve-detail.edit', {
            parent: 'eleve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-dialog.html',
                    controller: 'EleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Eleve', function(Eleve) {
                            return Eleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('eleve.new', {
            parent: 'eleve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-dialog.html',
                    controller: 'EleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                matricule: null,
                                nom: null,
                                prenom: null,
                                dateNaiss: null,
                                lieuNaiss: null,
                                sexe: null,
                                tel: null,
                                nationalite: null,
                                email: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('eleve', null, { reload: true });
                }, function() {
                    $state.go('eleve');
                });
            }]
        })
        .state('eleve.edit', {
            parent: 'eleve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-dialog.html',
                    controller: 'EleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Eleve', function(Eleve) {
                            return Eleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('eleve.delete', {
            parent: 'eleve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-delete-dialog.html',
                    controller: 'EleveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Eleve', function(Eleve) {
                            return Eleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
