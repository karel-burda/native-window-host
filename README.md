![Version](https://img.shields.io/badge/version-0.3.1-green.svg)
[![License](https://img.shields.io/badge/license-MIT_License-green.svg?style=flat)](LICENSE)
[![Build Status](https://travis-ci.org/karel-burda/native-window-embedder.svg?branch=develop)](https://travis-ci.org/karel-burda/native-window-embedder)

# Introduction
`native-windows-embedder` shows Proof of Concept how can native windows (e.g win32 window on Windows) might be embedded (hosted) in Java and C# GUI elements.

Java concept uses `Java Native Access` and C# uses `HwndHost` from the WPF framework.

See [java](java) for Java implementation and [C#](csharp) for C# implementation with WPF.

**Currently, only Windows concept is implemented.**

# Technical Details
## Java
Java implementation uses the JNA and `Canvas` component from the AWT. `Canvas` is used because it's a heavy-weight component (it has its own handle, e.g. `HWND` on Windows) and thus has ability to become a parent of native system window.

The basic idea is that when this inherited `Canvas` component become parent window (`SetParent(...)` call in Windows), all system messages that are sent to the `Canvas` are also sent to the child (the native window). Because of this parent-child relationship, the GUI-related things like moving, resizing, work (more or less) out of the box.

## C\#
The concept is using the `HwndHost` component from WPF that effectively works well without much code needed.

The only catch is to use set the correct window style to the native window (`WS_CHILD`).

# Build Process
## Java
Java project is built using CMake: [CMakeLists.txt](java/windows/CMakeLists.txt).

Projects is divided into separate JAR files that are also executed via CMake (either for demo or tests):
* `native-window-embedder-java.jar` -- makes the core functionality, depends on the JNA
* `native-window-embedder-java-demo.jar` -- demonstration, creates the native window artificially and then uses the `NativeWindowEmbedder` class to embedd it 
* `native-window-embedder-java-unit-tests.jar` -- automatic tests

So the build itself is being executed using:
```shell
cmake -Bbuild -H. -DJNA_PATH:PATH=<absolute-path-to-jna>
cmake --build build
```
where `<absolute-path-to-jna>` is absolute path to the directory where `jna.jar` and `jna-platform.jar` reside.

Demo might be run using (after the generation step is done, you can skip the `cmake --build build`):
```shell
cmake --build build --target run-demo
```

# Unit Tests
## Java
Unit tests are divided into 2 parts:
* headless -- working without the GUI (e.g. for testing in the CI environment without X11)
* headfull -- full GUI tests (project uses `Canvas` from the `java.awt`)

# Continuous Integration
## Java
Build of the project (multiple JARs) is done, see [.travis.yml](.travis.yml).
