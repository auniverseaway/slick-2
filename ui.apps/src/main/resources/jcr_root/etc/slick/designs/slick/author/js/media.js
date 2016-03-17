var inputs = document.querySelectorAll('#media-upload');
Array.prototype.forEach.call(inputs, function(input)
{
    input.addEventListener('change', function(e)
    {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var preview = document.getElementById('image-preview');
                var button = document.getElementById('submit-media-button');
                preview.setAttribute('src', e.target.result);
                preview.className = 'open';
                button.className += ' open';
            };
            reader.readAsDataURL(input.files[0]);
        }
    });
});