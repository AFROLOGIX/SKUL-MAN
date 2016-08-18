(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-moratoire', {
            parent: 'entity',
            url: '/type-moratoire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeMoratoire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-moratoire/type-moratoires.html',
                    controller: 'TypeMoratoireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeMoratoire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-moratoire-detail', {
            parent: 'entity',
            url: '/type-moratoire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeMoratoire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-moratoire/type-moratoire-detail.html',
                    controller: 'TypeMoratoireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeMoratoire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeMoratoire', function($stateParams, TypeMoratoire) {
                    return TypeMoratoire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-moratoire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-moratoire-detail.edit', {
            parent: 'type-moratoire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-moratoire/type-moratoire-dialog.html',
                    controller: 'TypeMoratoireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeMoratoire', function(TypeMoratoire) {
                            return TypeMoratoire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-moratoire.new', {
            parent: 'type-moratoire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-moratoire/type-moratoire-dialog.html',
                    controller: 'TypeMoratoireDialogController',
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
                    $state.go('type-moratoire', null, { reload: true });
                }, function() {
                    $state.go('type-moratoire');
                });
            }]
        })
        .state('type-moratoire.edit', {
            parent: 'type-moratoire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-moratoire/type-moratoire-dialog.html',
                    controller: 'TypeMoratoireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeMoratoire', function(TypeMoratoire) {
                            return TypeMoratoire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-moratoire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-moratoire.delete', {
            parent: 'type-moratoire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-moratoire/type-moratoire-delete-dialog.html',
                    controller: 'TypeMoratoireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeMoratoire', function(TypeMoratoire) {
                            return TypeMoratoire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-moratoire', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
