#-------------------------------------------------------------
# Copyright (c) All rights reserved.
#
# $id:$
#-------------------------------------------------------------
# vim: ts=4 sw=4 :

ifndef TOP
TOP   = $(shell cd $(dir $(firstword \
        $(foreach d, . .. ../.. ../../.. ../../../.. ../../../../.. ../../../../../..,\
        $(wildcard $(addsuffix /@TOP,$(d)))))) && pwd)
export TOP
endif

#-------------------------------------------------------------
# INCLUDE $(TOP)/.config
#-------------------------------------------------------------
ifeq ($(TOP)/.config,$(wildcard $(TOP)/.config))
-include $(TOP)/.config
endif

#-------------------------------------------------------------
# USER DEFINED LOCAL VARIABLES
#-------------------------------------------------------------
DUMPVAR         = true
FINAL           = true
DEFINES         = -DTLS -DHAVE_AF_PACKET \
                  -DHAVE_LINUX_IF_ETHER_H \
                  -DHAVE_LINUX_IF_PACKET_H \
                  -DHAVE_LINUX_IF_ARP_H \
                  -DHAVE_LINUX_FILTER_H \
                  -DHAVE_SYS_MMAN_H \
                  -DHAVE_POLL_H \
                  -DHAVE_NETINET_IN_H \
                  -DHAVE_NET_IF_H \
                  -DHAVE_SYS_IOCTL_H \
                  -DHAVE_INTTYPES_H \
                  -DHAVE_ERRNO_H \
                  -DHAVE_SYSLOG_H \
                  -DHAVE_LIMITS_H \
                  -DHAVE_SYS_STAT_H \
                  -DHAVE_SIGNAL_H \
                  -DHAVE_NETDB_H \
                  -DHAVE_STDARG_H \
                  -DHAVE_FCNTL_H \
                  -DHAVE_PCAP_PCAP_H \
                  -DHAVE_SYS_SYSCALL_H \
                  -DHAVE_GETOPT_H \
                  -DHAVE_ARPA_INET_H \
                  -DPROFILING \
                  -DCONFIG_INFOSEC_NETHOOK \
                  -DDECRYPT_SSL \
                  -DBRG_SHA \
                  -DPCRE_HAVE_JIT \
                  -D_NT_OS_TYPE=_NT_OS_TYPE_LINUX -D_NT_HOST_CPU_POINTER_SIZE=_NT_HOST_CPU_POINTER_SIZE_64 -D__EXPORTED_HEADERS__\
				  
INCLUDES        = 
LIBDIRS         =
LIBS				= -Xlinker --start-group \
                      $(TOP)/lib/libpcap.a \
                      $(TOP)/lib/libhtp.a \
                      $(TOP)/lib/libTSCommon.a \
                      $(TOP)/lib/libTSEnvman.a \
                      -Xlinker --end-group \
					  -ldl -lpcre -lpthread -lz -lmagic -lyaml

CFLAGS          = 
CPPFLAGS        = 
LDFLAGS         = 

THREAD_SAFE     = true

