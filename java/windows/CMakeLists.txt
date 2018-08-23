# Usage: cmake -Bbuild -H. -DJNA_PATH:PATH=<path-to-jna>
# JNA_PATH settings is optional, if the jna.jar and jna-platform.jar is located in the CLASSPATH

cmake_minimum_required (VERSION 3.8 FATAL_ERROR)

project (native-window-embedder-java VERSION 0.3.0 LANGUAGES Java)

include("../../cmake-helpers/cmake-helpers/messages.cmake")

_print_project_version()

find_package(Java REQUIRED)
include(UseJava)

if (JNA_PATH)
    get_filename_component(JNA_PATH "${JNA_PATH}" REALPATH BASE_DIR "${CMAKE_SOURCE_DIR}")
    _print_status_message("Using this path to the JNA: ${JNA_PATH}")
endif()

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")

# TODO: configure manifest and add ClassPath to it poiting to the jna

add_jar(
    ${PROJECT_NAME}
        Com/Github/KarelBurda/NativeWindowEmbedder/NativeWindowEmbedder.java
        Com/Github/KarelBurda/NativeWindowEmbedder/Main.java
    MANIFEST
        MANIFEST.mf
    INCLUDE_JARS
        ${JNA_PATH}/jna.jar
        ${JNA_PATH}/jna-platform.jar)

get_target_property(_jarFile ${PROJECT_NAME} JAR_FILE)
get_target_property(_classDir ${PROJECT_NAME} CLASSDIR)

message(STATUS "Jar file ${_jarFile}")
message(STATUS "Class compiled to ${_classDir}")

enable_testing()

add_test(NAME Demo COMMAND ${Java_JAVA_EXECUTABLE} -cp ${_jarFile} ${PROJECT_NAME})
