$("#change-admin-password").submit(function(event) {
    // Stop the presses
    event.preventDefault();
    
    // What's our form?
    var form = $(this);
    
    // Where's our form going?
    url = form.attr("action");
    
    $.ajax({
        type: "POST",
        url: url,
        data: form.serialize(),
        statusCode: {
            500: function() {
                sendMessage("message failure","User could not be changed");
            }
        },
        success: function(message) {
            sendMessage("message success","User Changed");
        }
        
    });
    
    form.addClass("collapse");
});

$("#create-new-user").submit(function(event) {
    // Stop the presses
    event.preventDefault();
    
    // What's our form?
    var form = $(this);
    
    // Where's our form going?
    var url = form.attr("action");
    
    // Serialize our data
    var data = form.serialize();
    
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        statusCode: {
            500: function() {
                sendMessage("message failure","User could not be created");
            }
        },
        success: function(message) {
            sendMessage("message success","User Changed");
        }
        
    });
    
    // Get the new users name
    var username = document.getElementById("username").value;
    
    sendMessage("message info","Adding user to group...");
    
    setTimeout(function() {
        addToGroup(username);
        window.location = "/login.html?resource=%2Fauthor.html";
    }, 4400);
    
});

function addToGroup(username) {
    $.ajax({
        type: "POST",
        url: "/system/userManager/group/authors.update.html",
        data: "%3Amember=" + username,
        statusCode: {
            500: function() {
                sendMessage("message failure","User could not be added to group");
            }
        },
        success: function(message) {
            sendMessage("message success","User added to authors group");
        }
    });
}

function sendMessage(style, message) {
    // Make unique id
    var messageId = "message-" + $("#messages > div").length;
    // Append the message to our messages section
    $("#messages").append("<div class='" + style + "' id='" + messageId +"'>" + message +"</div>").hide().show(0);
    // Pull the message into an object
    message = $("#" + messageId);
    // Show the message
    message.addClass("show");
    // Kill the message
    killMessage(message);
}

function killMessage(message) {
    var timing = 4000;
    // Remove the class to fade back out.
    setTimeout(function() {
        message.removeClass("show");
     }, timing);
    // Remove the message from the DOM.
    setTimeout(function() {
        message.remove();
     }, timing + 125);
}