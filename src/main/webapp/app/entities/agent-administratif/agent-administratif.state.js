(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agent-administratif', {
            parent: 'entity',
            url: '/agent-administratif',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.agentAdministratif.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-administratif/agent-administratifs.html',
                    controller: 'AgentAdministratifController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agentAdministratif');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agent-administratif-detail', {
            parent: 'entity',
            url: '/agent-administratif/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.agentAdministratif.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-administratif/agent-administratif-detail.html',
                    controller: 'AgentAdministratifDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agentAdministratif');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AgentAdministratif', function($stateParams, AgentAdministratif) {
                    return AgentAdministratif.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agent-administratif',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agent-administratif-detail.edit', {
            parent: 'agent-administratif-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-administratif/agent-administratif-dialog.html',
                    controller: 'AgentAdministratifDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentAdministratif', function(AgentAdministratif) {
                            return AgentAdministratif.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-administratif.new', {
            parent: 'agent-administratif',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-administratif/agent-administratif-dialog.html',
                    controller: 'AgentAdministratifDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                role: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agent-administratif', null, { reload: true });
                }, function() {
                    $state.go('agent-administratif');
                });
            }]
        })
        .state('agent-administratif.edit', {
            parent: 'agent-administratif',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-administratif/agent-administratif-dialog.html',
                    controller: 'AgentAdministratifDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentAdministratif', function(AgentAdministratif) {
                            return AgentAdministratif.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-administratif', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-administratif.delete', {
            parent: 'agent-administratif',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-administratif/agent-administratif-delete-dialog.html',
                    controller: 'AgentAdministratifDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AgentAdministratif', function(AgentAdministratif) {
                            return AgentAdministratif.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-administratif', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
