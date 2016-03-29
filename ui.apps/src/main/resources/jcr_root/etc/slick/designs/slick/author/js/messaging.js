function sendMessage(msg) {
	
	// Count how many messages are currently being displayed
	var messageCount = document.querySelectorAll('#messages > .message').length;
	
	// generate an Id based on message count.
	var messageId = 'message-' + document.querySelectorAll('#messages > .message').length;
	
	// Set the Message ID
	msg.messageId = messageId;
	
	// Grab the inline template
	var messageTemplate = document.getElementById('message').innerHTML;

	// Compile the template
	var compiledTemplate = Handlebars.compile(messageTemplate);

	// Render the data into the template (as a string!?)
	var message = compiledTemplate(msg);
	
	// Insert the message into the DOM
	document.getElementById('messages').insertAdjacentHTML('beforeend', message);
	
	var currentMessage = document.getElementById(messageId);
	
	killMessage(currentMessage);
	
}

function killMessage(message) {
	
	var timing = 4000;
	// Remove the class to fade back out.
	setTimeout(function() {
		message.classList.remove("show");
	 }, timing);
	// Remove the message from the DOM.
	setTimeout(function() {
		message.remove();
	 }, timing + 125);
}