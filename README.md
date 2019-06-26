![Version](https://img.shields.io/badge/csharp-0.0.0-blue.svg)
![Version](https://img.shields.io/badge/java-0.9.1-blue.svg)
[![License](https://img.shields.io/badge/license-MIT_License-blue.svg?style=flat)](LICENSE)
[![Build Status](https://travis-ci.org/karel-burda/native-window-host.svg?branch=develop)](https://travis-ci.org/karel-burda/native-window-host)

## Introduction
`native-window-host` demonstrates Proof of Concept how can native windows (e.g win32 window on Windows) might be embedded/hosted (hosted) in Java and C# GUI elements.

Java concept uses `Java Native Access` and C# uses `HwndHost` from the WPF framework.

See [java](java) for Java implementation and [C#](csharp) for C# implementation with WPF.

**Currently, only Windows concept is implemented.**

### Java
Java implementation uses the JNA and `Canvas` component from the AWT. `Canvas` is used because it's a heavy-weight component (it has its own handle, e.g. `HWND` on Windows) and thus has ability to become a parent of native system window.

The basic idea is that when this inherited `Canvas` component (i.e. `NativeWindowHost`) become parent window (`SetParent(...)` call in Windows), all system messages that are sent to the `Canvas` are also sent to the child (the native window). Because of this parent-child relationship, the GUI-related things like moving, resizing, work (more or less) out of the box.

Demo is located at [java/windows/source/com/github/karel-burda/native-window-host/demo/Main.java](java/windows/source/com/github/karel-burda/native-window-host/demo/Main.java).

### C#
The concept is using the `HwndHost` component on Windows from WPF that effectively works well without much code needed.

The only catch is to use set the correct window style to the native window (`WS_CHILD`).

**Currently not implemented.**

## Usage
C# and Java uses helper app that creates and shows dummy window with specified class name, this resides in [common/windows/win32-demo-helper-app](common/windows/win32-demo-helper-app).

Build process is meant to be performed like this:
```cmake
cmake -Bbuild -H.
cmake --build build
```

### Java
Java project is built using CMake: [CMakeLists.txt](java/windows/CMakeLists.txt).

Projects is divided into separate targets that are built (and also run) using CMake:
  * `native-window-host-java.jar` -- makes the core functionality, depends on the JNA
  * `native-window-host-java-demo.jar` -- demonstration example

The build itself is being executed using:
```cmake
cmake -Bbuild -H. -DJNA_PATH:PATH=<absolute-path-to-jna> -DJDK_VERSION:STRING=<version>
cmake --build build
```
where `<absolute-path-to-jna>` is absolute path to the directory where `jna.jar` and `jna-platform.jar` reside.
`<version>` is an optional parameter of the JDK used, e.g. `1.8`.

Demo might be run using (after the generation step is done, you can skip the `cmake --build build`):
```cmake
cmake --build build --target run-demo
```

Core class providing functionality is [NativeWindowHost.java](java/windows/source/com/github/karel-burda/native-window-host/NativeWindowHost.java)

### C#
To be done.

## Examples
For full use cases, see [Main.java](java/windows/source/com/github/karel-burda/native-window-host/demo/Main.java).

### Java
```java
import com.github.karelburda.nativewindowhost.NativeWindowHost;

public class Main {
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // using variant of constructor where window class name is known (the other one is with HWND)
        final NativeWindowHost host = new NativeWindowHost("win32-demo-helper-app");
        host.setSize(frame.getWidth(), frame.getHeight());
        host.setVisible(true);

        frame.add(host, BorderLayout.CENTER);

        // upon closing, we may want to "release" the native handle from the embedder
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                host.release();

                System.exit(0);
            }
        });
    }
}
```

## Tests
Currently no automatic tests, because of difficult testing in headless (mode without window managers from the OS) environment on the CI.

### Java
To perform manual integration test, you can run the demo. Steps are:
  1. Run the `win32-demo-helper-app`
  2. Run the demo (e.g. using CMake to execute the JAR)

## Continuous Integration
### Java
Continuous Integration is now being run on Linux on Travis: https://travis-ci.org/karel-burda/native-window-host.

The project is using these jobs:
  * `win32-demo-helper-app -- windows, release, msvc, 32-bit`
  * `native-window-host, example -- linux, jdk 1.8, 64-bit`

Build of the project, core and demo, is performed, see [.travis.yml](.travis.yml).
