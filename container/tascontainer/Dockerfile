FROM ubuntu:22.04

SHELL ["/bin/bash", "-c"]

ARG DEBIAN_FRONTEND=noninteractive

COPY cloudtas.deb /root/
COPY cloudtas_preinst.sh /root/

RUN apt-get update
RUN apt-get install -y sudo apt-utils init

RUN source /root/cloudtas_preinst.sh

RUN  echo "source \/etc\/profile" >> ~/.bashrc

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]

COPY tascloud_install.sh /usr/local/bin/
COPY simple-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["simple-entrypoint.sh"]

