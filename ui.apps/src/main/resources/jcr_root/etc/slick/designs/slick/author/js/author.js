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