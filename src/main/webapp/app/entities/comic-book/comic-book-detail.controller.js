(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicBookDetailController', ComicBookDetailController);

    ComicBookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ComicBook', 'ComicPage'];

    function ComicBookDetailController($scope, $rootScope, $stateParams, previousState, entity, ComicBook, ComicPage) {
        var vm = this;

        vm.comicBook = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('comicBookApp:comicBookUpdate', function(event, result) {
            vm.comicBook = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
