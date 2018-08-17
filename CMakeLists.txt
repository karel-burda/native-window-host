cmake_minimum_required(VERSION 3.8 FATAL_ERROR)

project(native-window-embedder VERSION 0.2.0 LANGUAGES NONE)

set(OTHER
    README.md
    .travis.yml)

add_custom_target(${PROJECT_NAME} SOURCES ${OTHER})
