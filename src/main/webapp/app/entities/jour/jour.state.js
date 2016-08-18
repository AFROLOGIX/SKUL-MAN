(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('jour', {
            parent: 'entity',
            url: '/jour',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.jour.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/jour/jours.html',
                    controller: 'JourController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jour');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('jour-detail', {
            parent: 'entity',
            url: '/jour/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.jour.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/jour/jour-detail.html',
                    controller: 'JourDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jour');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Jour', function($stateParams, Jour) {
                    return Jour.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'jour',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('jour-detail.edit', {
            parent: 'jour-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jour/jour-dialog.html',
                    controller: 'JourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Jour', function(Jour) {
                            return Jour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('jour.new', {
            parent: 'jour',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jour/jour-dialog.html',
                    controller: 'JourDialogController',
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
                    $state.go('jour', null, { reload: true });
                }, function() {
                    $state.go('jour');
                });
            }]
        })
        .state('jour.edit', {
            parent: 'jour',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jour/jour-dialog.html',
                    controller: 'JourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Jour', function(Jour) {
                            return Jour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('jour', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('jour.delete', {
            parent: 'jour',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jour/jour-delete-dialog.html',
                    controller: 'JourDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Jour', function(Jour) {
                            return Jour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('jour', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
