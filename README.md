![Version](https://img.shields.io/badge/version-0.8.0-green.svg)
[![License](https://img.shields.io/badge/license-MIT_License-green.svg?style=flat)](LICENSE)
[![Build Status](https://travis-ci.org/karel-burda/native-window-embedder.svg?branch=develop)](https://travis-ci.org/karel-burda/native-window-embedder)

## Introduction
`native-windows-embedder` demonstrates Proof of Concept how can native windows (e.g win32 window on Windows) might be embedded (hosted) in Java and C# GUI elements.

Java concept uses `Java Native Access` and C# uses `HwndHost` from the WPF framework.

See [java](java) for Java implementation and [C#](csharp) for C# implementation with WPF.

**Currently, only Windows concept is implemented.**

### Java
Java implementation uses the JNA and `Canvas` component from the AWT. `Canvas` is used because it's a heavy-weight component (it has its own handle, e.g. `HWND` on Windows) and thus has ability to become a parent of native system window.

The basic idea is that when this inherited `Canvas` component become parent window (`SetParent(...)` call in Windows), all system messages that are sent to the `Canvas` are also sent to the child (the native window). Because of this parent-child relationship, the GUI-related things like moving, resizing, work (more or less) out of the box.

### C#
The concept is using the `HwndHost` component on Windows from WPF that effectively works well without much code needed.

The only catch is to use set the correct window style to the native window (`WS_CHILD`).

**Currently not implemented.**

## Usage
### Java
Java project is built using CMake: [CMakeLists.txt](java/windows/CMakeLists.txt).

Projects is divided into separate targets that are built (and also run) using CMake:
  * `native-window-embedder-java.jar` -- makes the core functionality, depends on the JNA
  * `native-window-embedder-java-demo.jar` -- demonstration example
  * `win32-demo-helper-app` -- creates win32 window programmatically and shows it on the screen

So the build itself is being executed using:
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

Core class providing functionality is [NativeWindowEmbedder.java](java/windows/source/com/github/karel-burda/native-window-embedder/NativeWindowEmbedder.java)

## Examples
For full use cases, see [Main.java](java/windows/source/com/github/karel-burda/native-window-embedder/demo/Main.java).

```java
import com.github.karelburda.nativewindowembedder.NativeWindowEmbedder;

public class Main {
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // using variant of cunstructor where window class name is known (the other one is when HWND is known)
        final NativeWindowEmbedder embedder = new NativeWindowEmbedder("win32-demo-helper-app");
        embedder.setSize(frame.getWidth(), frame.getHeight());
        embedder.setVisible(true);

        frame.add(embedder, BorderLayout.CENTER);

        // upon closing, we may want to "release" the native handle from the embedder
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                embedder.release();

                System.exit(0);
            }
        });
    }
}
```

## Tests
Currently none, because of difficult testing in headless (mode without window managers from the OS) environment on the CI.

## Continuous Integration
### Java
Continuous Integration is now being run on Linux on Travis: https://travis-ci.org/karel-burda/native-window-embedder.

The project is using these jobs:
  * `native-window-embedder, exaple -- linux, jdk 1.8, 64-bit`

Build of the project, core and demo, is performed, see [.travis.yml](.travis.yml).
