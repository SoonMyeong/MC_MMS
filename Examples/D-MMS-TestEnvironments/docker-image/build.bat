rem docker build
cd HomeManager & build.bat & ^
cd ../MCM & build.bat & ^
cd ../MNSDummy & build.bat & ^
cd ../MMSServer & build.bat & ^
cd .. & docker image ls
