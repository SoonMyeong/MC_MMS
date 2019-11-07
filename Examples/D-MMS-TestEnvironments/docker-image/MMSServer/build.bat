SET CURPATH=%cd%

cd ../../../../VDESDaemon & ^
gradlew clean bootjar -b build.gradle & ^
cd .. & mvn clean install & ^
copy VDESDaemon\build\libs\*.jar MMSServer\target\ & ^
echo D | xcopy MMSServer\MMS-configuration MMSServer\target\MMS-configuration & ^
copy %CURPATH%\start_mms.sh MMSServer\target\start_mms.sh & ^
copy %CURPATH%\Dockerfile MMSServer\target\Dockerfile & ^
cd MMSServer\target & ^
docker build --no-cache --tag mmsserver:0.1 .  & ^
cd %CURPATH%
