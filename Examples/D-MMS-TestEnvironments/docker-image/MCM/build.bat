git submodule update  & ^
cd mcm & ^
gradlew clean bootjar -b build.gradle & ^
cd .. & ^
docker build --no-cache --tag mcm:0.1 .
