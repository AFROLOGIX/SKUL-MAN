(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('matiere', {
            parent: 'entity',
            url: '/matiere',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.matiere.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/matiere/matieres.html',
                    controller: 'MatiereController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('matiere');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('matiere-detail', {
            parent: 'entity',
            url: '/matiere/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.matiere.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/matiere/matiere-detail.html',
                    controller: 'MatiereDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('matiere');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Matiere', function($stateParams, Matiere) {
                    return Matiere.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'matiere',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('matiere-detail.edit', {
            parent: 'matiere-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Matiere', function(Matiere) {
                            return Matiere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('matiere.new', {
            parent: 'matiere',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
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
                    $state.go('matiere', null, { reload: true });
                }, function() {
                    $state.go('matiere');
                });
            }]
        })
        .state('matiere.edit', {
            parent: 'matiere',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Matiere', function(Matiere) {
                            return Matiere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('matiere', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('matiere.delete', {
            parent: 'matiere',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-delete-dialog.html',
                    controller: 'MatiereDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Matiere', function(Matiere) {
                            return Matiere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('matiere', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
