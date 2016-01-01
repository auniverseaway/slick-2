About
=====

For Nerds
---------
Slick is a blogging engine built on top of Sling and Sightly. The idea was to create a lightweight blogging engine using technologies common to AEM. It uses Sling Models heavily.

For Humans
----------
Slick is a beautiful app to help create exceptional web content. It's highly optimized for blogging.

Demo
====
[slick.millr.org](http://slick.millr.org)

Requirements
============
* Java 8
* Apache Sling 8
* Maven 3.2+

Features
========
* Creating and editing posts, pages, and media
* WYSIWYG Editor
* RSS 2.0 Feed
* Authentication
* Dispatcher
* Analytics
* Pagination

Planned
=======
* Tagging of posts
* Better support of media
* Better docs
* More editing features
* Pagination
* Better theming support

Installation
============

1. Start Sling
2. Install i18n
3. Install javax.mail
4. Install jcr.compiler
5. Install Sling XSS
6. Install Sightly
7. Deploy Slick 
 * mvn clean install -PautoInstallBundle
 * mvn clean install -PautoInstallBundle -Dsling.host=YOURHOST -Dsling.password=YOURPASSWORD -Dsling.port=YOURPORT

#Base Configuration

1. Admin is located at http://localhost:8080/auth.html (admin:admin)
2. Configure your URL in settings.

#Bare Minimum Apache Configuration
    <VirtualHost *:80>
		 ServerName slick.millr.org
		 ProxyPreserveHost On
		 ProxyPass / http://localhost:8080/ connectiontimeout=5 timeout=300
		 ProxyPassReverse / http://localhost:8080/
    </VirtualHost>

#Dispatcher Configuration
If you are using a dispatcher, you are probably already know what you need to do. You can flush your cache by heading to http://{your-domain}/auth/flush

Styling
=======

1. Use [Brackets](http://brackets.io) + Brackets SASS Extension
2. Slick theme located in /src/main/resources/themes/slick