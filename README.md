GAIA tool for the Roland GAIA SH-01
===================================

Copyright 2010 Laurens Holst

Table of Contents
-----------------

- Project information
- Contribution information
- Dependencies
- Getting the source code
- Build instructions
- Eclipse setup instructions


Project information
-------------------

Name: GAIA tool  
Site: <http://www.grauw.nl/projects/gaia-tool/>  
Source: <https://bitbucket.org/grauw/gaia-tool>  
Author: Laurens Holst  
Contact: <laurens.nospam@grauw.nl>  
License: Apache License, Version 2.0

The Gaia Tool is a free tool to view and edit the settings of a Roland GAIA
SH-01 synthesizer connected to your PC or Mac.

It is written in Java and should work on all Java SE 6 plaforms. Specific
builds for Windows and Mac are also generated.

The tool is Apache 2.0 licensed, which means that you are free to use the source
code in any way you wish as long as attribution is given to the original project
and its author(s). For details, please consult the LICENSE file.


Contribution information
------------------------

First of all, contributions are very welcome!

However to make it easier for me to accept your contributions and to minimise
any wasted effort, here are some guidelines you should try to follow when
contributing:

  * Coordinate your changes with me.
    
    When you start, contact me and tell me about your ideas. That way we can
    brainstorm a on it a bit, and make sure that we agree on the direction.
    This way you won’t be wasting your time on duplicated effort or a dead end.

  * Share your work in progress.
    
    Once you have code to share, send me a link to your Mercurial repository so
    I can see your changes. If you need a place to put it, bitbucket.com offers
    free hosting. I can then provide code review comments and catch problems
    early, saving you time.

  * Adhere to the coding style.

    I think it is important, so please adhere to it. You should be able to glean
    much of the style from the existing code, and I think most of these are
    standard Java and good practices, but let me just name a few:
    
      - Indent code with tabs.
      - Put curly braces on the same line.
      - Always put the body of an `if`/`for`/`while` statement on a new line.
      - `if`/`for`/`while` blocks braces are optional, but don’t mix.
      - Variables and members are written with lower `camelCase`. Class names
        use upper `CamelCase`.
      - Don’t abbreviate with initialisms. E.g. write `FileReader reader`, not
        `FileReader fr`. (Notable exception: `i`/`j` for loop counters.)
      - Don’t break lines at 80 characters, you can go up to 120 or so.
      - Keep your functions small.
      - Document your functions (but DRY).
      - Only write inline comments when absolutely necessary.

  * Make small, incremental commits.
    
    I really prefer not to receive one big blob of code, so please chop up your
    work into small commits. Keep unrelated changes separate: don’t make a
    functional change in the same commit as adjusting white space. If you need
    to refactor something, do it in a separate commit. Bullet points or the word
    “also” in your commit messages is a strong indicator it needs to be split.
    
    Doing this is easier for me to review, and also makes it easier for me to
    already accept parts of your work into the code base while other parts get
    refined a little further.

  * Adhere to the MVC pattern
    
    This project is using the Model-View-Controller pattern, which dictates that
    you separate the domain logic from the presentation. This gives you reusable
    models which contain all the logic and can basically work independently, and
    interchangeable views which focus solely on presentation.
    
    To notify views of changes to the models I use an observer pattern; a model
    extends Observable and invokes notifyObservers() whenever it changes, and
    then all registered classes implementing Observer will receive a callback
    to their update() method.
    
    The Gaia Tool’s Observable only retains weak references to the observers, so
    generally you don’t have to worry about removing the observers for garbage
    collection. Also, views have to use the AWTObserver interface, this ensures
    that their notifications are done on the AWT thread as required by Swing.

* Write unit tests
    
    Please write some unit tests for functionality that you add. Now I’ll be the
    first to admit that the current codebase hasn’t got full test coverage, but
    it really does make a difference in terms of reliability. Note that you
    don’t have to write tests for the views.


Dependencies
------------

  * Java 6
    
    This project is written using the Java SE 6 SDK.
    
    Download: <http://www.oracle.com/technetwork/java/javase/downloads/index.html>  
    Version check: `java -version`

  * Maven 3
    
    This project is built using Maven 3, although Maven 2 will probably work as
    well (but I did not test this). It follows standard Maven project setup.
    
    Download: <http://maven.apache.org/download.html>  
    Version check: `mvn --version`

  * Mercurial
    
    The source code of this project is managed with the Mercurial SCM.
    
    Download: <http://hg-scm.org/>  
    Version check: `hg version`


Getting the source code
-----------------------

The source is managed using the Mercurial SCM, you can retrieve it with the
following command:

    hg clone https://bitbucket.org/grauw/gaia-tool gaia-tool

This will download the project source code into the gaia-tool directory.

If you’re new to Mercurial, here are some resources to get you started:

  <http://mercurial.selenic.com/wiki/BeginnersGuides>  
  <http://hginit.com/>

In a nutshell: Mercurial is a distributed version control system (DVCS), which
means that the complete source code repository including all history is cloned
onto your hard drive. You can perform all version control operations on that
local clone, such as committing and viewing change history. When you are ready
to share your changes you push them to a public repository, which can then be
reviewed and merged back into the main project repository.

Advantages of this distributed model are that all operations are fast, you can
continue working when you have no internet connection or the server is down, you
can experiment without fear of messing up the main repository, and you don’t
need to ask me for commit access.

The command line interface of Mercurial is very simple and intuitive, but I
would also recommend the TortoiseHg GUI if you’re on Windows or Linux. On
Mac OS X, I have good experiences with Atlassian’s SourceTree.


Build instructions
------------------

From the project directory, enter the following command:

    mvn verify

This will build and test the project, and output the binaries into the `target`
directory. The first time it may take a while, because Maven needs to download
dependencies.

The build creates both a general-purpose jar, as well as a .exe-version for
Windows and a -app.zip version for Mac OS X. Some other jars are also generated
but these are intermediate files and you can ignore them.

To make Mac OS X builds on Windows or Linux machines, you need to get a copy of
the `JavaApplicationStub` file from a Mac and put it in the
`src/main/app-resources` directory. The next time you build, a Mac binary will
be generated as well. Due to licensing issues I can not include this file with
the source code.


Eclipse setup instructions
--------------------------

There is no requirement for any specific editor, however here are some
instructions to get you started with this project in Eclipse. Eclipse can be
downloaded from <http://www.eclipse.org/>.

(Note that these instructions are based on Eclipse Indigo SR1, you may have to
adapt them a little for older or newer versions of Eclipse.)

You can let Maven generate the project configuration so that Eclipse is
properly configured using the following command:

    mvn eclipse:eclipse

Next, in Eclipse, go to File, Import, select General, Existing Projects into
Workspace, and press Next. Browse to your project folder, and press Finish.

Now you can build and launch the project from Eclipse by right-clicking on
the project, selecting Debug As, Java Application. You can also run the
unit tests by selecting Debug As, JUnit Test.

If you have the m2eclipse (Maven) plugin for Eclipse installed, you can also
right-click on the project and select Configure, Convert to Maven Project
to enable some extra Maven integration.
