(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicBookDetailController', ComicBookDetailController);

    ComicBookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ComicBook', 'ComicPage'];

    function ComicBookDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ComicBook, ComicPage) {
        var vm = this;

        vm.comicBook = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        
        vm.setImage = function setImage(coverImageData) {
        	vm.comicBook.coverImageData = coverImageData;
        };        

        var unsubscribe = $rootScope.$on('comicBookApp:comicBookUpdate', function(event, result) {
            vm.comicBook = result;
        });
        
        $scope.$on('$destroy', unsubscribe);
    }
})();
