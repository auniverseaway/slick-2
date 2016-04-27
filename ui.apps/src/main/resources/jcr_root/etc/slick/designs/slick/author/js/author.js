/**
 * Author
 *
 * All functions to author an item.
 */

var slick = slick || {};
slick.author = {};

(function() {
	
	var slugInput, pureSlugInput, changeSlugButton;
	
	(function setupSlug() {
		titleInput = document.querySelector('#title');
		changeSlugButton = document.querySelector('#change-slug');
		slugInput = document.querySelector('#slug-input');
		pureSlugInput = document.querySelector('#pure-slug-input');
		
		changeSlugButton.addEventListener('click', function(event){
            event.preventDefault();
            
            // If we are clicking to reset the slug to the title
            if(changeSlugButton.innerHTML == 'reset') {
            	slugInput.value = formatSlug(titleInput.value);
            	slugInput.disabled = true;
            	changeSlugButton.innerHTML = 'change';
            } else {
            	slugInput.disabled = false;
            	slugInput.focus();
            	changeSlugButton.innerHTML = 'reset';
            }     
        });
    }());
	
	// Detect the slug state. Change the button if our title and slug do not match.
	(function detectSlugState() {
		var titleSlugValue = formatSlug(titleInput.value);
		var slugValue = formatSlug(slugInput.value);
		
		if(titleSlugValue != slugValue) {
			changeSlugButton.innerHTML = 'reset';
		}		
    }());
	
	
	// When slug loses focus, format the value to be node friendly.
	(function slugBlur() {
		slugInput.addEventListener('blur', function(event){
			slugInput.value = formatSlug(slugInput.value);
		});
    }());
	
	// When title loses focus, detect the slug state and format as needed.
	(function titleBlur() {
		titleInput.addEventListener('blur', function(event){
			if(changeSlugButton.innerHTML != 'reset') {
				slugInput.value = formatSlug(titleInput.value);
			}	
		});
    }());
	
	// A simple string formatter for the slug
	function formatSlug(string){
		var slug = string.replace(/[^a-zA-Z0-9-\s]/g, '') 	// Remove non alphanum except whitespace
		.replace(/^\s+|\s+$/, '')      				    // Remove leading and trailing whitespace
		.replace(/\s+/g, '-')          				    // Replace (multiple) whitespaces with a dash
		.toLowerCase();
		return slug;
	}
    
}).apply(slick.author);

//TODO: Need to refactor
//Open SEO Section on SEO button click
$("#seo-button").on( "click", function() {
	$("#seo-section").toggleClass("open");
	return false;
});


// Add a tag when tag is input
//// On Return
$('#tag-input input').keypress(function (e) {
  if (e.which == 13) {
    addTag();
    return false;    //<---- Add this line
  }
});
//// On Button Press
$("#tag-input a").on( "click", function() {
	addTag();
	return false;
});

function addTag() {
	var tagInput = $("#tag-input input");
	var tagName = tagInput.val();
	
	if (tagName != "") {
		$("#tag-section").append('<div class="finished-tag"><label>' + tagName + '<a href="#">Remove</a></label><input name="tags" type="text" value="' + tagName + '"></div>');
		tagInput.val("");
	}
	tagInput.focus();
}

// Tag Removal
$("#tag-section").on('click', '.finished-tag a', function(){
	$(this).parent().parent().remove();
	return false;
});
