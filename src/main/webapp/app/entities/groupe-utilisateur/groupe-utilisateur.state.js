(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('groupe-utilisateur', {
            parent: 'entity',
            url: '/groupe-utilisateur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.groupeUtilisateur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/groupe-utilisateur/groupe-utilisateurs.html',
                    controller: 'GroupeUtilisateurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupeUtilisateur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('groupe-utilisateur-detail', {
            parent: 'entity',
            url: '/groupe-utilisateur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.groupeUtilisateur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/groupe-utilisateur/groupe-utilisateur-detail.html',
                    controller: 'GroupeUtilisateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupeUtilisateur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GroupeUtilisateur', function($stateParams, GroupeUtilisateur) {
                    return GroupeUtilisateur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'groupe-utilisateur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('groupe-utilisateur-detail.edit', {
            parent: 'groupe-utilisateur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe-utilisateur/groupe-utilisateur-dialog.html',
                    controller: 'GroupeUtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupeUtilisateur', function(GroupeUtilisateur) {
                            return GroupeUtilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('groupe-utilisateur.new', {
            parent: 'groupe-utilisateur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe-utilisateur/groupe-utilisateur-dialog.html',
                    controller: 'GroupeUtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('groupe-utilisateur', null, { reload: true });
                }, function() {
                    $state.go('groupe-utilisateur');
                });
            }]
        })
        .state('groupe-utilisateur.edit', {
            parent: 'groupe-utilisateur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe-utilisateur/groupe-utilisateur-dialog.html',
                    controller: 'GroupeUtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupeUtilisateur', function(GroupeUtilisateur) {
                            return GroupeUtilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('groupe-utilisateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('groupe-utilisateur.delete', {
            parent: 'groupe-utilisateur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe-utilisateur/groupe-utilisateur-delete-dialog.html',
                    controller: 'GroupeUtilisateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GroupeUtilisateur', function(GroupeUtilisateur) {
                            return GroupeUtilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('groupe-utilisateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
