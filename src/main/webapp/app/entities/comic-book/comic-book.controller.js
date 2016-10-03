(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicBookController', ComicBookController);

    ComicBookController.$inject = ['$scope', '$state', 'ComicBook'];

    function ComicBookController ($scope, $state, ComicBook) {
        var vm = this;
        
        vm.comicBooks = [];

        loadAll();

        function loadAll() {
            ComicBook.query(function(result) {
                vm.comicBooks = result;
            });
        }
    }
})();
