# enRoute Blog
This workspace contains a simplistic Blog application. The purpose of this workspace
is to demonstrate many OSGi techniques. This workspaces contains a number of branches
that show the different stages of evolution in designing such an application.

## Branches

### 00-initial
The `00-initial` branch contains a pristine workspace. For convenience it will 
contain a fully loaded repository (which is downloaded on demand, jars are not 
included). So there is only a cnf project. 

### 01-setup
The purpose of the setup branch is to show how you can launch a framework 
and configure it. This branch creates a project `osgi.enroute.blog.appl`. 
This project is populated with some bundles defined in the `cnf/lib/ repository. 
These bundles provide a web application environment with some debugging bundles 
installed, like the shell, webconsole, and of course XRay. Since this environment 
needs parameters, the provides configuration information in `configuration/configuration.json`, 
which is picked up by the installed aQute.configurer bundle. 

### 02-hello
The previous branch did not have any content. In this branch, a static directory 
is added tot the bundle. This directory is mapped to the web server's name space 
(and merged with other bundles that provide content in the static directory). 
The hello branch  shows you therefore that you can display static text and resources. 

### 03-bootstrap
Bootstrap is the immensely popular CSS library from Twitter. This branch adds
bootstrap as a bundle and uses it from the index.html. The index.html is also
updated to be state of the art HTML-5. A nav bar is added and a footer. The footer
uses bnd's pre-processing engine to automatically show the build year and the
bundle's version.

### 04-angular
Angular JS is a library from Google that is a perfect adjunct to OSGi. It has a
model that is very like and compatible with the OSGi service model.In this branch
we create a Javascript/HTML only blog application written with Angular.

### 05-rest
REST is a standard that is well supported by Angular and is easy to support in 
OSGi. In this branch we extend the application of the previous branch to get
a Blog Post from the server. We use the aQute.rest library, which needs
to be configured. We ad a BlogApp service that receives the REST requests.

We create two remote functions on the server. One to get some test data, another to
create an exception to test error handling.

### 06-blog-gui
So far, the Javascript and HTML was local and simple, all took place on a single
page and state was maintained internally. Real applications need to keep the
core state of the application in the URL (so the application remains largely stateless
and you can share URLs). Angular has excellent, although hidden, support for this
with their $route service. The $route service uses a table driven approach to 
load html fragments and assign controllers based on the url.

In this, rather large, exercise the application is turned into a small Blog Application.
We will fake a backend in Javascript. This application has the following pages:

* home — Shows lists of blogs
* post/:id — Shows a specific post
* edit — Edit a new post
* edit/:id — Edit an existing post
* search?query — The result of a search

Additionally, we add a button to create some test data on the server.

### 07-blog-facade 
In this exercise we create a backend REST server that provides the CRUD operations
and connect this to the blog.js program from the previous exercise. The
backend keeps the blog posts in memory, in a HashMap. 

### 08-memory-provider
In the previous branch the the Blog App was a facade that also implemented the
blog manager. This is in general not good design since it is not cohesive. The
primary responsibility of the facade is to handle the incoming request, perform
security, parameterize the current user, and then call services for the 'vertical' 
work.

Therefore, in this branch we develop a Blog Manager API and move the facade's
in memory implementation of the API to an external bundle. We then modify the
facade to use the service.   

### 09-api-tester
If you write an implementation, you need to test. The osgi.enroute.blog.test bundle is
an OSGi test case. It can run from Eclipse and from the command line (either ant or bnd).

The test bundle has a bnd.bnd file that is setup to run the tests against the memory
provider. So you can always do *Debug As/OSGi JUnit Launcher* on this project.

### 10-jpa
This branch implements the osgi.enroute.blog.api with JPA. It shows how the 
OSGi Managed Persistence model can be used to make a very small and concise
implementation. The branch also integrates the test suite in this project so 
that the project can be ran like an OSGi JUnit test.

Additionally, the branch adds a enblog.bndrun file to the osgi.enroute.blog.appl 
project. This file is setup to run hibernate/eclipse with H2 for the blog
application.

## How to use
If you checkout the final branch '10-jpa' then you have the whole application
working. You can then do the following actions

### Main App
The main application can be run in the osgi.enroute.blog.appl project, look
at the blog.bndrun file. In Eclipse you can run this with Run As/Bnd Launcher.
In bnd you can do:

    $ bnd enblog.bndrun

### Debug the Main App
You can run the bnd.bnd in the  osgi.enroute.blog.appl project. This will run
the application with the memory based Blog Manager.

### Test the Memory/JPA implementation
You can run the test the JPA implementation by selecting the project
(osgi.enroute.blog.jpa.provider) and then do  Debug As/Bnd OSGi Test 
Launcher (Junit). This will run the tests for that project. You can of 
course set breakpoints. The test project itself can also be ran, it tests
by default against the memory provider.

### Build a Packaged App
You can package the app by going to the osgi.enroute.blog.appl project and run

     $ bnd package blog.bndrun

If you have jpm installed, then you can do:

     $ sudo jpm install enblog.jar
     $ enblog
     .... logging info

Otherwise:

     $ java -jar enblog.jar
     ... lots of logging


  



