cd home-manager & ^
git submodule update --remote & ^
gradlew clean bootjar -b build.gradle & ^
cd ..  & ^
docker build --no-cache --tag home-manager:0.1 .

