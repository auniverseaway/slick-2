define(function () {
    'use strict';

    // Lightbox - Show
    [].forEach.call(document.querySelectorAll('article .content img'), function (element) {
      element.addEventListener('click', function(event) {
        event.preventDefault();
        document.querySelector('#lightbox img').src = this.src;
        document.querySelector('#lightbox').classList.toggle('open');
      }, false);
    });

    // Lightbox - Hide
    var lightbox = document.querySelector('#lightbox, #lightbox img')
    if(lightbox) {
        lightbox.addEventListener('click', function(event){
            event.preventDefault();
            document.querySelector('#lightbox').classList.toggle('open');
        });
    }
});