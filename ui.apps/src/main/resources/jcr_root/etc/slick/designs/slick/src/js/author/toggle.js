/*global window, define */
define(function () {
    'use strict';

    var toggleValue;

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

    var showElements = function (element) {
        var targetValue = element.dataset.toggleItemValue;
        if (targetValue === toggleValue) {
            element.classList.add('active');
        } else {
            element.classList.remove('active');
        }
    };

    var setToggle = function (element) {
        toggleValue = element.value;
        var toggleElements = document.querySelectorAll('.toggle-enabled-item');
        forEach(toggleElements, showElements);
    };

    var getToggle = function (element) {
        setToggle(element);
        element.addEventListener('change', function () {
            setToggle(element);
        });
    };

    var toggles = document.querySelectorAll('select.toggle-enabled');
    forEach(toggles, getToggle);
});