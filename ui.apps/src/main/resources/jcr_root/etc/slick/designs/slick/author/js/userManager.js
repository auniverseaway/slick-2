var slick = slick || {};
slick.userManager = {};

(function() {
	
    this.getAuthorList = function() {
    	
    	var action = '/system/userManager/group/authors.tidy.1.json';
    	var method = 'GET';
    	var callback = parseUsers;
    	sendXhr(action, method, callback);
    	
    };
    
}).apply(slick.userManager);

slick.userManager.getAuthorList();


function sendXhr(action, method, callback) {
	
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            var json = JSON.parse(xhr.responseText);
            callback(json);
        }
    };
    xhr.open(method, action, true);
    xhr.send();
}

function parseUsers(usersJson) {
	usersJson.members.forEach(getUserDetails);
}


function getUserDetails(element, index, array) {
	var userId = element.substring(element.lastIndexOf("/") + 1);
	var action = '/system/userManager/user/' + userId + '.tidy.1.json';
	var method = 'GET';
	var callback = parseUserDetails;
	sendXhr(action, method, callback);
}

function parseUserDetails(user) {
	showUser(user);
}

function showUser(user) {
	
	console.log(user);
	
	// Grab the inline template
	var messageTemplate = document.getElementById('user-list-item').innerHTML;
	console.log(messageTemplate);

	// Compile the template
	var compiledTemplate = Handlebars.compile(messageTemplate);
	console.log(compiledTemplate);

	// Render the data into the template (as a string!?)
	var message = compiledTemplate(user);
	
	// Insert the message into the DOM
	document.getElementById('users').insertAdjacentHTML('beforeend', message);
}

var newUserForm = document.querySelector("#create-user");
newUserForm.addEventListener("submit", function(event){
	
    event.preventDefault();
    var formAction = this.action;
    var formData = new FormData(this);
        
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var response = JSON.parse(xhr.responseText);
            
            var msg = { 'responseCode': 200, 'responseType':'success', 'responseMessage': 'User successfully created'};
            sendMessage(msg);
            //console.log(msg);
        }
    };
    // Open the request to the token endpoint and send the GET
    xhr.open("POST", formAction, true);
    xhr.send(formData);

});