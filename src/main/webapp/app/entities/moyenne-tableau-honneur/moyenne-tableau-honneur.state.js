(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('moyenne-tableau-honneur', {
            parent: 'entity',
            url: '/moyenne-tableau-honneur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.moyenneTableauHonneur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/moyenne-tableau-honneur/moyenne-tableau-honneurs.html',
                    controller: 'MoyenneTableauHonneurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('moyenneTableauHonneur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('moyenne-tableau-honneur-detail', {
            parent: 'entity',
            url: '/moyenne-tableau-honneur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.moyenneTableauHonneur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/moyenne-tableau-honneur/moyenne-tableau-honneur-detail.html',
                    controller: 'MoyenneTableauHonneurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('moyenneTableauHonneur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MoyenneTableauHonneur', function($stateParams, MoyenneTableauHonneur) {
                    return MoyenneTableauHonneur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'moyenne-tableau-honneur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('moyenne-tableau-honneur-detail.edit', {
            parent: 'moyenne-tableau-honneur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moyenne-tableau-honneur/moyenne-tableau-honneur-dialog.html',
                    controller: 'MoyenneTableauHonneurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MoyenneTableauHonneur', function(MoyenneTableauHonneur) {
                            return MoyenneTableauHonneur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('moyenne-tableau-honneur.new', {
            parent: 'moyenne-tableau-honneur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moyenne-tableau-honneur/moyenne-tableau-honneur-dialog.html',
                    controller: 'MoyenneTableauHonneurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                conditionTableauHonneur: null,
                                conditionEncouragement: null,
                                conditionFelicitation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('moyenne-tableau-honneur', null, { reload: true });
                }, function() {
                    $state.go('moyenne-tableau-honneur');
                });
            }]
        })
        .state('moyenne-tableau-honneur.edit', {
            parent: 'moyenne-tableau-honneur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moyenne-tableau-honneur/moyenne-tableau-honneur-dialog.html',
                    controller: 'MoyenneTableauHonneurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MoyenneTableauHonneur', function(MoyenneTableauHonneur) {
                            return MoyenneTableauHonneur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('moyenne-tableau-honneur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('moyenne-tableau-honneur.delete', {
            parent: 'moyenne-tableau-honneur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/moyenne-tableau-honneur/moyenne-tableau-honneur-delete-dialog.html',
                    controller: 'MoyenneTableauHonneurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MoyenneTableauHonneur', function(MoyenneTableauHonneur) {
                            return MoyenneTableauHonneur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('moyenne-tableau-honneur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
