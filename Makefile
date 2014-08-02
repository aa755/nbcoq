jar: Manifest.txt
	cp -r build/classes/coq ./
	cp release/modules/ext/* lib/
	jar cfm DebugUniv.jar Manifest.txt coq
