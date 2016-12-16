define(function () {
    'use strict';

    var hideSection = function (index, element) {
        element.style.display = 'none';
    };

    var showSection = function (index, element) {
        element.addEventListener('click', function(event) {
            event.preventDefault();
            var showSection = element.dataset.showSection;
            var sectionToShow = document.querySelector('#' + showSection);
            var allSections = document.querySelectorAll('.page-nav-section');
            forEach(allSections,hideSection);
            sectionToShow.style.display = 'block';
        }, false);
    };

    /**
     * A simple forEach Utility
     * @param  {Array}    array    The array or NodeList to iterate through.
     * @param  {Function} callback The function to call on each element.
     * @param  {Scope}    scope    The scope of the objects.
     * @return {void}
     */
    var forEach = function (array, callback, scope) {
      for (var i = 0; i < array.length; i++) {
        callback.call(scope, i, array[i]); // passes back stuff we need
      }
    };

    /**
     * Find our page nav header links and iterate through them.
     *
     */
    var pageNavHeaderLinks = document.querySelectorAll('.page-nav a');
    forEach(pageNavHeaderLinks,showSection);

});