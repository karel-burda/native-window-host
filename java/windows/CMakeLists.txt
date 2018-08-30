# Usage: cmake -Bbuild -H. -DJNA_PATH:PATH=<absolute-path-to-jna>
# JNA_PATH settings is optional, if the jna.jar and jna-platform.jar is located in the CLASSPATH

cmake_minimum_required (VERSION 3.8 FATAL_ERROR)

project (native-window-embedder-java VERSION 0.3.1 LANGUAGES Java)

include("../../cmake-helpers/cmake-helpers/messages.cmake")

_print_project_version()

find_package(Java REQUIRED)
include(UseJava)

if (NOT JNA_PATH)
    message(FATAL_ERROR "Please specify absolute path to the JNA using the JNA_PATH=<path> attribute")
endif()

_print_status_message("Using this path to the JNA: ${JNA_PATH}")

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.8" "-target" "1.8")

add_jar(
    ${PROJECT_NAME}
        source/com/github/karel-burda/native-window-embedder/NativeWindowEmbedder.java
        source/com/github/karel-burda/native-window-embedder/Main.java
    INCLUDE_JARS
        ${JNA_PATH}/jna.jar
        ${JNA_PATH}/jna-platform.jar)

get_target_property(_jar_location ${PROJECT_NAME} JAR_FILE)
get_target_property(_class_location ${PROJECT_NAME} CLASSDIR)

set(_class_path_separator ":")
if (WIN32)
    set(_class_path_separator ";")
endif()

add_custom_target(run-demo
    COMMAND
        ${Java_JAVA_EXECUTABLE} -cp "${_jar_location}${_class_path_separator}${JNA_PATH}/jna.jar${_class_path_separator}${JNA_PATH}/jna-platform.jar"
        com.github.karelburda.nativewindowembedder.Main
    DEPENDS ${PROJECT_NAME})

enable_testing()

# TODO: add test using ctest and custom command
