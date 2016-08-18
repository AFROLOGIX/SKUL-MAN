(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('versement-eleve', {
            parent: 'entity',
            url: '/versement-eleve',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.versementEleve.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/versement-eleve/versement-eleves.html',
                    controller: 'VersementEleveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('versementEleve');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('versement-eleve-detail', {
            parent: 'entity',
            url: '/versement-eleve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.versementEleve.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/versement-eleve/versement-eleve-detail.html',
                    controller: 'VersementEleveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('versementEleve');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VersementEleve', function($stateParams, VersementEleve) {
                    return VersementEleve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'versement-eleve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('versement-eleve-detail.edit', {
            parent: 'versement-eleve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/versement-eleve/versement-eleve-dialog.html',
                    controller: 'VersementEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VersementEleve', function(VersementEleve) {
                            return VersementEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('versement-eleve.new', {
            parent: 'versement-eleve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/versement-eleve/versement-eleve-dialog.html',
                    controller: 'VersementEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneeScolaire: null,
                                periode: null,
                                montant: null,
                                dette: null,
                                createBy: null,
                                updateBy: null,
                                createAt: null,
                                updateAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('versement-eleve', null, { reload: true });
                }, function() {
                    $state.go('versement-eleve');
                });
            }]
        })
        .state('versement-eleve.edit', {
            parent: 'versement-eleve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/versement-eleve/versement-eleve-dialog.html',
                    controller: 'VersementEleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VersementEleve', function(VersementEleve) {
                            return VersementEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('versement-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('versement-eleve.delete', {
            parent: 'versement-eleve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/versement-eleve/versement-eleve-delete-dialog.html',
                    controller: 'VersementEleveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VersementEleve', function(VersementEleve) {
                            return VersementEleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('versement-eleve', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
