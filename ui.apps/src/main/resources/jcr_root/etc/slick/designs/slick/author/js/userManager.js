/**
 * userManager
 *
 * All functions to manage users.
 */

var slick = slick || {};
slick.userManager = {};

(function() {

    /**
     * Get the list of authors
     */
    (function getAuthorList() {

        var action = '/system/userManager/group/authors.tidy.1.json';
        var method = 'GET';
        var callback = parseUsers;
        sendXhr(action, method, null, callback);

    }());

    /**
     * Send all AJAX Requests
     */
    function sendXhr(action, method, formData, callback, userId) {
    
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                var json = JSON.parse(xhr.responseText);
                callback(json, userId);
            }
        };
        xhr.open(method, action, true);
        
        if (formData == null){
            xhr.send();
        } else {
            xhr.send(formData);
        }
    };

    /**
     * Handle Each User
     */
    function parseUsers(usersJson) {
        usersJson.members.forEach(getUserDetails);
    };

    /**
     * Get the user details
     */
    function getUserDetails(element, index, array) {
        var userId = getIdFromPath(element);

        var action = '/system/userManager/user/' + userId + '.tidy.1.json';
        var method = 'GET';
        var callback = parseUserDetails;
        sendXhr(action, method, null, callback, userId);
    };

    function getIdFromPath(userPath) {
        return userPath.substring(userPath.lastIndexOf("/") + 1);
    };

    function parseUserDetails(user, userId) {
        user['userId'] = userId;
        showUser(user);
    };

    function showUser(user) {

        // Grab the inline template
        var messageTemplate = document.getElementById('user-list-item').innerHTML;
        
        // Compile the template
        var compiledTemplate = Handlebars.compile(messageTemplate);
        
        // Render the data into the template (as a string!?)
        var message = compiledTemplate(user);
        
        // Insert the message into the DOM
        document.getElementById('users').insertAdjacentHTML('beforeend', message);
    };

    function addAuthor(userJson) {
    
        getUserDetails(userJson.path);
        
        var userId = getIdFromPath(userJson.path);
        var action = '/system/userManager/group/authors.update.json';
        var method = 'POST';
        var formData = new FormData();
        formData.append(':member', userId);
        var callback = confirmAuthor;
        
        sendXhr(action, method, formData, callback);
    }

    function confirmAuthor(json) {
        var msg = { 'responseCode': 200, 'responseType':'success', 'responseMessage': 'User successfully created' };
        sendMessage(msg);
    }
    
    (function createUserEvent(){
        var newUserForm = document.querySelector("#create-user");
        newUserForm.addEventListener("submit", function(event){
            
            event.preventDefault();
            var action = this.action;
            var method = 'POST';
            var formData = new FormData(this);
            var callback = addAuthor;

            sendXhr(action, method, formData, callback);

        });
    }());

    
}).apply(slick.userManager);