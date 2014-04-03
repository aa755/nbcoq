nbcoq
=====

NOTE: This plugin was delveloped for Coq version 8.4pl2. It does NOT work with the new 8.4pl3 version because the xml communication protocol for coqtop was changed. Hopefully I would fix that when I start working on my next big Coq project.


Coq plugin for Netbeans

Installation:


Download netbeans+java bundle
http://www.oracle.com/technetwork/java/javase/downloads/jdk-netbeans-jsp-142931.html
If you already have JDK installed you may just download netbeans. I've used (tested) this 
plugin ony with Oracle's JDK.

Open netbeans. Click on team | git and clone this repo.
It will find this netbeans project and ask if you wish to open it.
Say yes.


Right click on the project node and click Install in Development IDE.
(If you made some changes to plugin code, you might want to click debug. Tt will open another instance of netbeans
and let you debug the code.)


Prediodically, you might want to go a git pull to get the latest features(and bugs).
If you do that, Right click on the project node and click Install in Development IDE and restart the IDE
once the reload is finished (might take ~20 seconds to build)


There is a video demonstration/documentation of this plugin at 
http://www.youtube.com/watch?v=94D5RVK-cQg
(watch the video in HD to avoid blurred text)
Corrections to the video: 
You might want to use a C++ project instead of a Java project and provide the Coq Makefile.
Then, you can use the IDE's build system. This might need installation 
of the C++ plugin

You might want to go to Tools | Options | Fonts & Colors and set the profile to Norway Today.
The highlighting colors are currently designed to work with this setting
and cannot be customized w/o changing the plugin code.
If you prefer a white backgroud, set ProofError.DARK to false in ProofSubgoal.java
There is no syntax highlighting at the moment.


A list of known issues can be found at:
https://github.com/aa755/nbcoq/issues?state=open
Feel free to file bugs and feature requests if you encounter one.

As of now, looking at commit messages is the best way to find out about new features.

Shortcuts:

1) for buttons, look at their tooltip text

2) select and middle click in proof/message window to copy it into editor

3) C+A+o : jump to definition. Issues a Locate command internally.

4) C+A+p : If nothing is selected, it Prints the item under cursor(works even in proof/mesg window). Otherwise, it issues a Check command on the selection.

5) C+A+l : SearchAbout the item under cursor

6) click on hyp-name button to copy in into editor

7) drag and drop hyp-buttons to apply one hyp to another

8) select and drag lemma name from any place(e.g. editor,the window which shows SearchAbout o/p) to hyp-button to apply
