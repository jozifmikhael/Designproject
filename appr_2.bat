@echo off
cd C:\Program Files\Java\jdk-15\bin\javaw.exe

echo Starting compile?

javaw.exe -Dfile.encoding=Cp1252 -classpath "C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\bin;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\commons-math3-3.5-bin.zip;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\guava-18.0.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\json-simple-1.1.1.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\commons-math3-3.5\commons-math3-3.5.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\cloudsim-3.0.3.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\cloudsim-3.0.3-sources.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\cloudsim-examples-3.0.3.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\cloudsim-examples-3.0.3-sources.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\commons-math3-3.5-bin.zip;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\guava-18.0.jar;C:\Users\kumar\eclipse-workspace-f2020\iFogSim_v1\jars\json-simple-1.1.1.jar" -XX:+ShowCodeDetailsInExceptionMessages org.fog.test.perfeval.VRGameFog

pause

echo Successfully Compiled
java VRGameFog
pause