/*global window, define */
define(['author/messaging'], function (messaging) {
    'use strict';

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

    // Send the Delete Request
    var sendDeleteRequest = function (resourcePath, resourceName) {
        var formAction = resourcePath + '.delete.json';
        var formData = new FormData();
        formData.append('resource', resourcePath);
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            var msg;
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    msg = JSON.parse(xhr.responseText);
                    var deletedResource = document.querySelector('#' + resourceName);
                    deletedResource.parentNode.removeChild(deletedResource);
                } else {
                    msg = {responseCode: 'xhr.status', responseType: 'error', responseMessage: 'Something went wrong'};
                }
                messaging.sendMessage(msg);
            }
        };
        xhr.open('POST', formAction, true);
        xhr.send(formData);
    };

    var setDeleteConfirmClick = function (element) {
        element.addEventListener('click', function (event) {
            event.preventDefault();
            sendDeleteRequest(element.dataset.resourcePath, element.dataset.resourceName);
        });
    };

    var setDeleteClick = function (element) {
        element.addEventListener('click', function (event) {
            event.preventDefault();
            var confirmDelete = element.nextElementSibling;
            confirmDelete.classList.toggle('open');
            element.classList.toggle('cancel');
        });
    };

    // Delete
    var deleteIcons = document.querySelectorAll('.icon.delete');
    if (deleteIcons) {
        forEach(deleteIcons, setDeleteClick);
    }

    var deleteConfirmIcons = document.querySelectorAll('.delete-confirm');
    if (deleteConfirmIcons) {
        forEach(deleteConfirmIcons, setDeleteConfirmClick);
    }

});