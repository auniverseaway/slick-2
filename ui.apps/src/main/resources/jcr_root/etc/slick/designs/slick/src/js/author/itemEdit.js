/*global window, define */
define(['handlebars'], function (handlebars) {
    'use strict';

    var titleInput, slugInput, changeSlugButton;

    var formatSlug = function (string) {
        var slug = string.replace(/[^a-zA-Z0-9\-\s]/g, '')
            .replace(/^\s+|\s+$/, '')
            .replace(/\s+/g, '-')
            .toLowerCase();
        return slug;
    };

    var addTag = function (tagInput, tagSection) {
        var msg = {tagName: tagInput.value};
        var messageTemplate = document.getElementById('finished-tag').innerHTML;
        var compiledTemplate = handlebars.compile(messageTemplate);
        var finishedTag = compiledTemplate(msg);
        tagSection.insertAdjacentHTML('beforeend', finishedTag);
        tagInput.value = '';
        tagInput.focus();
    };

    // Setup Edit Page
    var itemEdit = document.querySelector('#itemEdit');
    if (itemEdit) {

        // Setup Slug and Title
        titleInput = document.querySelector('#title');
        changeSlugButton = document.querySelector('#change-slug');
        slugInput = document.querySelector('#slug-input');
        changeSlugButton.addEventListener('click', function (event) {
            event.preventDefault();
            // If we are clicking to reset the slug to the title
            if (changeSlugButton.innerHTML === 'reset') {
                slugInput.value = formatSlug(titleInput.value);
                changeSlugButton.innerHTML = 'change';
            } else {
                slugInput.focus();
                changeSlugButton.innerHTML = 'reset';
            }
        });

        // Detect the slug state. Change the button if our title and slug do not match.
        var titleSlugValue = formatSlug(titleInput.value);
        var slugValue = formatSlug(slugInput.value);
        if (titleSlugValue !== slugValue) {
            changeSlugButton.innerHTML = 'reset';
        }

        // When slug loses focus, format the value to be node friendly.
        slugInput.addEventListener('blur', function () {
            slugInput.value = formatSlug(slugInput.value);
        });

        // When title loses focus, detect the slug state and format as needed.
        titleInput.addEventListener('blur', function () {
            if (changeSlugButton.innerHTML !== 'reset') {
                slugInput.value = formatSlug(titleInput.value);
            }
        });

        //Open SEO Section on SEO button click
        var seoButton = document.querySelector('#seo-button');
        if (seoButton) {
            seoButton.addEventListener('click', function (event) {
                event.preventDefault();
                document.querySelector('#seo-section').classList.toggle('open');
            });
        }

        // Tag Events
        var tagInput = document.querySelector('#tag-input input');
        var tagSection = document.querySelector('#tag-section');
        var tagButton = document.querySelector('#tag-input a');
        if (tagInput && tagSection && tagButton) {

            // Accepts Return
            tagInput.addEventListener('keyup', function (event) {
                event.preventDefault();
                if (event.keyCode === 13 && tagInput.value !== '') {
                    addTag(tagInput, tagSection);
                }
            });

            // Button Press
            tagButton.addEventListener('click', function (event) {
                event.preventDefault();
                if (tagInput.value !== '') {
                    addTag(tagInput, tagSection);
                }
            });

            // Remove Tag
            tagSection.addEventListener('click', function (event) {
                event.preventDefault();
                if (event.target.classList.contains('remove-tag-button')) {
                    var finishedTag = event.target.parentNode.parentNode;
                    tagSection.removeChild(finishedTag);
                }
            });
        }

        // Setup Editor Text Area
        var editor = new window.wysihtml5.Editor('editor', {
            toolbar: 'wysihtml5-editor-toolbar',
            contentEditableMode: true,
            useLineBreaks: false,
            parserRules: window.wysihtml5ParserRules
        });

        // Setup Publish Date
        var dateContainer = document.querySelector('#date-container');
        if (dateContainer) {
            window.rome(window.dateInput, {appendTo: dateContainer});
        }

        // Toggle Calendar
        var calendarToggle = document.querySelector('#calendar-toggle');
        var dateInputField = document.querySelector('#dateInput');
        if (calendarToggle && dateInputField) {
            calendarToggle.addEventListener('click', function (event) {
                event.preventDefault();
                dateInputField.classList.toggle('open');
            }, false);
        }
    }

});