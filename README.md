# enRoute Blog
This workspace contains a simpistic Blog application. The purpose of this workspace
is to demonstrate many OSGi techniques. This workspaces contains a number of branches
that show the different stages of evolution in desiging such an application.

## Branches
### init
The `init` branch contains a pristine workspace. For convenience it will contain a fully
loaded repository (which is downloaded on demand, jars are not included). So there is
only a cnf project. 

### hello 
The hello branch shows how you can display static text and resources.

### bootstrap
Bootstrap is the immensely popular CSS library from Twitter. This branch adds
bootstrap as a bundle and uses it from the index.html.

### angular
Angular JS is a library from Google that is a perfect adjunct to OSGi. It has a
model that is very like and compatible with the OSGi service model. This branch
adds a small Angular JS program.

### rest
Adding a REST API so we can call the server

### gui
In this branch we develop a simple Angular JS GUI for a (rather simplistic) Blog
application. This application has the following pages:

* home — Shows lists of blogs
* post/:id — Shows a specific post
* edit — Edit a new post
* edit/:id — Edit an existing post
* search?query — The result of a search

This branch has some test data to play with the GUI.

### service 
In this branch a osgi.enroute.blog.api bundle is developed to capture the essence
of a (simple) blog service. This service will register a `BlogManager`

### memory
A simple implementation is provided to be a proof of concept. The osgi.enroute.blog.memory.provider
bundle demonstrates that the API is viable and can be used for testing.

### test
If you write an implementation, you need to test. The osgi.enroute.blog.test bundle is
an OSGi test case. It can run from Eclipse and from the command line (either ant or bnd).
The test bundle is integrated with the memory implementation to test that it conforms.

### facade
In this branch we add a facade that uses REST. Here we implement a number
of methods from the user interface and use the Blog Manager service to provide
us access to the blog posts.

### jpa
This branch implements the osgi.enroute.blog.api with JPA. It shows how the 
OSGi Managed Persistence model can be used to make a very small and concise
implementation. The branch also integrates the test suite.

## How to use
If you checkout the final branch 'jpa' then you have the whole application
working. You can then do the following actions

### Main App
The main application can be run in the osgi.enroute.blog.appl project, look
at the blog.bndrun file. In Eclipse you can run this with Run As/Bnd Launcher.
In bnd you can do:

    $ bnd blog.bndrun

### Debug the Main App
You can run the bnd.bnd in the  osgi.enroute.blog.appl project. This will run
the application with the memory based Blog Manager.

### Test the Memory/JPA implementation
You can run the test on the memory and JPA implementation by selecting the project
(osgi.enroute.blog.jpa.provider or osgi.enroute.blog.memory.provider) )and then do 
Debug As/Bnd OSGi Test Launcher (Junit). This will run the tests for that project.
You can of course set breakpoints.

### Build a Packaged App
You can package the app by going to the osgi.enroute.blog.appl project and run

     $ bnd package blog.bndrun

If you have jpm installed, then you can do:

     $ sudo jpm install blog.jar
     $ enRoute.blog
     .... logging info

Otherwise:

     $ java -jar blog.jar
     ... lots of logging


  



