(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        })
        .state('view-comic-book', {
            parent: 'entity',
            url: '/comic-book/{id}',
            data: {
                authorities: [],
                pageTitle: 'comicBookApp.comicBook.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comic-book/comic-book-detail.html',
                    controller: 'ComicBookDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comicBook');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ComicBook', function($stateParams, ComicBook) {
                    return ComicBook.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'comic-book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        });
    }
})();
