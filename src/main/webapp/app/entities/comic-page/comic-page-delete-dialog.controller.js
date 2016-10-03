(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicPageDeleteController',ComicPageDeleteController);

    ComicPageDeleteController.$inject = ['$uibModalInstance', 'entity', 'ComicPage'];

    function ComicPageDeleteController($uibModalInstance, entity, ComicPage) {
        var vm = this;

        vm.comicPage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ComicPage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
