# Usage: cmake -Bbuild -H. -DJNA_PATH:PATH=<absolute-path-to-jna> -DJDK_VERSION:STRING=<version>
# JNA_PATH settings is optional, if the jna.jar and jna-platform.jar is located in the CLASSPATH, JDK_VERSION optional

cmake_minimum_required(VERSION 3.8 FATAL_ERROR)

project(native-window-host-java VERSION 0.9.1 LANGUAGES Java)

find_package(Java COMPONENTS Development REQUIRED)

if (NOT DEFINED JDK_VERSION)
    message(STATUS "JDK_VERSION not given, will use the ${JDK_VERSION}")
    set(JDK_VERSION 1.8)
endif()

message(STATUS "Found this Java version: ${Java_VERSION}, will use this JDK for compilation: ${JDK_VERSION}")

include(UseJava)

if (NOT JNA_PATH)
    message(FATAL_ERROR "Please specify absolute path to the JNA using the JNA_PATH=<path> attribute")
endif()

message(STATUS "Using this path to the JNA: ${JNA_PATH}")

list(APPEND CMAKE_JAVA_COMPILE_FLAGS -source ${JDK_VERSION} -target ${JDK_VERSION} -version)

message(STATUS "Using these compile flags: ${CMAKE_JAVA_COMPILE_FLAGS}")

add_jar(
    ${PROJECT_NAME}
        ${CMAKE_CURRENT_SOURCE_DIR}/source/com/github/karel-burda/native-window-host/NativeWindowHost.java
        ${CMAKE_CURRENT_SOURCE_DIR}/source/com/github/karel-burda/native-window-host/exceptions/InvalidNativeHandle.java
        ${CMAKE_CURRENT_SOURCE_DIR}/source/com/github/karel-burda/native-window-host/exceptions/NativeError.java
    INCLUDE_JARS
        ${JNA_PATH}/jna.jar
        ${JNA_PATH}/jna-platform.jar)

get_target_property(_jar_path_core ${PROJECT_NAME} JAR_FILE)

add_jar(
    ${PROJECT_NAME}-demo
        ${CMAKE_CURRENT_SOURCE_DIR}/source/com/github/karel-burda/native-window-host/demo/Main.java
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
        com.github.karelburda.nativewindowhost.demo.Main
    DEPENDS ${PROJECT_NAME} ${PROJECT_NAME}-demo)
