(function() {
    'use strict';

    angular
        .module('comicBookApp')
        .controller('ComicPageDialogController', ComicPageDialogController);

    ComicPageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ComicPage', 'ComicBook'];

    function ComicPageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ComicPage, ComicBook) {
        var vm = this;

        vm.comicPage = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.comicbooks = ComicBook.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comicPage.id !== null) {
                ComicPage.update(vm.comicPage, onSaveSuccess, onSaveError);
            } else {
                ComicPage.save(vm.comicPage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('comicBookApp:comicPageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImageData = function ($file, comicPage) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        comicPage.imageData = base64Data;
                        comicPage.imageDataContentType = $file.type;
                    });
                });
            }
        };

    }
})();
