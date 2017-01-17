/*global window, define */
define(['handlebars'], function (handlebars) {
    'use strict';

    var messaging = {};

    messaging.killMessage = function (message) {

        var timing = 4000;
        // Remove the class to fade back out.
        setTimeout(function () {
            message.classList.remove("show");
        }, timing);
        // Remove the message from the DOM.
        setTimeout(function () {
            message.remove();
        }, timing + 125);
    };

    messaging.sendMessage = function (msg) {

        // generate an Id based on message count.
        var messageId = 'message-' + document.querySelectorAll('#messages > .message').length;

        // Set the Message ID
        msg.messageId = messageId;

        // Grab the inline template
        var messageTemplate = document.getElementById('message').innerHTML;

        // Compile the template
        var compiledTemplate = handlebars.compile(messageTemplate);

        // Render the data into the template
        var message = compiledTemplate(msg);

        // Insert the message into the DOM
        document.getElementById('messages').insertAdjacentHTML('beforeend', message);

        var currentMessage = document.getElementById(messageId);

        messaging.killMessage(currentMessage);

    };

    return messaging;
});