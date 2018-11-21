FROM ccr.ccs.tencentyun.com/qcloud/centos
MAINTAINER tsf
#install jdk and jar
ADD jdk-8u181-linux-x64.tar.gz /usr/java/
#COPY rule-engine-action-0.0.1-SNAPSHOT.jar /data/tsf/
ADD rule-engine-action-0.0.1-SNAPSHOT.tar.gz /data/tsf
COPY run.sh /data/tsf/

#jdk enviroment
ENV JAVA_HOME=/usr/java/jdk1.8.0_181
ENV JRE_HOME=/usr/java/jdk1.8.0_181/jre
ENV CLASSPATH=$JAVA_HOME/lib:$JAVA_HOME/jre/lib
ENV PATH=$JAVA_HOME/bin:$PATH

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone

#run.sh
CMD ["sh", "-c", "cd /data/tsf; sh run.sh rule-engine-action-0.0.1-SNAPSHOT.jar /data/tsf"]

