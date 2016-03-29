getFullUserList();

function getFullUserList() {
	
	var formAction = '/system/userManager/user.tidy.1.json';
	
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            var userList = JSON.parse(xhr.responseText);
            showFullUserList(userList);
        }
    };
    xhr.open('GET', formAction, true);
    xhr.send();
}

function showFullUserList(userList) {
	
	console.log(userList);
	
	// Grab the inline template
	var messageTemplate = document.getElementById('user-list').innerHTML;
	console.log(messageTemplate);

	// Compile the template
	var compiledTemplate = Handlebars.compile(messageTemplate);
	console.log(compiledTemplate);

	// Render the data into the template (as a string!?)
	var message = compiledTemplate(userList);
	
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