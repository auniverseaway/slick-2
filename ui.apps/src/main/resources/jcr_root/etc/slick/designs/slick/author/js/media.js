/**
 * mediaManager
 *
 * All functions to manage media.
 */

var slick = slick || {};
slick.mediaManager = {};

(function() {
    
    (function uploadMediaEvent(){
        var mediaForm = document.querySelector("#upload-form");
        mediaForm.addEventListener("submit", function(event){
            
            event.preventDefault();
            var action = this.action;
            var method = this.method;
            var formData = new FormData(this);
            var callback = addMedia;

            sendXhr(action, method, formData, callback);

        });
    }());
    
    /**
     * Send all AJAX Requests
     * @param {string} action - The location of the request.
     * @param {string} method - The method of the request.
     * @param {object} formData - The form contents.
     * @param {function} callback - The function to call when receiving a response.
     */
    function sendXhr(action, method, formData, callback) {
    
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                var json = JSON.parse(xhr.responseText);
                callback(json);
            }
        };
        xhr.open(method, action, true);
        if (formData == null){
            xhr.send();
        } else {
            xhr.send(formData);
        }
    }
    
    /**
     * Add Media
     * 
     * Adds media to the view.
     * @param {object} json - The object returned from the ajax
     */
    function addMedia(json) {
        sendMessage(json);
        showMedia(json.content);
        clearUploadLabel(document.querySelector('#media-upload'));
        toggleUploadButton();
    }
    
    function showMedia(content) {
        
        var mediaCount = document.querySelectorAll('#media-list > .media-box').length;
        
        content.mediaId = mediaCount;

        // Grab the inline template
        var mediaTemplate = document.getElementById('media-list-item').innerHTML;
        
        // Compile the template
        var compiledTemplate = Handlebars.compile(mediaTemplate);
        
        // Render the data into the template (as a string!?)
        var message = compiledTemplate(content);
        
        // Insert the message into the DOM
        document.getElementById('upload-form').insertAdjacentHTML('afterend', message);
    }
    
    /**
     * Preview the media to upload
     */
    (function previewMedia() {
        
        var inputs = document.querySelectorAll('#media-upload');
        Array.prototype.forEach.call(inputs, function (input)
        {
            input.addEventListener('change', function (e)
            {
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
                        var compiledTemplate = Handlebars.compile(mediaTemplate);
                        
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
        
    }());
    
    function toggleUploadButton() {
    	var button = document.getElementById('submit-media-button');
        button.classList.toggle('open');
    }
    
    function clearUploadLabel() {
    	// Get our Label
        var mediaLabel = document.getElementById('media-upload-label');
        
        // See if we have any style tags in place
        var styleTag = mediaLabel.getElementsByTagName('style')[0];
        
        // Remove if we have style tags
        if(styleTag) {
        	mediaLabel.removeChild(styleTag);
        }
    }
    
    function clearInputFile(f){
        if(f.value){
            try{
                f.value = ''; //for IE11, latest Chrome/Firefox/Opera...
            }catch(err){ }
            if(f.value){ //for IE5 ~ IE10
                var form = document.createElement('form'),
                    parentNode = f.parentNode, ref = f.nextSibling;
                form.appendChild(f);
                form.reset();
                parentNode.insertBefore(f,ref);
            }
        }
    }
    
    
}).apply(slick.mediaManager);