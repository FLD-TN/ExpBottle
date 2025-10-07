@echo off
echo Building ExpBottle Plugin...
echo.

mvn clean install

if %ERRORLEVEL% == 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo Plugin file: target\ExpBottle-1.0.0.jar
    echo ========================================
    echo.
    
    if exist "target\ExpBottle-1.0.0.jar" (
        echo File size: 
        dir "target\ExpBottle-1.0.0.jar" | findstr "ExpBottle-1.0.0.jar"
    )
) else (
    echo.
    echo ========================================
    echo Build failed! Check the output above.
    echo ========================================
)

pause