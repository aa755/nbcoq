nbcoq : Coq plugin for Netbeans
=====

NOTE: This plugin was delveloped for Coq version 8.4pl2. It does NOT work with 8.4pl3 or later versions because the xml communication protocol for coqtop was changed. 

Key features :
1) All of netbeans's features. I regularly use features like (regexp) search (and replace) content in a project (not just individual files), multiple windows (usually on multiple screens), live version control information. Given the drastic variation in pixel densities screens, the font of the proof window can be adjusted independently of the editor (click on the + or - button)

2) Entering unicode content by entering the corresponding latex. This uses https://github.com/tomtung/latex2unicode . ((see keyboard shortcut below)

3) Drag and drop gestures for working on proofs. My proof scripts have often too long variable names. It jused to be a pain to type them manually.

4) Hypothesis with same type are bunched together. Saves a lot of space while working on messy proofs.

5) Experimental support for jumping to definition (see keyboard shortcut below)

6) A drop down for entering queries like SearchAbout, Check, Print , e.t.c. The dropdown remembers old queries. There is a checkbox to display query output on a separate popup window. This is useful to compare definitions.

7) Experimental support for coqdoc-like syntax hihglihgitng using .glob files ; color categories include Variables, theorems, definitions, inductive definitions, constructors of (coInductive) types. If an uptodate .glob file is present, one can click SH to do syntax highlighting. After that, deleting contents preserves .glob based syntax highlighting. So does insertion and modification, except that the new text is not highlighted. For that, one has to compile again to create fresh .glob file and then click SH again. It should be possible to automate the compilation part.

Installation:

Download netbeans+java bundle
http://www.oracle.com/technetwork/java/javase/downloads/jdk-netbeans-jsp-142931.html
(If you already have JDK installed, you may just download netbeans (https://netbeans.org/).
I've used (tested) this plugin ony with Oracle's JDK.)

Open netbeans. Click on team | git and clone this repo.
It will find this netbeans project and ask if you wish to open it.
Say yes.

Right click on the project node and click Install in Development IDE.

Prediodically, you might want to go a git pull to get the latest features (and bugs).
If you do that, Right click on the project node and click Install in Development IDE and restart the IDE
once the reload is finished (might take ~20 seconds to build).


There is an old and outdated video demonstration/documentation of this plugin at 
http://www.youtube.com/watch?v=94D5RVK-cQg
 (watch the video in HD to avoid blurred text)

You might want to go to Tools | Options | Fonts & Colors and set the profile to Norway Today.
The highlighting colors are currently designed to work with this setting
and cannot be customized w/o changing the plugin code.
If you prefer a white backgroud, set ProofError.DARK to false in ProofSubgoal.java
There is no syntax highlighting at the moment.


A list of known issues can be found at:
https://github.com/aa755/nbcoq/issues?state=open
Feel free to file bugs and feature requests if you encounter one.

If you make changes to the code fo this plugin, you can click Debug in netbeans menu for this project. It will open another instance of netbeans and let you debug the plugin code. You can visually set breakpoints, view variables and watches, e.t.c.

Shortcuts:

1) for buttons, look at their tooltip text

2) select and middle click in proof/message window to copy it into editor

3) Ctrl+Alt+o : jump to definition. Issues a Locate command to coqtop internally.

4) Ctrl+Alt+p : If nothing is selected, it Prints the item under cursor(works even in proof/mesg window). Otherwise, it issues a Check command on the selection.

5) Ctrl+Alt+l : SearchAbout the item under cursor

6) click on hyp-name button to copy in into editor

7) drag and drop hyp-buttons to apply one hypothesis to another

8) select and drag lemma name from any place (e.g. editor,the window which shows SearchAbout o/p) to hyp-button to apply

9) Ctrl+Alt+l to convert selected latex text to unicode.

For another IDE built with many similar goals, checkout Coqoon http://itu.dk/research/tomeso/coqoon/
