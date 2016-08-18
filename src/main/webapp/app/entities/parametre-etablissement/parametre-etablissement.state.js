(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('parametre-etablissement', {
            parent: 'entity',
            url: '/parametre-etablissement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.parametreEtablissement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parametre-etablissement/parametre-etablissements.html',
                    controller: 'ParametreEtablissementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parametreEtablissement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('parametre-etablissement-detail', {
            parent: 'entity',
            url: '/parametre-etablissement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.parametreEtablissement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parametre-etablissement/parametre-etablissement-detail.html',
                    controller: 'ParametreEtablissementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parametreEtablissement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ParametreEtablissement', function($stateParams, ParametreEtablissement) {
                    return ParametreEtablissement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'parametre-etablissement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('parametre-etablissement-detail.edit', {
            parent: 'parametre-etablissement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parametre-etablissement/parametre-etablissement-dialog.html',
                    controller: 'ParametreEtablissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ParametreEtablissement', function(ParametreEtablissement) {
                            return ParametreEtablissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parametre-etablissement.new', {
            parent: 'parametre-etablissement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parametre-etablissement/parametre-etablissement-dialog.html',
                    controller: 'ParametreEtablissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                activerEnregistrementBulletinNoteBd: null,
                                heureDebCours: null,
                                heureFinCours: null,
                                regime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('parametre-etablissement', null, { reload: true });
                }, function() {
                    $state.go('parametre-etablissement');
                });
            }]
        })
        .state('parametre-etablissement.edit', {
            parent: 'parametre-etablissement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parametre-etablissement/parametre-etablissement-dialog.html',
                    controller: 'ParametreEtablissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ParametreEtablissement', function(ParametreEtablissement) {
                            return ParametreEtablissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parametre-etablissement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parametre-etablissement.delete', {
            parent: 'parametre-etablissement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parametre-etablissement/parametre-etablissement-delete-dialog.html',
                    controller: 'ParametreEtablissementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ParametreEtablissement', function(ParametreEtablissement) {
                            return ParametreEtablissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parametre-etablissement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
