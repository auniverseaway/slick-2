/*global window, define */
define(['author/messaging', 'handlebars'], function (messaging, handlebars) {
    'use strict';

    var sendXhr = function (action, method, formData, callback) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                var json = JSON.parse(xhr.responseText);
                callback(json);
            }
        };
        xhr.open(method, action, true);
        if (formData === null) {
            xhr.send();
        } else {
            xhr.send(formData);
        }
    };

    var showComment = function (commentJson) {

        // Grab the inline template
        var commentTemplate = document.getElementById('comment-template').innerHTML;

        // Compile the template
        var compiledTemplate = handlebars.compile(commentTemplate);

        // Render the data into the template (as a string!?)
        var comment = compiledTemplate(commentJson);

        // Insert the message into the DOM
        document.getElementById('comments-list').insertAdjacentHTML('beforeend', comment);

    };

    var handleResponse = function (json) {
        if (json.responseType === 'success') {
            showComment(json);
            var commentForm = document.getElementById('comment-form');
            commentForm.parentElement.removeChild(commentForm);
        } else {
            var msg = {responseCode: json.responseCode, responseType: json.responseType, responseMessage: 'There was an error'};
            messaging.sendMessage(msg);
        }
    };

    var setupCommentForm = function (element) {
        console.log('comment form');
        element.addEventListener('submit', function (event) {
            event.preventDefault();
            var action = element.action;
            var method = element.method;
            var formData = new FormData(element);
            sendXhr(action, method, formData, handleResponse);
        });
    };

    var forEach = function (array, callback, scope) {
        var i;
        for (i = 0; i < array.length; i += 1) {
            callback.call(scope, array[i]);
        }
    };

    var commentForms = document.querySelectorAll('.comment-form');
    if (commentForms) {
        forEach(commentForms, setupCommentForm);
    }
});