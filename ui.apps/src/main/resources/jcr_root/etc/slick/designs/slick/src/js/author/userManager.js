/*global window, define */
define(['handlebars', 'author/messaging'], function (handlebars, messaging) {
    'use strict';

    var showUser = function (user) {
        var messageTemplate = document.getElementById('user-list-item').innerHTML;
        var compiledTemplate = handlebars.compile(messageTemplate);
        var message = compiledTemplate(user);
        document.getElementById('users').insertAdjacentHTML('beforeend', message);
    };

    var parseUserDetails = function (user, userId) {
        user.userId = userId;
        showUser(user);
    };

    var confirmAuthor = function () {
        var msg = {responseCode: 200, responseType: 'success', responseMessage: 'User successfully created'};
        messaging.sendMessage(msg);
    };

    /**
     * Send all AJAX Requests
     */
    var sendXhr = function (action, method, formData, callback, userId) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                var json = JSON.parse(xhr.responseText);
                callback(json, userId);
            }
        };
        xhr.open(method, action, true);
        if (formData === null) {
            xhr.send();
        } else {
            xhr.send(formData);
        }
    };

    var getIdFromPath = function (userPath) {
        return userPath.substring(userPath.lastIndexOf("/") + 1);
    };

    /**
     * Get the user details
     */
    var getUserDetails = function (element) {
        var userId = getIdFromPath(element);
        var action = '/system/userManager/user/' + userId + '.tidy.1.json';
        var method = 'GET';
        var callback = parseUserDetails;
        sendXhr(action, method, null, callback, userId);
    };

    /**
     * Handle Each User
     */
    var parseUsers = function (usersJson) {
        usersJson.members.forEach(getUserDetails);
    };

    var addAuthor = function (userJson) {

        getUserDetails(userJson.path);

        var userId = getIdFromPath(userJson.path);
        var action = '/system/userManager/group/authors.update.json';
        var method = 'POST';
        var formData = new FormData();
        formData.append(':member', userId);
        var callback = confirmAuthor;

        sendXhr(action, method, formData, callback);
    };

    var newUserForm = document.querySelector('#create-user');
    if (newUserForm) {
        newUserForm.addEventListener('submit', function (event) {
            event.preventDefault();
            var action = this.action;
            var method = 'POST';
            var formData = new FormData(this);
            var callback = addAuthor;
            sendXhr(action, method, formData, callback);
        });
    }

    var bodyId = document.querySelector('#userEdit');
    if (bodyId) {
        var action = '/system/userManager/group/authors.tidy.1.json';
        var method = 'GET';
        var callback = parseUsers;
        sendXhr(action, method, null, callback);
    }

});