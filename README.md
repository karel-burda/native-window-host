![Version](https://img.shields.io/badge/version-0.3.0-green.svg)
[![License](https://img.shields.io/badge/license-MIT_License-green.svg?style=flat)](LICENSE)
[![Build Status](https://travis-ci.org/karel-burda/native-window-embedder.svg?branch=master)](https://travis-ci.org/karel-burda/native-window-embedder)

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
Java project is built using CMake: [CMakeLists.txt](java/windows/CMakeLists.txt).

# Unit Tests
TODO
