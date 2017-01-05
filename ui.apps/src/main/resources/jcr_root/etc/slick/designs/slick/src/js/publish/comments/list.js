/*global window, define */
define(['handlebars'], function (handlebars) {
    'use strict';

    var sendXhr = function (action, method, formData, callback, article, getCommentsCount, getCommentsList) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                var json = JSON.parse(xhr.responseText);
                callback(json, article, getCommentsCount, getCommentsList);
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

    handlebars.registerHelper('compare', function (lvalue, rvalue, options) {

        var operator = options.hash.operator || '===';
        var operators = {
            '===': function (l, r) {
                return l === r;
            },
            '!==': function (l, r) {
                return l !== r;
            },
            '<': function (l, r) {
                return l < r;
            },
            '>': function (l, r) {
                return l > r;
            },
            '<=': function (l, r) {
                return l <= r;
            },
            '>=': function (l, r) {
                return l >= r;
            }
        };

        if (!operators[operator]) {
            throw new Error('Handlerbars Helper compare doesn\'t know the operator ' + operator);
        }

        var result = operators[operator](lvalue, rvalue);

        if (result) {
            return options.fn(this);
        } else {
            return options.inverse(this);
        }

    });

    var showCommentsCount = function (article, commentsContent) {
        var countTemplate = document.getElementById('comment-count-template').innerHTML;
        var compiledTemplate = handlebars.compile(countTemplate);
        var content = compiledTemplate(commentsContent);
        article.querySelector('.comment-count').insertAdjacentHTML('beforeend', content);
    };

    var showCommentsList = function (article, commentsContent) {
        var listTemplate = document.getElementById('comment-list-template').innerHTML;
        var compiledTemplate = handlebars.compile(listTemplate);
        var content = compiledTemplate(commentsContent);
        article.querySelector('.comments-list').insertAdjacentHTML('beforeend', content);
    };

    var consumeComments = function (json, article, getCommentsCount, getCommentsList) {
        if (getCommentsCount === true) {
            showCommentsCount(article, json.content);
        }
        if (getCommentsList === true) {
            showCommentsList(article, json.content);
        }
    };

    var setupCommentsSection = function (article) {
        var getCommentsCount = (article.dataset.commentsCount === 'true');
        var getCommentsList = (article.dataset.commentsList === 'true');
        var resourcePath = article.dataset.resourcePath;
        if (getCommentsCount || getCommentsList) {
            var commentsPath = resourcePath + '.list.comments.json';
            sendXhr(commentsPath, 'GET', null, consumeComments, article, getCommentsCount, getCommentsList);
        }
    };

    var articles = document.querySelectorAll('article');
    if (articles) {
        forEach(articles, setupCommentsSection);
    }
});