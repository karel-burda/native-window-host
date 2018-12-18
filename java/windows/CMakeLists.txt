# Usage: cmake -Bbuild -H. -DJNA_PATH:PATH=<absolute-path-to-jna> -D
# JNA_PATH settings is optional, if the jna.jar and jna-platform.jar is located in the CLASSPATH

cmake_minimum_required(VERSION 3.8 FATAL_ERROR)

project(native-window-embedder-java VERSION 0.4.0 LANGUAGES Java)

find_package(Java COMPONENTS Development REQUIRED)
include(UseJava)

if (NOT JNA_PATH)
    message(FATAL_ERROR "Please specify absolute path to the JNA using the JNA_PATH=<path> attribute")
endif()

message(STATUS "Using this path to the JNA: ${JNA_PATH}")

list(APPEND CMAKE_JAVA_COMPILE_FLAGS -source 1.8 -target 1.8 -Werror -version)

message(STATUS "Using these compile flags: ${CMAKE_JAVA_COMPILE_FLAGS}")

add_jar(
    ${PROJECT_NAME}
        source/com/github/karel-burda/native-window-embedder/NativeWindowEmbedder.java
        source/com/github/karel-burda/native-window-embedder/exceptions/InvalidNativeHandle.java

    INCLUDE_JARS
        ${JNA_PATH}/jna.jar
        ${JNA_PATH}/jna-platform.jar)

get_target_property(_jar_path_core ${PROJECT_NAME} JAR_FILE)

add_jar(
    ${PROJECT_NAME}-demo
        source/com/github/karel-burda/native-window-embedder/demo/Main.java
    INCLUDE_JARS
        ${_jar_path_core}
        ${JNA_PATH}/jna.jar
        ${JNA_PATH}/jna-platform.jar)

get_target_property(_jar_path_demo ${PROJECT_NAME}-demo JAR_FILE)

set(_separator ":")
if (WIN32)
    set(_separator ";")
endif()

add_custom_target(run-demo
    COMMAND
        ${Java_JAVA_EXECUTABLE} -cp "${_jar_path_demo}${_separator}${_jar_path_core}${_separator}${JNA_PATH}/jna.jar${_separator}${JNA_PATH}/jna-platform.jar"
        com.github.karelburda.nativewindowembedder.demo.Main
    DEPENDS ${PROJECT_NAME} ${PROJECT_NAME}-demo)

enable_testing()

#add_test(NAME death-tests
#         COMMAND testDriver --config $<CONFIGURATION> --exe $<TARGET_FILE:myexe>)

#add_test(NAME unit-tests
#         COMMAND testDriver --config $<CONFIGURATION> --exe $<TARGET_FILE:myexe>)

# TODO: add test using ctest and custom command
