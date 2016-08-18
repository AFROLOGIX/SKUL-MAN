(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-personnel', {
            parent: 'entity',
            url: '/type-personnel',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typePersonnel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-personnel/type-personnels.html',
                    controller: 'TypePersonnelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typePersonnel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-personnel-detail', {
            parent: 'entity',
            url: '/type-personnel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typePersonnel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-personnel/type-personnel-detail.html',
                    controller: 'TypePersonnelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typePersonnel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypePersonnel', function($stateParams, TypePersonnel) {
                    return TypePersonnel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-personnel',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-personnel-detail.edit', {
            parent: 'type-personnel-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-personnel/type-personnel-dialog.html',
                    controller: 'TypePersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypePersonnel', function(TypePersonnel) {
                            return TypePersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-personnel.new', {
            parent: 'type-personnel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-personnel/type-personnel-dialog.html',
                    controller: 'TypePersonnelDialogController',
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
                    $state.go('type-personnel', null, { reload: true });
                }, function() {
                    $state.go('type-personnel');
                });
            }]
        })
        .state('type-personnel.edit', {
            parent: 'type-personnel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-personnel/type-personnel-dialog.html',
                    controller: 'TypePersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypePersonnel', function(TypePersonnel) {
                            return TypePersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-personnel.delete', {
            parent: 'type-personnel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-personnel/type-personnel-delete-dialog.html',
                    controller: 'TypePersonnelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypePersonnel', function(TypePersonnel) {
                            return TypePersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