SRCS            =  suricata.c \
				   util-debug.c \
				   util-error.c \
				   util-signal.c \
				   detect-engine.c \
				   util-strlcatu.c \
				   conf.c \
				   source-nfq.c \
				   source-ipfw.c \
				   source-pcap.c \
				   source-pcap-file.c \
				   source-af-packet.c \
				   source-pfring.c \
				   source-erf-file.c \
				   source-erf-dag.c \
				   source-napatech.c \
				   stream-tcp.c \
				   detect.c \
				   respond-reject.c \
				   alert-fastlog.c \
				   alert-debuglog.c \
				   alert-prelude.c \
				   alert-syslog.c \
				   alert-unified2-alert.c \
				   alert-pcapinfo.c \
				   log-droplog.c \
				   output-json-drop.c \
				   output-json.c \
				   log-httplog.c \
				   output-json-http.c \
				   log-tlslog.c \
				   output-json-tls.c \
				   output-json-ssh.c \
				   log-pcap.c \
				   log-file.c \
				   output-json-file.c \
				   log-filestore.c \
				   log-dnslog.c \
				   output-json-dns.c \
				   output-json-alert.c \
				   output-packet.c \
				   output-tx.c \
				   output-file.c \
				   output-filedata.c \
				   tm-modules.c \
				   source-nflog.c \
				   conf-yaml-loader.c \
				   util-device.c \
				   runmode-af-packet.c \
				   util-strlcpyu.c \
				   util-conf.c \
				   util-pidfile.c \
				   util-daemon.c \
				   util-privs.c \
				   util-running-modes.c \
				   runmodes.c \
				   runmode-unittests.c \
				   util-threshold-config.c \
				   util-ioctl.c \
				   util-misc.c \
				   util-mpm.c \
				   app-layer.c \
				   util-host-os-info.c \
				   defrag.c \
				   defrag-hash.c \
				   tm-queuehandlers.c \
				   util-storage.c \
				   util-cidr.c \
				   detect-parse.c \
				   counters.c \
				   reputation.c \
				   util-proto-name.c \
				   detect-engine-tag.c \
				   detect-engine-threshold.c \
				   detect-engine-address.c \
				   detect-engine-port.c \
				   app-layer-htp.c \
				   util-time.c \
				   detect-fast-pattern.c \
				   util-cpu.c \
				   tmqh-packetpool.c \
				   host.c \
				   flow.c \
				   util-classification-config.c \
				   util-reference-config.c \
				   util-action.c \
				   app-layer-parser.c \
				   util-magic.c \
				   util-decode-asn1.c \
				   util-coredump-config.c \
				   unix-manager.c \
				   flow-manager.c \
				   tm-queues.c \
				   tm-threads.c \
				   output.c \
				   flow-timeout.c \
				   util-debug-filters.c \
				   util-enum.c \
				   util-syslog.c \
				   flow-util.c \
				   decode.c \
				   packet-queue.c \
				   detect-engine-mpm.c \
				   detect-engine-siggroup.c \
				   util-var-name.c \
				   detect-engine-sigorder.c \
				   util-byte.c \
				   detect-content.c \
				   detect-uricontent.c \
				   detect-engine-iponly.c \
				   detect-engine-uri.c \
				   detect-engine-hcbd.c \
				   detect-engine-hhd.c \
				   detect-engine-hrhd.c \
				   detect-engine-hmd.c \
				   detect-engine-hcd.c \
				   detect-engine-hrud.c \
				   detect-engine-file.c \
				   detect-engine-hua.c \
				   detect-engine-hhhd.c \
				   detect-engine-hrhhd.c \
				   detect-engine-dns.c \
				   detect-engine-hsbd.c \
				   detect-engine-hsmd.c \
				   util-path.c \
				   detect-engine-hscd.c \
				   util-checksum.c \
				   decode-sll.c \
				   decode-ethernet.c \
				   decode-ppp.c \
				   decode-raw.c \
				   runmode-unix-socket.c \
				   stream-tcp-reassemble.c \
				   stream-tcp-sack.c \
				   stream.c \
				   util-pool-thread.c \
				   stream-tcp-inline.c \
				   detect-engine-alert.c \
				   detect-engine-analyzer.c \
				   detect-replace.c \
				   detect-filestore.c \
				   detect-dns-query.c \
				   util-var.c \
				   detect-engine-state.c \
				   detect-engine-proto.c \
				   detect-engine-payload.c \
				   detect-flowvar.c \
				   util-file.c \
				   util-hashlist.c \
				   detect-sid.c \
				   detect-priority.c \
				   detect-rev.c \
				   detect-classtype.c \
				   detect-reference.c \
				   detect-tag.c \
				   detect-threshold.c \
				   detect-metadata.c \
				   detect-msg.c \
				   detect-ack.c \
				   detect-seq.c \
				   detect-pcre.c \
				   detect-depth.c \
				   detect-nocase.c \
				   detect-rawbytes.c \
				   detect-bytetest.c \
				   detect-bytejump.c \
				   detect-sameip.c \
				   detect-geoip.c \
				   detect-l3proto.c \
				   detect-ipproto.c \
				   detect-within.c \
				   detect-distance.c \
				   detect-offset.c \
				   detect-flow.c \
				   detect-window.c \
				   detect-rpc.c \
				   detect-ftpbounce.c \
				   detect-isdataat.c \
				   detect-id.c \
				   detect-dsize.c \
				   detect-flowint.c \
				   detect-pktvar.c \
				   detect-noalert.c \
				   detect-flowbits.c \
				   detect-engine-event.c \
				   detect-ipopts.c \
				   detect-flags.c \
				   detect-fragbits.c \
				   detect-fragoffset.c \
				   detect-gid.c \
				   detect-mark.c \
				   detect-csum.c \
				   detect-stream_size.c \
				   detect-ttl.c \
				   detect-tos.c \
				   detect-itype.c \
				   detect-icode.c \
				   detect-icmp-id.c \
				   detect-icmp-seq.c \
				   detect-dce-iface.c \
				   detect-dce-opnum.c \
				   detect-dce-stub-data.c \
				   detect-http-cookie.c \
				   detect-http-method.c \
				   detect-http-stat-msg.c \
				   detect-tls.c \
				   detect-tls-version.c \
				   detect-urilen.c \
				   detect-detection-filter.c \
				   detect-http-header.c \
				   detect-http-raw-header.c \
				   detect-http-client-body.c \
				   detect-http-server-body.c \
				   detect-http-uri.c \
				   detect-http-raw-uri.c \
				   detect-asn1.c \
				   detect-ssh-proto-version.c \
				   detect-ssh-software-version.c \
				   detect-ssl-state.c \
				   detect-http-stat-code.c \
				   detect-ssl-version.c \
				   detect-byte-extract.c \
				   detect-file-data.c \
				   detect-pkt-data.c \
				   detect-filename.c \
				   detect-fileext.c \
				   detect-filemagic.c \
				   detect-filemd5.c \
				   detect-filesize.c \
				   detect-app-layer-event.c \
				   detect-http-ua.c \
				   detect-http-hh.c \
				   detect-http-hrh.c \
				   detect-luajit.c \
				   detect-iprep.c \
				   detect-app-layer-protocol.c \
				   respond-reject-libnet11.c \
				   util-print.c \
				   util-logopenfile.c \
				   util-buffer.c \
				   util-crypt.c \
				   app-layer-dns-common.c \
				   runmode-pcap.c \
				   runmode-pcap-file.c \
				   runmode-pfring.c \
				   runmode-nfq.c \
				   runmode-ipfw.c \
				   runmode-erf-file.c \
				   runmode-erf-dag.c \
				   runmode-napatech.c \
				   runmode-nflog.c \
				   runmode-tile.c \
				   util-affinity.c \
				   util-mpm-wumanber.c \
				   util-mpm-b2g.c \
				   util-mpm-b3g.c \
				   util-mpm-b2gc.c \
				   util-mpm-b2gm.c \
				   util-mpm-ac.c \
				   util-mpm-ac-bs.c \
				   util-mpm-ac-gfbs.c \
				   util-mpm-ac-tile.c \
				   util-host-info.c \
				   app-layer-detect-proto.c \
				   app-layer-events.c \
				   app-layer-htp-mem.c \
				   util-radix-tree.c \
				   util-ip.c \
				   util-pool.c \
				   util-fix_checksum.c \
				   decode-ipv4.c \
				   decode-ipv6.c \
				   defrag-config.c \
				   defrag-queue.c \
				   util-random.c \
				   util-hash-lookup3.c \
				   tmqh-simple.c \
				   tmqh-nfq.c \
				   tmqh-flow.c \
				   tmqh-ringbuffer.c \
				   detect-engine-apt-event.c \
				   host-storage.c \
				   flow-storage.c \
				   detect-engine-address-ipv4.c \
				   detect-engine-address-ipv6.c \
				   util-rule-vars.c \
				   app-layer-htp-body.c \
				   util-spm.c \
				   app-layer-htp-file.c \
				   app-layer-htp-libhtp.c \
				   util-ringbuffer.c \
				   pkt-var.c \
				   host-queue.c \
				   flow-queue.c \
				   flow-hash.c \
				   util-hash.c \
				   app-layer-ssl.c \
				   app-layer-smb.c \
				   app-layer-dcerpc.c \
				   app-layer-dcerpc-udp.c \
				   app-layer-ftp.c \
				   app-layer-ssh.c \
				   app-layer-smtp.c \
				   app-layer-dns-udp.c \
				   app-layer-dns-tcp.c \
				   defrag-timeout.c \
				   host-timeout.c \
				   decode-vlan.c \
				   util-spm-bm.c \
				   detect-engine-content-inspection.c \
				   decode-pppoe.c \
				   app-layer-protos.c \
				   flow-bit.c \
				   flow-var.c \
				   detect-engine-dcepayload.c \
				   util-spm-bs.c \
				   util-runmodes.c \
				   util-bloomfilter.c \
				   decode-tcp.c \
				   decode-udp.c \
				   decode-icmpv4.c \
				   decode-gre.c \
				   decode-sctp.c \
				   decode-icmpv6.c \
				   data-queue.c \
				   util-spm-bs2bm.c \
				   app-layer-tls-handshake.c \
				   decode-teredo.c \
				   util-decode-der.c \
				   util-decode-der-get.c \
				   util-profiling.c \
				   util-profiling-rules.c \
				   util-profiling-keywords.c \


HEADERS         = none

TARGET          = sensor

INSTALL_DIR		= sensor

include $(TOP)/rules/common.mk
