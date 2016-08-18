(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('epreuve', {
            parent: 'entity',
            url: '/epreuve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.epreuve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/epreuve/epreuves.html',
                    controller: 'EpreuveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('epreuve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('epreuve-detail', {
            parent: 'entity',
            url: '/epreuve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.epreuve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/epreuve/epreuve-detail.html',
                    controller: 'EpreuveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('epreuve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Epreuve', function($stateParams, Epreuve) {
                    return Epreuve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'epreuve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('epreuve-detail.edit', {
            parent: 'epreuve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/epreuve/epreuve-dialog.html',
                    controller: 'EpreuveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Epreuve', function(Epreuve) {
                            return Epreuve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('epreuve.new', {
            parent: 'epreuve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/epreuve/epreuve-dialog.html',
                    controller: 'EpreuveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('epreuve', null, { reload: true });
                }, function() {
                    $state.go('epreuve');
                });
            }]
        })
        .state('epreuve.edit', {
            parent: 'epreuve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/epreuve/epreuve-dialog.html',
                    controller: 'EpreuveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Epreuve', function(Epreuve) {
                            return Epreuve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('epreuve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('epreuve.delete', {
            parent: 'epreuve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/epreuve/epreuve-delete-dialog.html',
                    controller: 'EpreuveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Epreuve', function(Epreuve) {
                            return Epreuve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('epreuve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
