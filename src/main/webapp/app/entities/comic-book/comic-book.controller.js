(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicBookController', ComicBookController);

    ComicBookController.$inject = ['$scope', '$state', 'DataUtils', 'ComicBook'];

    function ComicBookController ($scope, $state, DataUtils, ComicBook) {
        var vm = this;
        
        vm.comicBooks = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ComicBook.query(function(result) {
                vm.comicBooks = result;
            });
        }
    }
})();
