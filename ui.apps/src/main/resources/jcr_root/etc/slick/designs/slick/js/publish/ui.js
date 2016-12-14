define(function () {
    'use strict';
    // Toggle Main Menu
    document.querySelector('#logo').addEventListener('click', function(event){
        event.preventDefault();
        document.querySelector('#menu').classList.toggle('open');
    });
});