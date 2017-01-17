/*global window, define */
define(function () {
    'use strict';

    var forEach = function (array, callback, scope) {
        var i;
        for (i = 0; i < array.length; i += 1) {
            callback.call(scope, array[i]);
        }
    };

    var setupExpandAction = function (element) {
        element.addEventListener('click', function (event) {
            event.preventDefault();

            var activeItemTarget = element.dataset.activeItem;
            var activeItem = document.getElementById(activeItemTarget);
            activeItem.classList.toggle('active');
        });
    };

    var expandItemAnchors = document.querySelectorAll('.expand-item');
    if (expandItemAnchors) {
        forEach(expandItemAnchors, setupExpandAction);
    }

});