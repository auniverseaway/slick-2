"use strict";

// Toggle Main Menu
document.querySelector("#logo").addEventListener("click", function(event){
    event.preventDefault();
    document.querySelector("#menu").classList.toggle("open");
});

// Share Windows
[].forEach.call(document.querySelectorAll('.social'), function (element) {
  element.addEventListener('click', function(event) {
  	event.preventDefault();
  	var socialNetwork = this.dataset.socialNetwork;
    window.open(this.getAttribute('href'), socialNetwork, 'height=500,width=575');
  }, false);
});