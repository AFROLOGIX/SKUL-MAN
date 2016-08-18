(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trimestre', {
            parent: 'entity',
            url: '/trimestre',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.trimestre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trimestre/trimestres.html',
                    controller: 'TrimestreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trimestre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('trimestre-detail', {
            parent: 'entity',
            url: '/trimestre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.trimestre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trimestre/trimestre-detail.html',
                    controller: 'TrimestreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trimestre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Trimestre', function($stateParams, Trimestre) {
                    return Trimestre.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trimestre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trimestre-detail.edit', {
            parent: 'trimestre-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-dialog.html',
                    controller: 'TrimestreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trimestre', function(Trimestre) {
                            return Trimestre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trimestre.new', {
            parent: 'trimestre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-dialog.html',
                    controller: 'TrimestreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                dateDeb: null,
                                dateFin: null,
                                anneeScolaire: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: true });
                }, function() {
                    $state.go('trimestre');
                });
            }]
        })
        .state('trimestre.edit', {
            parent: 'trimestre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-dialog.html',
                    controller: 'TrimestreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trimestre', function(Trimestre) {
                            return Trimestre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trimestre.delete', {
            parent: 'trimestre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-delete-dialog.html',
                    controller: 'TrimestreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Trimestre', function(Trimestre) {
                            return Trimestre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
