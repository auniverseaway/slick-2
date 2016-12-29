/*global window, define, FileReader */
/**
 * Analytics, Social, and SEO Functions.
 *
 */
define(function () {
    'use strict';

    var analyticsType;

    var trackLink = function (link, socialNetwork) {
        if (analyticsType === 'adobeAnalytics') {
            var appMeasurement = window.s_c_il[0];
            if (appMeasurement) {
                appMeasurement.tl(link, 'o', socialNetwork);
            }
        } else if (analyticsType === 'googleAnalytics') {
            if (window.ga) {
                window.ga('send', {
                    hitType: 'social',
                    socialNetwork: socialNetwork,
                    socialAction: 'Share',
                    socialTarget: link
                });
            }
        }
    };

    // Share Windows
    [].forEach.call(document.querySelectorAll('.social'), function (element) {
        element.addEventListener('click', function (event) {
            event.preventDefault();
            var socialNetwork = this.dataset.socialNetwork;
            trackLink(this, socialNetwork);
            window.open(this.getAttribute('href'), socialNetwork, 'height=500,width=575');
        }, false);
    });

    analyticsType = document.body.dataset.analyticsType;

});