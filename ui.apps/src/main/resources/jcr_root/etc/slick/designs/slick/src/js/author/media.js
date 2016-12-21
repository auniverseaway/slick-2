/*global window, define, FileReader */
define(['author/messaging', 'handlebars'], function (messaging, handlebars) {
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

    var showMedia = function (content) {
        var mediaCount = document.querySelectorAll('#media-list > .media-box').length;

        content.mediaId = mediaCount;

        // Grab the inline template
        var mediaTemplate = document.getElementById('media-list-item').innerHTML;

        // Compile the template
        var compiledTemplate = handlebars.compile(mediaTemplate);

        // Render the data into the template (as a string!?)
        var message = compiledTemplate(content);

        // Insert the message into the DOM
        document.getElementById('upload-form').insertAdjacentHTML('afterend', message);
    };

    var toggleUploadButton = function () {
        var button = document.getElementById('submit-media-button');
        button.classList.toggle('open');
    };

    var clearUploadLabel = function () {
        // Get our Label
        var mediaLabel = document.getElementById('media-upload-label');

        // See if we have any style tags in place
        var styleTag = mediaLabel.getElementsByTagName('style')[0];

        // Remove if we have style tags
        if (styleTag) {
            mediaLabel.removeChild(styleTag);
        }
    };

    /**
     * Add Media
     *
     * Adds media to the view.
     * @param {object} json - The object returned from the ajax
     */
    function addMedia(json) {
        messaging.sendMessage(json);
        showMedia(json.content);
        clearUploadLabel(document.querySelector('#media-upload'));
        toggleUploadButton();
    }

    var mediaForm = document.querySelector('#upload-form');
    if (mediaForm) {
        mediaForm.addEventListener('submit', function (event) {
            event.preventDefault();
            var action = this.action;
            var method = this.method;
            var formData = new FormData(this);
            var callback = addMedia;
            sendXhr(action, method, formData, callback);
        });
    }

    var mediaUploadInputs = document.querySelectorAll('#media-upload');
    if (mediaUploadInputs) {
        Array.prototype.forEach.call(mediaUploadInputs, function (input) {
            input.addEventListener('change', function () {
                if (input.files && input.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {

                        // Show the upload button
                        toggleUploadButton();

                        // Get our preview data
                        var mediaPreview = {mediaUrl: e.target.result};

                        // Grab the inline template
                        var mediaTemplate = document.getElementById('media-preview').innerHTML;

                        // Compile the template
                        var compiledTemplate = handlebars.compile(mediaTemplate);

                        // Render the data into the template (as a string!?)
                        var mediaPreviewTemplate = compiledTemplate(mediaPreview);

                        clearUploadLabel();

                        var mediaLabel = document.getElementById('media-upload-label');

                        // Insert the template
                        mediaLabel.insertAdjacentHTML('afterbegin', mediaPreviewTemplate);

                    };
                    reader.readAsDataURL(input.files[0]);
                }
            });
        });
    }
});