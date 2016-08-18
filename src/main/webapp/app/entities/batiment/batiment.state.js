(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('batiment', {
            parent: 'entity',
            url: '/batiment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.batiment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/batiment/batiments.html',
                    controller: 'BatimentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('batiment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('batiment-detail', {
            parent: 'entity',
            url: '/batiment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.batiment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/batiment/batiment-detail.html',
                    controller: 'BatimentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('batiment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Batiment', function($stateParams, Batiment) {
                    return Batiment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'batiment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('batiment-detail.edit', {
            parent: 'batiment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/batiment/batiment-dialog.html',
                    controller: 'BatimentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Batiment', function(Batiment) {
                            return Batiment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('batiment.new', {
            parent: 'batiment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/batiment/batiment-dialog.html',
                    controller: 'BatimentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                localisation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('batiment', null, { reload: true });
                }, function() {
                    $state.go('batiment');
                });
            }]
        })
        .state('batiment.edit', {
            parent: 'batiment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/batiment/batiment-dialog.html',
                    controller: 'BatimentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Batiment', function(Batiment) {
                            return Batiment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('batiment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('batiment.delete', {
            parent: 'batiment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/batiment/batiment-delete-dialog.html',
                    controller: 'BatimentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Batiment', function(Batiment) {
                            return Batiment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('batiment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
