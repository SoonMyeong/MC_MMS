FROM openjdk:8-jdk-alpine
EXPOSE 8088
COPY MMSServer.jar /app/mms/MMSServer.jar
COPY lib/ /app/mms/lib/
COPY start_mms.sh /app/mms/start_mms.sh
COPY MMS-configuration/ /app/mms/MMS-configuration/
WORKDIR /app/mms
CMD ["/app/mms/start_mns.sh"]