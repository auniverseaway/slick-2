About
=====

#For Humans
Slick is a beautiful app to help create exceptional web content. It's highly optimized for blogging.

#For Nerds
Slick is a blogging engine built on top of Sling. The idea was to create a lightweight blogging engine using technologies common to AEM. It uses Sling Models heavily.

#Demo
[slick.millr.org](http://slick.millr.org)

#Requirements
* Java 8
* Sling 8
* Maven 3.2+

#Features
* Creating and editing posts, pages, and media
* WYSIWYG Editor
* RSS 2.0 Feed
* Authentication
* Search
* Post and Page Scheduling
* Dispatcher (
* Analytics and SEO 
* Pagination

Planned
=======
* Commenting
* Documentation
* More editing features

#Installation

1. Start Sling
 * java -jar org.apache.sling.launchpad-8.jar
 * java -Xmx1024M -agentlib:jdwp=transport=dt_socket,address=30303,server=y,suspend=n -jar org.apache.sling.launchpad-8.jar
2. Deploy Slick 
 * mvn clean install -PautoInstallBundle
 * mvn clean install -PautoInstallBundle -Dsling.host=YOURHOST -Dsling.password=YOURPASSWORD -Dsling.port=YOURPORT

#Base Configuration

1. Admin is located at http://localhost:8080/author.html (admin:admin)

#Bare Minimum Apache Configuration
    <VirtualHost *:80>
		 ServerName slick.millr.org
		 ProxyPreserveHost On
		 ProxyPass / http://localhost:8080/ connectiontimeout=5 timeout=300
		 ProxyPassReverse / http://localhost:8080/
    </VirtualHost>

#Dispatcher
1. In settings (author/settings.html) turn on dispatcher.
2. Editing content or changing settings will automatically trigger a cache flush.
3. If the UI has changed, you should flush the UI cache.
4. You can find a sample dispatch.any file under dispatcher/dispatcher.any

#Front-End Development

1. HTML can be found in ui.apps/src/main/resources/jcr_root/apps
2. SCSS, JS, images, and CSS can be found in ui.apps/src/main/resources/jcr_root/etc/slick/designs/slick
2. All SCSS will be compiled upon build.
2. Slick theme located in /src/main/resources/themes/slick