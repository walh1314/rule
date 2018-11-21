FROM ccr.ccs.tencentyun.com/qcloud/centos
MAINTAINER tsf
#install jdk and jar
#RUN yum install wget
#RUN wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" -P /usr/java/ http://download.oracle.com/otn-pub/java/jdk/8u181-b13/96a7b8442fe848ef90c96a2fad6ed6d1/jdk-8u181-linux-x64.tar.gz
ADD jdk-8u181-linux-x64.tar.gz /usr/java/
#COPY rule-engine-front-0.0.1-SNAPSHOT.jar /data/tsf/
ADD rule-engine-front-0.0.1-SNAPSHOT.tar.gz /data/tsf

COPY run.sh /data/tsf/

#jdk enviroment
ENV JAVA_HOME=/usr/java/jdk1.8.0_181
ENV JRE_HOME=/usr/java/jdk1.8.0_181/jre
ENV CLASSPATH=$JAVA_HOME/lib:$JAVA_HOME/jre/lib
ENV PATH=$JAVA_HOME/bin:$PATH

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone

#run.sh
CMD ["sh", "-c", "cd /data/tsf; sh run.sh rule-engine-front-0.0.1-SNAPSHOT.jar /data/tsf"]
