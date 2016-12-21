/*global window, define */
define(['author/messaging'], function (messaging) {
    'use strict';

    /**
     * Send all AJAX Requests
     */
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

    var handleGroupChange = function (json) {
        console.log(json);
        window.location = "/login.html?resource=%2Fauthor.html";
    };

    var addToGroup = function (username) {
        var action = '/system/userManager/group/authors.update.json';
        var data = new FormData();
        data.append(':member', username);
        sendXhr(action, 'POST', data, handleGroupChange);
    };

    var handleAdminChange = function (json) {
        console.log(json);
        var msg = {responseCode: 200, responseType: 'success', responseMessage: 'User successfully updated'};
        messaging.sendMessage(msg);
    };

    var handleUserCreate = function (json) {
        if (json['status.code'] === 200) {
            var username = document.querySelector('#username').value;
            var msg = {responseCode: 200, responseType: 'success', responseMessage: username + ' successfully updated'};
            messaging.sendMessage(msg);
            var groupMsg = {responseCode: 200, responseType: 'info', responseMessage: 'Adding ' + username + ' to author group'};
            messaging.sendMessage(groupMsg);
            setTimeout(function () {
                addToGroup(username);
            }, 4400);
        }
    };

    var changeAdminPasswordForm = document.querySelector('#change-admin-password');
    if (changeAdminPasswordForm) {
        changeAdminPasswordForm.addEventListener('submit', function (event) {
            event.preventDefault();
            var action = changeAdminPasswordForm.action;
            var data = new FormData(changeAdminPasswordForm);
            sendXhr(action, 'POST', data, handleAdminChange);
        });
    }

    var createNewUserForm = document.querySelector('#create-new-user');
    if (createNewUserForm) {
        createNewUserForm.addEventListener('submit', function (event) {
            event.preventDefault();
            var action = createNewUserForm.action;
            var data = new FormData(createNewUserForm);
            sendXhr(action, 'POST', data, handleUserCreate);
        });
    }

});