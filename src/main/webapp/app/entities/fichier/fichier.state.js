(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fichier', {
            parent: 'entity',
            url: '/fichier',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.fichier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fichier/fichiers.html',
                    controller: 'FichierController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fichier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fichier-detail', {
            parent: 'entity',
            url: '/fichier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.fichier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fichier/fichier-detail.html',
                    controller: 'FichierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fichier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fichier', function($stateParams, Fichier) {
                    return Fichier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fichier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fichier-detail.edit', {
            parent: 'fichier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichier/fichier-dialog.html',
                    controller: 'FichierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fichier', function(Fichier) {
                            return Fichier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fichier.new', {
            parent: 'fichier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichier/fichier-dialog.html',
                    controller: 'FichierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                libelleFr: null,
                                libelleEn: null,
                                emplacement: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fichier', null, { reload: true });
                }, function() {
                    $state.go('fichier');
                });
            }]
        })
        .state('fichier.edit', {
            parent: 'fichier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichier/fichier-dialog.html',
                    controller: 'FichierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fichier', function(Fichier) {
                            return Fichier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fichier', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fichier.delete', {
            parent: 'fichier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichier/fichier-delete-dialog.html',
                    controller: 'FichierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fichier', function(Fichier) {
                            return Fichier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fichier', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
