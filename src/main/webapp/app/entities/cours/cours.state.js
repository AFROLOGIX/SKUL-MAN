(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cours', {
            parent: 'entity',
            url: '/cours',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.cours.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cours/cours.html',
                    controller: 'CoursController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cours');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cours-detail', {
            parent: 'entity',
            url: '/cours/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.cours.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cours/cours-detail.html',
                    controller: 'CoursDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cours');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cours', function($stateParams, Cours) {
                    return Cours.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cours',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cours-detail.edit', {
            parent: 'cours-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cours/cours-dialog.html',
                    controller: 'CoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cours', function(Cours) {
                            return Cours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cours.new', {
            parent: 'cours',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cours/cours-dialog.html',
                    controller: 'CoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cours', null, { reload: true });
                }, function() {
                    $state.go('cours');
                });
            }]
        })
        .state('cours.edit', {
            parent: 'cours',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cours/cours-dialog.html',
                    controller: 'CoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cours', function(Cours) {
                            return Cours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cours', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cours.delete', {
            parent: 'cours',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cours/cours-delete-dialog.html',
                    controller: 'CoursDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cours', function(Cours) {
                            return Cours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cours', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
