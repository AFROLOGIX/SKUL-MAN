(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('etablissement', {
            parent: 'entity',
            url: '/etablissement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.etablissement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/etablissement/etablissements.html',
                    controller: 'EtablissementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('etablissement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('etablissement-detail', {
            parent: 'entity',
            url: '/etablissement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.etablissement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/etablissement/etablissement-detail.html',
                    controller: 'EtablissementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('etablissement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Etablissement', function($stateParams, Etablissement) {
                    return Etablissement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'etablissement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('etablissement-detail.edit', {
            parent: 'etablissement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etablissement/etablissement-dialog.html',
                    controller: 'EtablissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Etablissement', function(Etablissement) {
                            return Etablissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('etablissement.new', {
            parent: 'etablissement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etablissement/etablissement-dialog.html',
                    controller: 'EtablissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                dateCreation: null,
                                titreResponsable: null,
                                ville: null,
                                nomReponsable: null,
                                siteWeb: null,
                                cheminLogo: null,
                                nbTrimestre: null,
                                nbSequence: null,
                                bp: null,
                                localisation: null,
                                tel: null,
                                email: null,
                                devise: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('etablissement', null, { reload: true });
                }, function() {
                    $state.go('etablissement');
                });
            }]
        })
        .state('etablissement.edit', {
            parent: 'etablissement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etablissement/etablissement-dialog.html',
                    controller: 'EtablissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Etablissement', function(Etablissement) {
                            return Etablissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('etablissement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('etablissement.delete', {
            parent: 'etablissement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etablissement/etablissement-delete-dialog.html',
                    controller: 'EtablissementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Etablissement', function(Etablissement) {
                            return Etablissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('etablissement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
