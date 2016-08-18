(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('format-matricule', {
            parent: 'entity',
            url: '/format-matricule',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.formatMatricule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/format-matricule/format-matricules.html',
                    controller: 'FormatMatriculeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('formatMatricule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('format-matricule-detail', {
            parent: 'entity',
            url: '/format-matricule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.formatMatricule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/format-matricule/format-matricule-detail.html',
                    controller: 'FormatMatriculeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('formatMatricule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FormatMatricule', function($stateParams, FormatMatricule) {
                    return FormatMatricule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'format-matricule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('format-matricule-detail.edit', {
            parent: 'format-matricule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/format-matricule/format-matricule-dialog.html',
                    controller: 'FormatMatriculeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormatMatricule', function(FormatMatricule) {
                            return FormatMatricule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('format-matricule.new', {
            parent: 'format-matricule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/format-matricule/format-matricule-dialog.html',
                    controller: 'FormatMatriculeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                format: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('format-matricule', null, { reload: true });
                }, function() {
                    $state.go('format-matricule');
                });
            }]
        })
        .state('format-matricule.edit', {
            parent: 'format-matricule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/format-matricule/format-matricule-dialog.html',
                    controller: 'FormatMatriculeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormatMatricule', function(FormatMatricule) {
                            return FormatMatricule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('format-matricule', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('format-matricule.delete', {
            parent: 'format-matricule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/format-matricule/format-matricule-delete-dialog.html',
                    controller: 'FormatMatriculeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FormatMatricule', function(FormatMatricule) {
                            return FormatMatricule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('format-matricule', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
