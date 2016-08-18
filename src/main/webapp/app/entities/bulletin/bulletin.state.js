(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bulletin', {
            parent: 'entity',
            url: '/bulletin',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.bulletin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bulletin/bulletins.html',
                    controller: 'BulletinController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bulletin');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bulletin-detail', {
            parent: 'entity',
            url: '/bulletin/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.bulletin.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bulletin/bulletin-detail.html',
                    controller: 'BulletinDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bulletin');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bulletin', function($stateParams, Bulletin) {
                    return Bulletin.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bulletin',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bulletin-detail.edit', {
            parent: 'bulletin-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bulletin/bulletin-dialog.html',
                    controller: 'BulletinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bulletin', function(Bulletin) {
                            return Bulletin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bulletin.new', {
            parent: 'bulletin',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bulletin/bulletin-dialog.html',
                    controller: 'BulletinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                moyenne: null,
                                rang: null,
                                anneeScolaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bulletin', null, { reload: true });
                }, function() {
                    $state.go('bulletin');
                });
            }]
        })
        .state('bulletin.edit', {
            parent: 'bulletin',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bulletin/bulletin-dialog.html',
                    controller: 'BulletinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bulletin', function(Bulletin) {
                            return Bulletin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bulletin', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bulletin.delete', {
            parent: 'bulletin',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bulletin/bulletin-delete-dialog.html',
                    controller: 'BulletinDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bulletin', function(Bulletin) {
                            return Bulletin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bulletin', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
