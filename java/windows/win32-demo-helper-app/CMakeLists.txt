cmake_minimum_required(VERSION 3.8 FATAL_ERROR)

project(win32-demo-helper-app LANGUAGES CXX)

message(STATUS "Building ${PROJECT_NAME}")

add_executable(${PROJECT_NAME} main.cpp)
