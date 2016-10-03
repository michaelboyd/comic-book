(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicPageDetailController', ComicPageDetailController);

    ComicPageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ComicPage', 'ComicBook'];

    function ComicPageDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ComicPage, ComicBook) {
        var vm = this;

        vm.comicPage = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('comicBookApp:comicPageUpdate', function(event, result) {
            vm.comicPage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
