(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('absence-eleve', {
            parent: 'entity',
            url: '/absence-eleve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.absenceEleve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/absence-eleve/absence-eleves.html',
                    controller: 'AbsenceEleveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('absenceEleve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('absence-eleve-detail', {
            parent: 'entity',
            url: '/absence-eleve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.absenceEleve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/absence-eleve/absence-eleve-detail.html',
                    controller: 'AbsenceEleveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('absenceEleve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AbsenceEleve', function($stateParams, AbsenceEleve) {
                    return AbsenceEleve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'absence-eleve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('absence-eleve-detail.edit', {
            parent: 'absence-eleve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-eleve/absence-eleve-dialog.html',
                    controller: 'AbsenceEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AbsenceEleve', function(AbsenceEleve) {
                            return AbsenceEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('absence-eleve.new', {
            parent: 'absence-eleve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-eleve/absence-eleve-dialog.html',
                    controller: 'AbsenceEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                plageHoraire: null,
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
                    $state.go('absence-eleve', null, { reload: true });
                }, function() {
                    $state.go('absence-eleve');
                });
            }]
        })
        .state('absence-eleve.edit', {
            parent: 'absence-eleve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-eleve/absence-eleve-dialog.html',
                    controller: 'AbsenceEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AbsenceEleve', function(AbsenceEleve) {
                            return AbsenceEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('absence-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('absence-eleve.delete', {
            parent: 'absence-eleve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/absence-eleve/absence-eleve-delete-dialog.html',
                    controller: 'AbsenceEleveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AbsenceEleve', function(AbsenceEleve) {
                            return AbsenceEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('absence-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
