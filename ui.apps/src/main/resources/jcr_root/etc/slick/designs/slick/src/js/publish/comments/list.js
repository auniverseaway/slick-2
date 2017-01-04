/*global window, define */
define(['handlebars'], function (handlebars) {
    'use strict';

    var sendXhr = function (action, method, formData, callback, commentsSection, getCommentsCount, getCommentsList) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                var json = JSON.parse(xhr.responseText);
                callback(json, commentsSection, getCommentsCount, getCommentsList);
            }
        };
        xhr.open(method, action, true);
        if (formData === null) {
            xhr.send();
        } else {
            xhr.send(formData);
        }
    };

    var forEach = function (array, callback, scope) {
        var i;
        for (i = 0; i < array.length; i += 1) {
            callback.call(scope, array[i]);
        }
    };

    var showCommentsCount = function (commentsSection, commentsContent) {
        var countTemplate = document.getElementById('comment-count-template').innerHTML;
        var compiledTemplate = handlebars.compile(countTemplate);
        var content = compiledTemplate(commentsContent);
        commentsSection.querySelector('.comment-count').insertAdjacentHTML('beforeend', content);
    };

    var showCommentsList = function (commentsSection, commentsContent) {
        var listTemplate = document.getElementById('comment-list-template').innerHTML;
        var compiledTemplate = handlebars.compile(listTemplate);
        var content = compiledTemplate(commentsContent);
        commentsSection.querySelector('.comments-list').insertAdjacentHTML('beforeend', content);
    };

    var consumeComments = function (json, commentsSection, getCommentsCount, getCommentsList) {
        if (getCommentsCount) {
            showCommentsCount(commentsSection, json.content);
        }
        if (getCommentsList) {
            showCommentsList(commentsSection, json.content);
        }
    };

    var setupCommentsSection = function (element) {
        var getCommentsCount = element.dataset.commentsCount;
        var getCommentsList = element.dataset.commentsList;
        var resourcePath = element.dataset.resourcePath;
        if (getCommentsCount || getCommentsList) {
            var commentsPath = resourcePath + '.list.comments.json';
            sendXhr(commentsPath, 'GET', null, consumeComments, element, getCommentsCount, getCommentsList);
        }
    };

    var commentsSection = document.querySelectorAll('.comments-section');
    if (commentsSection) {
        forEach(commentsSection, setupCommentsSection);
    }
});