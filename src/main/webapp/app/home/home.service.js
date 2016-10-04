(function() {
    'use strict';
    angular
        .module('comicBookApp')
        .factory('Home', Home);

    Home.$inject = ['$resource', 'DateUtils'];

    function ComicBook ($resource, DateUtils) {
        var resourceUrl =  'api/comic-book-cover/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertLocalDateFromServer(data.createDate);
                    }
                    return data;
                }
            }
        });
    }
})();
