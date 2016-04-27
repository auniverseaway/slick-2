#About
Slick is a beautiful app to help create exceptional web content. It's highly optimized for blogging.

It's built on top of Sling, Sightly, Oak, OSGi and many other frameworks common to Adobe Experience Manager.

![Travis Status](https://travis-ci.org/auniverseaway/slick2.svg?branch=master)

![Sling and Slick](https://raw.githubusercontent.com/auniverseaway/slick2/master/ui.apps/src/main/resources/jcr_root/etc/slick/designs/slick/img/sling-slick-logo.png)

#Demo
[slick.millr.org](http://slick.millr.org) | [experiencemanaged.com](http://experiencemanaged.com)

#Features
* Creating and editing posts, pages, and media
* Post and Page Scheduling
* WYSIWYG Editor
* RSS 2.0 Feed
* Authentication and user management
* Search
* Dispatcher Caching
* Analytics, SEO, and social integrations
* Pagination
* Basic Localization

#Requirements
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Sling 8](http://sling.apache.org/downloads.cgi)
* [Maven 3+](http://maven.apache.org/download.cgi)

#Installation
##1. Run Sling

    java -jar org.apache.sling.launchpad-8.jar

##2. Install Slick

    mvn clean install -PautoInstallBundle

##3. Make things

    http://localhost:8080

#Advanced Installation
##Debug Run

    java -Xmx1024M -agentlib:jdwp=transport=dt_socket,address=30303,server=y,suspend=n -jar org.apache.sling.launchpad-8.jar

##Install With Options

    mvn clean install -PautoInstallBundle -Dsling.host=YOURHOST -Dsling.password=YOURPASSWORD -Dsling.port=YOURPORT

#Authoring
Located at [http://localhost:8080/author.html](http://localhost:8080/author.html) (admin:admin)

#Apache Configuration
    <VirtualHost *:80>
        ServerName slick.millr.org
        ProxyPreserveHost On
        ProxyPass / http://localhost:8080/ connectiontimeout=5 timeout=300
        ProxyPassReverse / http://localhost:8080/
    </VirtualHost>

    RewriteEngine on

    # Rewrite the homepage to posts
    RewriteRule ^/$ /content/slick/publish/posts.html [PT,L]
    
    # Rewrite Fav Icon
    RewriteRule ^/favicon.ico$ /etc/slick/designs/slick/img/favicon.ico [PT,L]
    
    # Rewrite our Robots
    RewriteRule ^/robots.txt$ /etc/slick/designs/slick/txt/robots.txt [PT,L]

    # Rewrite our author 
    RewriteCond %{REQUEST_URI} !^/apps
    RewriteCond %{REQUEST_URI} !^/bin
    RewriteCond %{REQUEST_URI} !^/content
    RewriteCond %{REQUEST_URI} !^/etc
    RewriteCond %{REQUEST_URI} !^/home
    RewriteCond %{REQUEST_URI} !^/libs
    RewriteCond %{REQUEST_URI} !^/tmp
    RewriteCond %{REQUEST_URI} !^/var
    RewriteCond %{REQUEST_URI} !^/system
    RewriteRule ^/author(.*)$ /content/slick/author$1 [PT,E,L]

    # Rewrite everything else to publish
    RewriteCond %{REQUEST_URI} !^/apps
    RewriteCond %{REQUEST_URI} !^/bin
    RewriteCond %{REQUEST_URI} !^/content
    RewriteCond %{REQUEST_URI} !^/etc
    RewriteCond %{REQUEST_URI} !^/home
    RewriteCond %{REQUEST_URI} !^/libs
    RewriteCond %{REQUEST_URI} !^/tmp
    RewriteCond %{REQUEST_URI} !^/var
    RewriteCond %{REQUEST_URI} !^/system
    RewriteRule ^/(.*)$ /content/slick/publish/$1 [PT,E,L]
    RewriteRule ^/content/slick/publish/(.*)$ /$1 [R=301,NC,L]

#Dispatcher
1. In settings (author/settings.html) turn on dispatcher.
2. Editing content or changing settings will automatically trigger a cache flush.
3. If ui.apps has changed, you should flush the UI cache.
4. You can find a sample dispatch.any file at dispatcher/dispatcher.any

#Back-End Development
1. Open Eclipse
2. Import Maven project
3. Install the [org.apache.sling.tooling.support.install](http://mvnrepository.com/artifact/org.apache.sling/org.apache.sling.tooling.support.install) bundle to your [Sling instance](http://localhost:8080/system/console/bundles).
4. Setup new Sling Server in Eclipse.
5. Start buidling great things.
6. Change the password at {YOUR-WORKSPACE}/.metadata/.plugins/org.eclipse.wst.server.core/servers.xml

#Front-End Development
1. HTML can be found in ui.apps/src/main/resources/jcr_root/apps
2. SCSS, JS, images, and CSS can be found in ui.apps/src/main/resources/jcr_root/etc/slick/designs/slick
3. All SCSS will be compiled during autoInstallBundle. You can also run gulp inside ui.apps.