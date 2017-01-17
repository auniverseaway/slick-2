/*global window, define */
/**
 * Analytics, Social, and SEO Functions.
 *
 * <p>This module will handle new windows for social sharing.</p>
 * <p>If analytics are enabled, send the appropriate events to those services</p>
 */
define(function () {
    'use strict';

    var analyticsType = document.body.dataset.analyticsType;

    /**
     * Send social information to analytics services.
     * @param  {String} shareUri      The URL we're sharing.
     * @param  {String} socialNetwork The social network.
     * @return {void}
     */
    var trackLink = function (shareUri, socialNetwork) {
        if (analyticsType === 'adobeAnalytics') {
            var s = window.s_c_il[0];
            if (s) {
                s.tl(shareUri, 'o', socialNetwork, {linkTrackVars: 'prop1', prop1: s.pageName});
            }
        } else if (analyticsType === 'googleAnalytics') {
            if (window.ga) {
                window.ga('send', 'social', socialNetwork, 'Share', shareUri);
            }
        }
    };

    var forEach = function (array, callback, scope) {
        var i;
        for (i = 0; i < array.length; i += 1) {
            callback.call(scope, array[i]);
        }
    };

    var handleClick = function (element) {
        element.addEventListener('click', function (event) {
            event.preventDefault();
            var socialNetwork = this.dataset.socialNetwork;
            var shareUri = this.dataset.shareUri;
            trackLink(shareUri, socialNetwork);
            window.open(this.getAttribute('href'), socialNetwork, 'height=500,width=575');
        }, false);
    };

    // Share Windows
    var socialLinks = document.querySelectorAll('.social');
    if (socialLinks) {
        forEach(socialLinks, handleClick);
    }

});