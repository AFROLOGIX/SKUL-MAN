(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('section', {
            parent: 'entity',
            url: '/section',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.section.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/section/sections.html',
                    controller: 'SectionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('section');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('section-detail', {
            parent: 'entity',
            url: '/section/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'skulmanApp.section.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/section/section-detail.html',
                    controller: 'SectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('section');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Section', function($stateParams, Section) {
                    return Section.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'section',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('section-detail.edit', {
            parent: 'section-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section/section-dialog.html',
                    controller: 'SectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Section', function(Section) {
                            return Section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('section.new', {
            parent: 'section',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section/section-dialog.html',
                    controller: 'SectionDialogController',
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
                    $state.go('section', null, { reload: true });
                }, function() {
                    $state.go('section');
                });
            }]
        })
        .state('section.edit', {
            parent: 'section',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section/section-dialog.html',
                    controller: 'SectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Section', function(Section) {
                            return Section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('section', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('section.delete', {
            parent: 'section',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section/section-delete-dialog.html',
                    controller: 'SectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Section', function(Section) {
                            return Section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('section', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
