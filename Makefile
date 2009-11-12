
FISICA_VERSION=2

JAVAC_ARGS=-source 5 -target 1.5
CLASSPATH=3rdparty/JBox2D-2.0.1-b229-Library.jar:"$(PROCESSING_PATH)/lib/core.jar":"$(PROCESSING_PATH)/libraries/xml/library/xml.jar"
DIST_DIR=distribution/fisica

#
# TODO: add check that PROCESSING_PATH is defined
#

library/fisica.jar: src/fisica/*.java
	mkdir -p build
	mkdir -p library
	javac $(JAVAC_ARGS) src/fisica/*.java -d build -cp $(CLASSPATH)
	cp 3rdparty/JBox2D*.jar library
	jar cvf library/fisica.jar -C build fisica

dist: library/fisica.jar #doc
	mkdir -p $(DIST_DIR)/library

        ##  Copy libs
	cp library/fisica.jar $(DIST_DIR)/library
	cp 3rdparty/JBox2D*.jar $(DIST_DIR)/library

        ##  Copy docs
	#cp README $(DIST_DIR)
	#cp COPYING $(DIST_DIR)
	#cp HANDBOOK $(DIST_DIR)

        ##  Copy files
	cp -r examples $(DIST_DIR)
	#cp -r tutorial $(DIST_DIR)
	#cp -r src $(DIST_DIR)

        ##  Zip up
	rm -f distribution/fisica-*.zip
	cd distribution && zip -r fisica-$(FISICA_VERSION).zip .

doc:
	#mkdir -p $(DIST_DIR)
	#javadoc -classpath $(CLASSPATH) -doclet prodoc.StartDoclet -docletpath external -sourcepath src fisica
	#rm -rf $(DIST_DIR)/documentation
	ant -f fisica-javadoc.xml
	mv -f documentation $(DIST_DIR)


clean:
	rm -rf build
	rm -rf distribution

clean_all: clean
	rm -rf library
