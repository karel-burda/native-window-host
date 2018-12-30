cmake_minimum_required(VERSION 3.8 FATAL_ERROR)

project(win32-demo-helper-app LANGUAGES CXX)

message(STATUS "Building ${PROJECT_NAME}")

if (NOT WINDOWS)
    message(FATAL_ERROR "This project is windows-only")
endif()

add_executable(${PROJECT_NAME} WIN32 main.cpp)

target_compile_options(${PROJECT_NAME}
                           PRIVATE
                               $<$<CXX_COMPILER_ID:MSVC>:
                                   /W4>
