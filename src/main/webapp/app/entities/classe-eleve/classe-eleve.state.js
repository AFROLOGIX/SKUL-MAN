(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('classe-eleve', {
            parent: 'entity',
            url: '/classe-eleve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.classeEleve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classe-eleve/classe-eleves.html',
                    controller: 'ClasseEleveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classeEleve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('classe-eleve-detail', {
            parent: 'entity',
            url: '/classe-eleve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.classeEleve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classe-eleve/classe-eleve-detail.html',
                    controller: 'ClasseEleveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classeEleve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClasseEleve', function($stateParams, ClasseEleve) {
                    return ClasseEleve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'classe-eleve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('classe-eleve-detail.edit', {
            parent: 'classe-eleve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe-eleve/classe-eleve-dialog.html',
                    controller: 'ClasseEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClasseEleve', function(ClasseEleve) {
                            return ClasseEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classe-eleve.new', {
            parent: 'classe-eleve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe-eleve/classe-eleve-dialog.html',
                    controller: 'ClasseEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                isActive: null,
                                observation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('classe-eleve', null, { reload: true });
                }, function() {
                    $state.go('classe-eleve');
                });
            }]
        })
        .state('classe-eleve.edit', {
            parent: 'classe-eleve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe-eleve/classe-eleve-dialog.html',
                    controller: 'ClasseEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClasseEleve', function(ClasseEleve) {
                            return ClasseEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classe-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classe-eleve.delete', {
            parent: 'classe-eleve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe-eleve/classe-eleve-delete-dialog.html',
                    controller: 'ClasseEleveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClasseEleve', function(ClasseEleve) {
                            return ClasseEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classe-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
