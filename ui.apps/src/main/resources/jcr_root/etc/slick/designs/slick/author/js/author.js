// Edit Item
// On blur format the title to something better.
$("input:text[name=title]").blur(function() {
	var str = $(this).val();
	var slug = str.replace(/[^a-zA-Z0-9-\s]/g, '') 	// Remove non alphanum except whitespace
		.replace(/^\s+|\s+$/, '')      				// Remove leading and trailing whitespace
		.replace(/\s+/g, '-')          				// Replace (multiple) whitespaces with a dash
		.toLowerCase();
	$("input:text[name='nodeName']").val(slug);
});

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