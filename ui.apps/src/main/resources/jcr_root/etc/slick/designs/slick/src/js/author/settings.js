/*global window, define */
define(['author/messaging'], function (messaging) {
    'use strict';

    var handleSubmit = function (element) {
        element.addEventListener('submit', function (event) {

            event.preventDefault();
            var formAction = element.action;
            var formData = new FormData(element);

            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    var msg = JSON.parse(xhr.responseText);
                    messaging.sendMessage(msg);
                    console.log('new hotness');
                }
            };
            xhr.open('POST', formAction, true);
            xhr.send(formData);

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
        var i;
        for (i = 0; i < array.length; i += 1) {
            callback.call(scope, array[i]);
        }
    };

    var ajaxForms = document.querySelectorAll('.ajax-form');
    if (ajaxForms) {
        forEach(ajaxForms, handleSubmit);
    }

});