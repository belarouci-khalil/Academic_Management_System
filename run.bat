@echo off
echo ========================================
echo   SYSTEME D'INFORMATION ACADEMIQUE
echo ========================================
echo.

echo Verification de MongoDB...
echo (Assurez-vous que MongoDB est demarre)
echo.

echo Compilation du projet...
call mvn compile

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR: La compilation a echoue!
    echo Verifiez que Maven est installe et dans le PATH
    echo.
    pause
    exit /b 1
)

echo.
echo Lancement de l'interface...
call mvn exec:java -Dexec.mainClass="com.example.academic.ui.LoginForm"

pause

