[].forEach.call(document.querySelectorAll('.cache-form'), function (element) {
  element.addEventListener('submit', function(event) {

    event.preventDefault();
    var formAction = element.action;
    var formData = new FormData(element);
        
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            var msg = JSON.parse(xhr.responseText);
            console.log(msg);
        }
    };
    // Open the request to the token endpoint and send the GET
    xhr.open("POST", formAction, true);
    xhr.send(formData);

  }, false);
});