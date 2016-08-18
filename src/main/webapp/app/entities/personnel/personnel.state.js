(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('personnel', {
            parent: 'entity',
            url: '/personnel',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.personnel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personnel/personnels.html',
                    controller: 'PersonnelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personnel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('personnel-detail', {
            parent: 'entity',
            url: '/personnel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.personnel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personnel/personnel-detail.html',
                    controller: 'PersonnelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personnel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Personnel', function($stateParams, Personnel) {
                    return Personnel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'personnel',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('personnel-detail.edit', {
            parent: 'personnel-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnel/personnel-dialog.html',
                    controller: 'PersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Personnel', function(Personnel) {
                            return Personnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personnel.new', {
            parent: 'personnel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnel/personnel-dialog.html',
                    controller: 'PersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                tel: null,
                                email: null,
                                dateAdmission: null,
                                isActive: null,
                                adresse: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('personnel', null, { reload: true });
                }, function() {
                    $state.go('personnel');
                });
            }]
        })
        .state('personnel.edit', {
            parent: 'personnel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnel/personnel-dialog.html',
                    controller: 'PersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Personnel', function(Personnel) {
                            return Personnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personnel.delete', {
            parent: 'personnel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnel/personnel-delete-dialog.html',
                    controller: 'PersonnelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Personnel', function(Personnel) {
                            return Personnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
