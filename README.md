nbcoq
=====

Coq plugin for Netbeans

Installation:


Download netbeans+java bundle
http://www.oracle.com/technetwork/java/javase/downloads/jdk-7-netbeans-download-432126.html

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


A list of known issues can be found at:
https://github.com/aa755/nbcoq/issues?state=open
Feel free to file bugs and feature requests if you encounter one.

As of now, looking at commit messages is the best way to find out about new features.

Shortcuts:

1) for buttons, look at their tooltip text

2) select and middle click in proof/message window to copy it into editor

3) C+A+o : jump to definition

4) C+A+p : Print the item under cursor(works even in proof/mesg window)

5) C+A+l : SearchAbout the item under cursor

6) click on hyp-name button to copy in into editor

7) drag and drop hyp-buttons to apply one hyp to another

8) select and drag lemma name from any place(e.g. editor,the window which shows SearchAbout o/p) to hyp-button to apply
