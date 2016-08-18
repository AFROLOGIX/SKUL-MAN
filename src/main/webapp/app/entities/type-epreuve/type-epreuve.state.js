(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-epreuve', {
            parent: 'entity',
            url: '/type-epreuve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeEpreuve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-epreuve/type-epreuves.html',
                    controller: 'TypeEpreuveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeEpreuve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-epreuve-detail', {
            parent: 'entity',
            url: '/type-epreuve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.typeEpreuve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-epreuve/type-epreuve-detail.html',
                    controller: 'TypeEpreuveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeEpreuve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeEpreuve', function($stateParams, TypeEpreuve) {
                    return TypeEpreuve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-epreuve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-epreuve-detail.edit', {
            parent: 'type-epreuve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-epreuve/type-epreuve-dialog.html',
                    controller: 'TypeEpreuveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeEpreuve', function(TypeEpreuve) {
                            return TypeEpreuve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-epreuve.new', {
            parent: 'type-epreuve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-epreuve/type-epreuve-dialog.html',
                    controller: 'TypeEpreuveDialogController',
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
                    $state.go('type-epreuve', null, { reload: true });
                }, function() {
                    $state.go('type-epreuve');
                });
            }]
        })
        .state('type-epreuve.edit', {
            parent: 'type-epreuve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-epreuve/type-epreuve-dialog.html',
                    controller: 'TypeEpreuveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeEpreuve', function(TypeEpreuve) {
                            return TypeEpreuve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-epreuve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-epreuve.delete', {
            parent: 'type-epreuve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-epreuve/type-epreuve-delete-dialog.html',
                    controller: 'TypeEpreuveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeEpreuve', function(TypeEpreuve) {
                            return TypeEpreuve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-epreuve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
