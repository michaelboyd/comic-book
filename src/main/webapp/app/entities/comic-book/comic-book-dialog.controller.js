(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicBookDialogController', ComicBookDialogController);

    ComicBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ComicBook', 'ComicPage'];

    function ComicBookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ComicBook, ComicPage) {
        var vm = this;

        vm.comicBook = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.comicpages = ComicPage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comicBook.id !== null) {
                ComicBook.update(vm.comicBook, onSaveSuccess, onSaveError);
            } else {
                ComicBook.save(vm.comicBook, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('comicBookApp:comicBookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        
        //default createDate to current date
        vm.comicBook.createDate = new Date();

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
