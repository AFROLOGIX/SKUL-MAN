(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sequence', {
            parent: 'entity',
            url: '/sequence',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.sequence.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sequence/sequences.html',
                    controller: 'SequenceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sequence');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sequence-detail', {
            parent: 'entity',
            url: '/sequence/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.sequence.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sequence/sequence-detail.html',
                    controller: 'SequenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sequence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sequence', function($stateParams, Sequence) {
                    return Sequence.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sequence',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sequence-detail.edit', {
            parent: 'sequence-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sequence/sequence-dialog.html',
                    controller: 'SequenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sequence', function(Sequence) {
                            return Sequence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sequence.new', {
            parent: 'sequence',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sequence/sequence-dialog.html',
                    controller: 'SequenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelleFr: null,
                                libelleEn: null,
                                anneeScolaire: null,
                                dateDeb: null,
                                dateFin: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sequence', null, { reload: true });
                }, function() {
                    $state.go('sequence');
                });
            }]
        })
        .state('sequence.edit', {
            parent: 'sequence',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sequence/sequence-dialog.html',
                    controller: 'SequenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sequence', function(Sequence) {
                            return Sequence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sequence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sequence.delete', {
            parent: 'sequence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sequence/sequence-delete-dialog.html',
                    controller: 'SequenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sequence', function(Sequence) {
                            return Sequence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sequence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
