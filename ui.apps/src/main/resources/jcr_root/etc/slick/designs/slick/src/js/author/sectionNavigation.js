/*global window, define */
define(function () {
    'use strict';

    var allHeadings = document.querySelectorAll('.page-nav-heading');
    var allSections = document.querySelectorAll('.page-nav-section');

    /**
     * A simple forEach Utility
     * @param  {Array}    array    The array or NodeList to iterate through.
     * @param  {Function} callback The function to call on each element.
     * @param  {Scope}    scope    The scope of the objects.
     * @return {void}
     */
    var forEach = function (array, callback, scope) {
        var i;
        for (i = 0; i < array.length; i += 1) {
            callback.call(scope, array[i]);
        }
    };

    var deactivateHeading = function (element) {
        element.classList.remove('active');
    };

    var hideSection = function (element) {
        element.classList.remove('active');
    };

    var showSection = function (element) {
        element.addEventListener('click', function () {
            var dataSection = element.dataset.showSection;
            var sectionToShow = document.querySelector('#' + dataSection);
            forEach(allHeadings, deactivateHeading);
            forEach(allSections, hideSection);
            element.classList.add('active');
            sectionToShow.classList.add('active');
        }, false);
    };

    /**
     * URL Hash Detection
     *
     * <p>Detect if the window location has a hash. If it does,
     * show the appropriate section. Otherwise, show the first section</p>
     *
     * @param  {String} window.location.hash The hash of the window.
     * @return {void}
     */
    if (window.location.hash) {
        window.scrollTo(0, 0);
        var windowHash = window.location.hash;
        var sectionToShow = document.querySelector(windowHash);
        var titleToShow = document.querySelector('#title-' + windowHash.replace('#', ''));
        forEach(allHeadings, deactivateHeading);
        forEach(allSections, hideSection);
        titleToShow.classList.add('active');
        sectionToShow.classList.add('active');
    } else {
        var defaultTitleToShow = document.querySelector('.page-nav-heading:first-child');
        var defaultSectionToShow = document.querySelector('.page-nav-section:first-child');

        if (defaultSectionToShow) {
            defaultTitleToShow.classList.add('active');
            defaultSectionToShow.classList.add('active');
        }
    }

    /**
     * Find our page nav header links and iterate through them to attach click events.
     *
     */
    var pageNavHeaderLinks = document.querySelectorAll('.page-nav a');
    forEach(pageNavHeaderLinks, showSection);

});