$ErrorActionPreference = "Continue"

$mavenHome = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.3\plugins\maven\lib\maven3"
$javaExe   = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.3\jbr\bin\java.exe"
$boot      = Join-Path $mavenHome "boot\plexus-classworlds-2.9.0.jar"
$m2Conf    = Join-Path $mavenHome "bin\m2.conf"
$projDir   = (Resolve-Path "$PSScriptRoot\..").Path

& $javaExe `
    -classpath $boot `
    "-Dclassworlds.conf=$m2Conf" `
    "-Dmaven.home=$mavenHome" `
    "-Dmaven.multiModuleProjectDirectory=$projDir" `
    org.codehaus.plexus.classworlds.launcher.Launcher `
    -DskipTests `
    compile
