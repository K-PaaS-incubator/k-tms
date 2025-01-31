FROM ubuntu:22.04

SHELL ["/bin/bash", "-c"]

COPY cloudtas/opt /opt
COPY cloudtas/conf /conf
COPY cloudtas/logs /logs

RUN chmod 755 /logs/upfile/*

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

RUN apt update
RUN apt -y install net-tools
RUN apt -y install vim
RUN apt -y install wget
RUN apt -y install perl
RUN apt -y install iputils-ping
RUN apt -y install expect
RUN apt -y install libelf1
RUN apt -y install libyaml-0-2
RUN apt -y install libhyperscan5
RUN apt -y install libssh2-1
RUN apt -y install language-pack-ko
RUN apt -y install htop
RUN apt -y install rdate
RUN apt -y install ntp

COPY cloudtas/tastmp/etc/rsyslog.d/50-default.conf /etc/rsyslog.d/50-default.conf
COPY cloudtas/tastmp/etc/profile /etc/profile
COPY cloudtas/tastmp/etc/magic.mgc /etc/magic.mgc

RUN mkdir -p /logs/TESS/log/log
RUN mkdir -p /logs/TESS/log/core
RUN mkdir -p /logs/TESS/log/backup
RUN mkdir -p /logs/TESS/log/traffic
RUN mkdir -p /logs/TESS/log/session
RUN ln -s /opt/TESS/6.0.0 /opt/TESS/current
RUN ln -s /conf/TESS/6.0.0 /conf/TESS/current
RUN ln -s /opt/TESS/current/sensor /home/sensor
RUN ln -s /conf/TESS/current/sensor/cfg /home/sensor/cfg
RUN ln -s /conf/TESS/current/sensor/rule /home/sensor/rule
RUN ln -s /conf/TESS/current/sensor/CertKeys /home/sensor/CertKeys
RUN ln -s /logs/TESS/log /home/sensor/log
RUN mkdir -p /var/log/
RUN touch /var/log/wtmp
RUN touch /var/log/utmp
RUN touch /var/log/btmp

CMD source /etc/profile

RUN echo 'root:No1ids@#' | chpasswd

COPY simple-entrypoint.sh /tmp

RUN mkdir /var/empty

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]

ENTRYPOINT ["/tmp/simple-entrypoint.sh"]

