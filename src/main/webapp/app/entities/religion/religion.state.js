(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('religion', {
            parent: 'entity',
            url: '/religion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.religion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/religion/religions.html',
                    controller: 'ReligionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('religion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('religion-detail', {
            parent: 'entity',
            url: '/religion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.religion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/religion/religion-detail.html',
                    controller: 'ReligionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('religion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Religion', function($stateParams, Religion) {
                    return Religion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'religion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('religion-detail.edit', {
            parent: 'religion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/religion/religion-dialog.html',
                    controller: 'ReligionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Religion', function(Religion) {
                            return Religion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('religion.new', {
            parent: 'religion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/religion/religion-dialog.html',
                    controller: 'ReligionDialogController',
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
                    $state.go('religion', null, { reload: true });
                }, function() {
                    $state.go('religion');
                });
            }]
        })
        .state('religion.edit', {
            parent: 'religion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/religion/religion-dialog.html',
                    controller: 'ReligionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Religion', function(Religion) {
                            return Religion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('religion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('religion.delete', {
            parent: 'religion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/religion/religion-delete-dialog.html',
                    controller: 'ReligionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Religion', function(Religion) {
                            return Religion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('religion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
