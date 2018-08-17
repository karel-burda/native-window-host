# Usage: cmake -Bbuild -H. -DJNA_PATH:PATH=<path-to-jna>
# JNA_PATH settings is optional, if the jna.jar and jna-platform.jar is located in the CLASSPATH

cmake_minimum_required (VERSION 3.8 FATAL_ERROR)

project (native-window-embedder-java VERSION 0.2.0 LANGUAGES Java)

find_package(Java REQUIRED)
include(UseJava)

enable_testing()

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")
set(CLASSPATH
    ${JNA_PATH}/jna.jar
    ${JNA_PATH}/jna-platform.jar)

list(APPEND CMAKE_JAVA_INCLUDE_PATH ${CLASSPATH})

add_jar(${PROJECT_NAME} NativeWindowEmbedder.java)
add_jar(Demo NativeWindowEmbedder.java Main.java)

get_target_property(_jarFile ${PROJECT_NAME} JAR_FILE)
get_target_property(_classDir ${PROJECT_NAME} CLASSDIR)

message(STATUS "Jar file ${_jarFile}")
message(STATUS "Class compiled to ${_classDir}")

add_test(NAME Demo COMMAND ${Java_JAVA_EXECUTABLE} -cp ${_jarFile} ${PROJECT_NAME})
