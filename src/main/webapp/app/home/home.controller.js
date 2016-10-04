(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'ComicBook'];

    function HomeController ($scope, Principal, LoginService, $state, ComicBook) {
        var vm = this;

        vm.register = register;


        loadAll();        

        function loadAll() {
            ComicBook.query(function(result) {
                vm.comicBooks = result;
            });
        }

        function register () {
            $state.go('register');
        }
    }
})();
