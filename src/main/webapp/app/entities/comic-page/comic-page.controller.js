(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicPageController', ComicPageController);

    ComicPageController.$inject = ['$scope', '$state', 'DataUtils', 'ComicPage'];

    function ComicPageController ($scope, $state, DataUtils, ComicPage) {
        var vm = this;
        
        vm.comicPages = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ComicPage.query(function(result) {
                vm.comicPages = result;
            });
        }
    }
})();
