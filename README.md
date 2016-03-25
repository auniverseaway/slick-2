#About
Slick is a beautiful app to help create exceptional web content. It's highly optimized for blogging.

It is built on top of Sling, Sightly, Oak, OSGi and many other frameworks common to Adobe Experience Manager.

#Demo
[slick.millr.org](http://slick.millr.org) | [experiencemanaged.com](http://experiencemanaged.com)

#Features
* Creating and editing posts, pages, and media
* Post and Page Scheduling
* WYSIWYG Editor
* RSS 2.0 Feed
* Authentication
* Search
* Dispatcher
* Analytics, SEO, and social integrations
* Pagination

#Requirements
* Java 8
* Sling 8
* Maven 3.2+

#Planned
* Commenting
* Documentation
* More editing features

#Installation

1. Start Sling
 * Normal Run
   * java -jar org.apache.sling.launchpad-8.jar
 * Debug Run
   * java -Xmx1024M -agentlib:jdwp=transport=dt_socket,address=30303,server=y,suspend=n -jar org.apache.sling.launchpad-8.jar
2. Deploy Slick 
 * mvn clean install -PautoInstallBundle
 * mvn clean install -PautoInstallBundle -Dsling.host=YOURHOST -Dsling.password=YOURPASSWORD -Dsling.port=YOURPORT

#Authoring
1. Located at http://localhost:8080/author.html (admin:admin)

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

#Back-End Development
1. Open Eclipse
2. Import Maven project
3. Deploy org.apache.sling.tooling.support.install bundle to your Sling intance.
4. Setup new Sling Server in Eclipse.
5. Start buidling great things.

#Front-End Development
1. HTML can be found in ui.apps/src/main/resources/jcr_root/apps
2. SCSS, JS, images, and CSS can be found in ui.apps/src/main/resources/jcr_root/etc/slick/designs/slick
2. All SCSS will be compiled during autoInstallBundle.