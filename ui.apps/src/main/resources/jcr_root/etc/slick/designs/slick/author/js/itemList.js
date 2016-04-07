// Delete
[].forEach.call(document.querySelectorAll('.icon.delete'), function (element) {
    element.addEventListener('click', function(event) {
        event.preventDefault();
        var confirmDelete = element.nextElementSibling;
        confirmDelete.classList.toggle('open');
        element.classList.toggle('cancel');
    });
});

// Confirm Delete
[].forEach.call(document.querySelectorAll('.delete-confirm'), function (element) {
    element.addEventListener('click', function(event) {
        event.preventDefault();
        sendDeleteRequest(element.dataset.resourcePath, element.dataset.resourceName);
    });
});

// Send the Delete Request
function sendDeleteRequest(resourcePath, resourceName) {
	var formAction = resourcePath + ".delete.json";
    var formData = new FormData();
    formData.append("resource", resourcePath);
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
    	var msg;
    	if (xhr.readyState == 4) {
    		if(xhr.status == 200) {
    			msg = JSON.parse(xhr.responseText);
            	var deletedResource = document.querySelector("#" + resourceName);
            	deletedResource.parentNode.removeChild(deletedResource);
            } else {
            	msg = { 'responseCode': xhr.status, 'responseType':'error', 'responseMessage': 'Something went wrong' };
            }
    		sendMessage(msg);
    	}    		
    	
    };
    xhr.open("POST", formAction, true);
    xhr.send(formData);
}