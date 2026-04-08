@echo off
REM =========================================================
REM  Dungeon Crawler — Build & Run Script (Windows)
REM  JavaFX SDK 26 bundled — no Maven or internet needed
REM =========================================================
SETLOCAL

SET JAVAFX_LIB=openjfx-26_windows-x64_bin-sdk\javafx-sdk-26\lib
SET JDK_BIN=C:\Program Files\Java\latest\jdk-26\bin
SET OUT_DIR=out

REM ----- Collect source files -----
SET SRC=^
 src\utils\Constants.java ^
 src\utils\SaveManager.java ^
 src\utils\LeaderboardDB.java ^
 src\utils\WebUtil.java ^
 src\utils\GameWebServer.java ^
 src\world\Tile.java ^
 src\world\MapGrid.java ^
 src\world\DungeonGenerator.java ^
 src\entities\Combatant.java ^
 src\entities\Entity.java ^
 src\entities\Player.java ^
 src\entities\Enemy.java ^
 src\ai\Pathfinding.java ^
 src\ai\AIController.java ^
 src\engine\InputHandler.java ^
 src\engine\GameStateManager.java ^
 src\engine\Renderer.java ^
 src\engine\GameLoop.java ^
 src\main\MainApp.java ^
 src\module-info.java

echo =====================================================
echo  Dungeon Crawler Build ^& Launch
echo =====================================================
echo.

REM ----- Compile -----
echo [1/2] Compiling...
IF NOT EXIST "%OUT_DIR%" mkdir "%OUT_DIR%"

"%JDK_BIN%\javac.exe" ^
    --module-path "%JAVAFX_LIB%;lib" ^
    --add-modules javafx.controls,javafx.graphics,java.sql,java.net.http,jdk.httpserver ^
    -d "%OUT_DIR%" ^
    %SRC%

IF ERRORLEVEL 1 (
    echo.
    echo [ERROR] Compilation failed. Check error messages above.
    pause
    EXIT /B 1
)

echo [OK] Compilation successful.
echo.

REM ----- Run -----
echo [2/2] Launching game...
"%JDK_BIN%\java.exe" ^
    --module-path "%JAVAFX_LIB%;lib" ^
    --add-modules javafx.controls,javafx.graphics,java.sql,java.net.http,jdk.httpserver ^
    --enable-native-access=javafx.graphics ^
    -cp "%OUT_DIR%" ^
    main.MainApp

echo.
echo Press any key to exit...
pause
ENDLOCAL
