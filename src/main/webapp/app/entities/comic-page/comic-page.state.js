(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comic-page', {
            parent: 'entity',
            url: '/comic-page',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicBookApp.comicPage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comic-page/comic-pages.html',
                    controller: 'ComicPageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comicPage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('comic-page-detail', {
            parent: 'entity',
            url: '/comic-page/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicBookApp.comicPage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comic-page/comic-page-detail.html',
                    controller: 'ComicPageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comicPage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ComicPage', function($stateParams, ComicPage) {
                    return ComicPage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'comic-page',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('comic-page-detail.edit', {
            parent: 'comic-page-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-page/comic-page-dialog.html',
                    controller: 'ComicPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComicPage', function(ComicPage) {
                            return ComicPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comic-page.new', {
            parent: 'comic-page',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-page/comic-page-dialog.html',
                    controller: 'ComicPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pageNumber: null,
                                imageData: null,
                                imageDataContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('comic-page', null, { reload: 'comic-page' });
                }, function() {
                    $state.go('comic-page');
                });
            }]
        })
        .state('comic-page.edit', {
            parent: 'comic-page',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-page/comic-page-dialog.html',
                    controller: 'ComicPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComicPage', function(ComicPage) {
                            return ComicPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comic-page', null, { reload: 'comic-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comic-page.delete', {
            parent: 'comic-page',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-page/comic-page-delete-dialog.html',
                    controller: 'ComicPageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ComicPage', function(ComicPage) {
                            return ComicPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comic-page', null, { reload: 'comic-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
