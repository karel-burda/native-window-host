#include <iostream>
#include <string>

#include <windows.h>

int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE /*hPrevInstance*/, PWSTR /*pCmdLine*/, const int nCmdShow)
{
    const char windowClassName[] = "win32-demo-helper-app";

    WNDCLASSEX windowClass = { 0 } ;
    windowClass.cbSize = sizeof(WNDCLASSEX);
    windowClass.style = 0;
    windowClass.lpfnWndProc = DefWindowProc;
    windowClass.cbClsExtra = 0;
    windowClass.cbWndExtra = 0;
    windowClass.hInstance = hInstance;
    windowClass.hIcon = nullptr;
    windowClass.hCursor = nullptr;
    windowClass.hbrBackground = static_cast<HBRUSH>(GetStockObject(DKGRAY_BRUSH));
    windowClass.lpszMenuName = nullptr;
    windowClass.lpszClassName = windowClassName;
    windowClass.hIconSm = nullptr;

    if (RegisterClassEx(&windowClass) == 0)
    {
        std::cerr << "Failed to register class, error code: " << std::to_string(GetLastError()) << std::endl;

        return 1;
    }

    const HWND hwnd = CreateWindow(windowClassName,
                                  "win32-demo-helper-app",
                                  WS_POPUPWINDOW,
                                  CW_USEDEFAULT, CW_USEDEFAULT,
                                  800, 600,
                                  nullptr,
                                  nullptr,
                                  hInstance,
                                  nullptr);

    if (hwnd == nullptr)
    {
        std::cerr << "Window creation failed, error code: " << std::to_string(GetLastError()) << std::endl;

        return 2;
    }

    ShowWindow(hwnd, SW_SHOW);
    UpdateWindow(hwnd);
    MSG msg = {0};
    BOOL messageSuccess = FALSE;
    while ((messageSuccess = GetMessage(&msg, nullptr, 0, 0)) != 0)
    {
        if (messageSuccess == -1)
        {
            // handle the error and possibly exit
        }
        else
        {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }

    return msg.wParam;
}
