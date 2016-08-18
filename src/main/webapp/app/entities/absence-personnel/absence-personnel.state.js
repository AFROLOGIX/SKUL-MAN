(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('absence-personnel', {
            parent: 'entity',
            url: '/absence-personnel',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.absencePersonnel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/absence-personnel/absence-personnels.html',
                    controller: 'AbsencePersonnelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('absencePersonnel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('absence-personnel-detail', {
            parent: 'entity',
            url: '/absence-personnel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.absencePersonnel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/absence-personnel/absence-personnel-detail.html',
                    controller: 'AbsencePersonnelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('absencePersonnel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AbsencePersonnel', function($stateParams, AbsencePersonnel) {
                    return AbsencePersonnel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'absence-personnel',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('absence-personnel-detail.edit', {
            parent: 'absence-personnel-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-personnel/absence-personnel-dialog.html',
                    controller: 'AbsencePersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AbsencePersonnel', function(AbsencePersonnel) {
                            return AbsencePersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('absence-personnel.new', {
            parent: 'absence-personnel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-personnel/absence-personnel-dialog.html',
                    controller: 'AbsencePersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                periode: null,
                                justifiee: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('absence-personnel', null, { reload: true });
                }, function() {
                    $state.go('absence-personnel');
                });
            }]
        })
        .state('absence-personnel.edit', {
            parent: 'absence-personnel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-personnel/absence-personnel-dialog.html',
                    controller: 'AbsencePersonnelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AbsencePersonnel', function(AbsencePersonnel) {
                            return AbsencePersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('absence-personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('absence-personnel.delete', {
            parent: 'absence-personnel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-personnel/absence-personnel-delete-dialog.html',
                    controller: 'AbsencePersonnelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AbsencePersonnel', function(AbsencePersonnel) {
                            return AbsencePersonnel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('absence-personnel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
