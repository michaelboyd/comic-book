(function() {
    'use strict';
    angular
        .module('comicBookApp')
        .factory('ComicPage', ComicPage);

    ComicPage.$inject = ['$resource'];

    function ComicPage ($resource) {
        var resourceUrl =  'api/comic-pages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
