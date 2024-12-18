USE TESS;
CREATE TABLE TESS.BACKUP_FILE_LIST (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	TMFROM	DATETIME,
	TMTO	DATETIME,
	NTABLEDEL	INT,
	NTABLECHECKVALUE	INT,
	STRBACKUPFILEPATH	VARCHAR(256),
	TMREGDATE	DATETIME,
	CONSTRAINT BACKUP_FILE_LIST_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.FILTERVIEW_APPLICATION (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRFILTERVIEWNAME	VARCHAR(50),
	NTARGETCATEGORY	INT,
	NTARGETREFINDEX	INT,
	NTARGETCOLOR	INT,
	NTARGETENABLED	INT,
	NAPPTYPE	INT,
	NAPPTYPECOLOR	INT,
	NAPPTYPEENABLED	INT,
	STRSRCIP	VARCHAR(40),
	NSRCIPCOLOR	INT,
	NSRCIPENABLED	INT,
	STRDSTIP	VARCHAR(40),
	NDSTIPCOLOR	INT,
	NDSTIPENABLED	INT,
	NSRCPORT	INT,
	NSRCPORTCOLOR	INT,
	NSRCPORTENABLED	INT,
	NDSTPORT	INT,
	NDSTPORTCOLOR	INT,
	NDSTPORTENABLED	INT,
	LUSERINDEX	INT,
	MODDATE	DATETIME,
	STRDESCRIPTION	TEXT,
	CONSTRAINT FILTERVIEW_APPLICATION_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.FILTERVIEW_DETECTION (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRFILTERVIEWNAME	VARCHAR(50),
	NTARGETCATEGORY	INT,
	NTARGETREFINDEX	INT,
	NTARGETCOLOR	INT,
	NTARGETENABLED	INT,
	NATTACKPORT	INT,
	NATTACKPORTCOLOR	INT,
	NATTACKPORTENABLED	INT,
	NVICTIMPORT	INT,
	NVICTIMPORTCOLOR	INT,
	NVICTIMPORTENABLED	INT,
	STRATTACKNAME	VARCHAR(256),
	NATTACKNAMECOLOR	INT,
	NATTACKNAMEENABLED	INT,
	NATTACKTYPE	INT,
	NATTACKTYPECOLOR	INT,
	NATTACKTYPEENABLED	INT,
	NWINBOUND	INT,
	NWINBOUNDCOLOR	INT,
	NWINBOUNDENABLED	INT,
	STRATTACKIP	VARCHAR(40),
	NATTACKIPCOLOR	INT,
	NATTACKIPENABLED	INT,
	STRVICTIMIP	VARCHAR(40),
	NVICTIMIPCOLOR	INT,
	NVICTIMIPENABLED	INT,
	STRSEVERITY	VARCHAR(10),
	NSEVERITYCOLOR	INT,
	NSEVERITYENABLED	INT,
	LUSERINDEX	INT,
	MODDATE	DATETIME,
	STRDESCRIPTION	TEXT,
	NBLOCK	INT,
	NBLOCKCOLOR	INT,
	NBLOCKENABLED	INT,
	CONSTRAINT FILTERVIEW_DETECTION_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.FILTERVIEW_FILEMETA (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRFILTERVIEWNAME	VARCHAR(50),
	NTARGETCATEGORY	INT,
	NTARGETREFINDEX	INT,
	NTARGETCOLOR	INT,
	NTARGETENABLED	INT,
	STRFILENAME	VARCHAR(256),
	NFILENAMECOLOR	INT,
	NFILENAMEENABLED	INT,
	STRSRCIP	VARCHAR(40),
	NSRCIPCOLOR	INT,
	NSRCIPENABLED	INT,
	STRDSTIP	VARCHAR(40),
	NDSTIPCOLOR	INT,
	NDSTIPENABLED	INT,
	NSRCPORT	INT,
	NSRCPORTCOLOR	INT,
	NSRCPORTENABLED	INT,
	NDSTPORT	INT,
	NDSTPORTCOLOR	INT,
	NDSTPORTENABLED	INT,
	LUSERINDEX	INT,
	MODDATE	DATETIME,
	STRDESCRIPTION	TEXT,
	CONSTRAINT FILTERVIEW_FILEMETA_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.FILTERVIEW_SESSION (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRFILTERVIEWNAME	VARCHAR(50),
	NTARGETCATEGORY	INT,
	NTARGETREFINDEX	INT,
	NTARGETCOLOR	INT,
	NTARGETENABLED	INT,
	NSESSION	INT,
	NSESSIONCOLOR	INT,
	NSESSIONENABLED	INT,
	STRSERVERIP	VARCHAR(40),
	NSERVERIPCOLOR	INT,
	NSERVERIPENABLED	INT,
	STRCLIENTIP	VARCHAR(40),
	NCLIENTIPCOLOR	INT,
	NCLIENTIPENABLED	INT,
	LUSERINDEX	INT,
	MODDATE	DATETIME,
	STRDESCRIPTION	TEXT,
	CONSTRAINT FILTERVIEW_SESSION_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.POLICY_AUDITSET (
	LAUDITSETINDEX	INT,
	LTYPE1	INT,
	LTYPE2	INT,
	STRCONTENT	VARCHAR(512),
	LWARNINGINDEX	INT,
	STRSMSCONTENT	VARCHAR(512),
	NAPPLY	INT
);

CREATE TABLE TESS.POLICY_EXCEPTION (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	LVIOCODE	INT,
	NCLASSTYPE	INT,
	LSRCNETWORKINDEX	INT,
	LDSTNETWORKINDEX	INT,
	STRSRCIPFROM	VARCHAR(40),
	STRSRCIPTO	VARCHAR(40),
	STRDSTIPFROM	VARCHAR(40),
	STRDSTIPTO	VARCHAR(40),
	NPROTOCOL	INT,
	NSPORT	INT,
	NDPORT	INT,
	NDETECT	INT,
	NCHKVIOCODE	INT,
	NCHKSRCNETWORK	INT,
	NCHKDSTNETWORK	INT,
	NCHKSRCIP	INT UNSIGNED,
	NCHKDSTIP	INT UNSIGNED,
	NCHKPROTOCOL	INT,
	NCHKSPORT	INT,
	NCHKDPORT	INT,
	CONSTRAINT POLICY_EXCEPTION_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.POLICY_SIGNATURE (
	LCODE	INT NOT NULL,
	SALIVE	INT,
	SCLASSTYPE	INT,
	STRDESCRIPTION	VARCHAR(256),
	SSEVERITY	INT,
	STRSIGRULE	TEXT,
	LUSED	INT,
	LRESPONSE	INT,
	LTHRESHOLDTIME	INT,
	LTHRESHOLDNUM	INT,
	STRSERVICEGROUP	VARCHAR(256),
	CONSTRAINT POLICY_SIGNATURE_PK PRIMARY KEY (LCODE)
);

CREATE TABLE TESS.POLICY_USERSIG (
	LCODE	INT NOT NULL,
	SALIVE	INT,
	SCLASSTYPE	INT,
	STRDESCRIPTION	VARCHAR(256),
	SSEVERITY	INT,
	STRSIGRULE	TEXT,
	LUSED	INT,
	LRESPONSE	INT,
	LTHRESHOLDTIME	INT,
	LTHRESHOLDNUM	INT,
	STRSERVICEGROUP	VARCHAR(256),
	CONSTRAINT POLICY_USERSIG_PK PRIMARY KEY (LCODE)
);

CREATE TABLE TESS.POLICY_WARNINGSET (
	LWARNINGSETINDEX	INT NOT NULL,
	STRALARMTYPE	VARCHAR(500),
	NCOUNT	INT,
	NSECOND	INT,
	LMAILGROUP	INT,
	LSMSGROUP	INT,
	NTYPE	INT,
	NALARMTYPE	INT,
	CONSTRAINT POLICY_WARNINGSET_PK PRIMARY KEY (LWARNINGSETINDEX)
);

CREATE TABLE TESS.POLICY_YARA_RULE (
	LINDEX	INT UNSIGNED NOT NULL,
	LGROUPINDEX	INT,
	STRNAME	VARCHAR(256),
	STRMETA	TEXT,
	STRSTRINGS	TEXT,
	STRCONDITION	TEXT,
	SSEVERITY	INT,
	TMINSERT	DATETIME,
	TMUPDATE	DATETIME,
	LUSED	INT,
	CONSTRAINT POLICY_YARA_RULE_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.POLICY_YARA_USERRULE (
	LINDEX	INT UNSIGNED NOT NULL,
	LGROUPINDEX	INT,
	STRNAME	VARCHAR(256),
	STRMETA	TEXT,
	STRSTRINGS	TEXT,
	STRCONDITION	TEXT,
	SSEVERITY	INT,
	TMINSERT	DATETIME,
	TMUPDATE	DATETIME,
	LUSED	INT,
	CONSTRAINT POLICY_YARA_USERRULE_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.RESTORE_REQ_FILE_LIST (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRRESTOREFILEIDXLIST	VARCHAR(500),
	CONSTRAINT RESTORE_REQ_FILE_LIST_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.SENSOR_INBOUND (
	LSENSORINDEX	INT,
	STRENABLE	VARCHAR(5),
	STRTYPE	VARCHAR(10),
	STRNICINFO	TEXT,
	STRIPINFO	TEXT
);

CREATE TABLE TESS.SENSOR_INTEGRITY_FILEDATA (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRFILENAME	VARCHAR(100),
	CONSTRAINT SENSOR_INTEGRITY_FILEDATA_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.SENSOR_INTEGRITY_FILELIST (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRFILENAME	VARCHAR(100),
	NCHECK	INT,
	CONSTRAINT SENSOR_INTEGRITY_FILELIST_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.SESSION_SERVICE_DATA (
	LINDEX	INT,
	NPORT	INT,
	STRNAME	VARCHAR(256),
	NFILTEROPTION	INT,
	LTOTALPACKET	INT,
	NKEEPTIME	INT,
	NFUNC	INT,
	NRENEWOPTION	INT,
	NPERIOD	INT,
	LUSED	INT,
	CONSTRAINT SESSION_SERVICE_DATA_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.SYSTEM_AUDITHELP (
	LTYPE1	INT,
	LTYPE2	INT,
	STRCAUSE	VARCHAR(256),
	STRMANAGEMENT	VARCHAR(512),
	CONSTRAINT SYSTEM_AUDITHELP_PK PRIMARY KEY (LTYPE1, LTYPE2)
);

CREATE TABLE TESS.SYSTEM_CONFIG (
	IDX	INT,
	NAME	VARCHAR(256),
	VALUE	VARCHAR(256),
	DESCP	VARCHAR(512)
);

CREATE TABLE TESS.SYSTEM_CONSOLE_INTEGRITY (
	LINDEX	INT,
	STRFILENAME	VARCHAR(512),
	STRPATH	VARCHAR(512),
	FILE_HASHCODE	VARCHAR(512),
	NCHECK	INT,
	INDEX SYSTEM_CONSOLE_INTEGRITY_IDX (LINDEX)
);

CREATE TABLE TESS.SYSTEM_DB_CONFIG (
	NRAWPERIODIC	INT,
	NAUDITPERIODIC	INT,
	NDISKUSAGE	INT,
	NDISKWARN	INT,
	TMUPDATESTATE	DATETIME
);

CREATE TABLE TESS.SYSTEM_DB_BACKUP (
	NDAYCONFIGFLAG	INT,
	STRDAYBOOKTIME	VARCHAR(20),
	NDAYBOOKDAYBEFORE	INT,
	NDAYFILEFLAG	INT,
	STRDAYFILENAME	VARCHAR(100),
	NDAYTABLEDELETEFLAG	INT,
	NDAYTABLECHECKVALUE	INT,
	NMONTHCONFIGFLAG	INT,
	STRMONTHBOOKTIME	VARCHAR(20),
	NMONTHBOOKDAY	INT,
	NMONTHBOOKDAYBEFORE	INT,
	NMONTHFILEFLAG	INT,
	STRMONTHFILENAME	VARCHAR(100),
	NMONTHTABLEDELETEFLAG	INT,
	NMONTHTABLECHECKVALUE	INT,
	STRBACKUPPATHNAME	VARCHAR(100),
	NMINDRIVEFREESIZE	INT,
	TMUPDATESTATE	DATETIME
);

CREATE TABLE TESS.SYSTEM_CONFIG_OPTION (
	NUSETIMESYNC	    INT,
	STRTIMESERVERNAME	VARCHAR(100),
	NTIMESYNCPERIOD	    INT,
	STREMAILSERVER	    VARCHAR(100),
	STREMAILPORT	    VARCHAR(5),
    STREMAILSECURITY	VARCHAR(5),
	STREMAILID	        VARCHAR(50),
	STREMAILPWD	        VARCHAR(50),
	NSTARTINTEGRITY	    INT,
	NAUTOINTEGRITY	    INT,
	NAUTOINTEGRITYMIN	INT,
	MODDATE	            DATETIME
);

CREATE TABLE TESS.SYSTEM_NETIPBLOCK (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	LID	INT,
	DWFROMIP	INT UNSIGNED,
	DWTOIP	INT UNSIGNED,
	STRFROMIPV6	VARCHAR(40),
	STRTOIPV6	VARCHAR(40),
	BTYPE	INT,
	CONSTRAINT SYSTEM_NETIPBLOCK_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.SYSTEM_NETWORK (
	LNETWORKINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRNAME	VARCHAR(256),
	STRDESCRIPTION	VARCHAR(512),
	STYPE	INT,
	LVALUE1	INT,
	LVALUE2	INT,
	SZIP1	INT,
	DBLBANDWIDTH	INT,
	CONSTRAINT SYSTEM_NETWORK_PK PRIMARY KEY (LNETWORKINDEX)
);

CREATE TABLE TESS.SYSTEM_SENSOR (
	LINDEX	INT NOT NULL,
	STYPE	INT,
	STRNAME	VARCHAR(256),
	STRDESCRIPTION	VARCHAR(512),
	LPRIVATEIP	INT UNSIGNED,
	LPUBLICIP	INT UNSIGNED,
	SPORT	INT,
	SMODE	INT,
	NINBOUNDTYPE	INT,
	LUSERINDEX	INT,
	LGROUPID	INT,
	NSTARTINTEGRITY	INT,
	NAUTOINTEGRITY	INT,
	NAUTOINTEGRITYMIN	INT,
	STRPRIVATEIP	VARCHAR(40),
	STRPUBLICIP	VARCHAR(40),
	NHYPERSCANHITCOUNT	INT,
	SUSEBLACKLIST	INT,
	CONSTRAINT SYSTEM_SENSOR_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.SYSTEM_SENSOR_MONIP (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRFROMIP	VARCHAR(40),
	STRTOIP	VARCHAR(40),
	BTYPE	INT,
	CONSTRAINT SYSTEM_SENSOR_MONIP_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.SYSTEM_SIGCLASSTYPE (
	NCLASSTYPE	INT,
	STRNAME	VARCHAR(256)
);

CREATE TABLE TESS.SYSTEM_SIGHELP (
	LCODE	INT NOT NULL,
	STRTITLE	VARCHAR(256),
	STRATKTYPE	VARCHAR(256),
	BSEVERITY	INT,
	STRSUMMARY	VARCHAR(256),
	STRDESCRIPTION	TEXT,
	STRFALSEPOSITIVE	TEXT,
	STRSOLUTION	TEXT,
	STRREFERENCE	TEXT,
	STRCVEID	VARCHAR(50),
	STRBID	VARCHAR(256),
	STRVUL	VARCHAR(256),
	STRNOTVUL	VARCHAR(256),
	STRADDRSPOOF	VARCHAR(256),
	STRDETECT	VARCHAR(256),
	CONSTRAINT SYSTEM_SIGHELP_PK PRIMARY KEY (LCODE)
);

CREATE TABLE TESS.SYSTEM_TABLESPACE (
	LTABLESPACEINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRNAME	VARCHAR(256),
	CONSTRAINT SYSTEM_TABLESPACE_PK PRIMARY KEY (LTABLESPACEINDEX)
);

CREATE TABLE TESS.SYSTEM_USER (
	LUSERINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRID	VARCHAR(20) UNIQUE,
	STRPASSWD	VARCHAR(256),
	SGROUPTYPE	INT,
	STRNAME	VARCHAR(50),
	STRDESCRIPTION	VARCHAR(256),
	STRCOMPANY	VARCHAR(256),
	STRTELEPHONE	VARCHAR(20),
	STRMOBILE	VARCHAR(15),
	STREMAIL	VARCHAR(100),
	BACCOUNTSTATUS	INT,
	NCATEGORY	INT,
	NREFINDEX	INT,
	NROLE	INT,
	NLOGIN	INT,
	NLOCKOUT	INT,
	NFAILEDLOGIN	INT,
	TMLOGIN	DATETIME,
	CONSTRAINT SYSTEM_USER_PK PRIMARY KEY (LUSERINDEX)
);

CREATE TABLE TESS.SYSTEM_HOST (
	STRUSERIP VARCHAR(40),
	CONSTRAINT SYSTEM_USER_UNIQUE UNIQUE (STRUSERIP)
);

CREATE TABLE TESS.SYSTEM_USER_IP (
	ID	VARCHAR(20),
	IP	VARCHAR(40)
);

CREATE TABLE TESS.SYSTEM_USER_ACCGROUP (
	LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,
	STRNAME	VARCHAR(256),
	NACCINDEXCOUNT	INT,
	STRACCINDEX	VARCHAR(256),
	CONSTRAINT SYSTEM_USER_ACCGROUP_PK PRIMARY KEY (LINDEX)
);

CREATE TABLE TESS.TB_MENU (
	MENU_NO	INT,
	MENU_KEY	VARCHAR(256),
	MENU_NAME	VARCHAR(256),
	MENU_NAME_ENG	VARCHAR(256),
	DISPLAY_ORDER	INT,
	ENABLED	VARCHAR(256),
	UPPER_MENU_KEY	VARCHAR(256),
	ROLE_NO	INT,
	URL	VARCHAR(256)
);

CREATE TABLE TESS.YARA_RULE_GROUP (
	LINDEX	INT,
	STRNAME	VARCHAR(256)
);

CREATE TABLE TESS.TB_TABLE_COLUMNS (
    USERID VARCHAR(256) NOT NULL,
    MENUKEY VARCHAR(256) NOT NULL,
    COLID VARCHAR(256) NOT NULL,
    ENABLED VARCHAR(256) NOT NULL
);

CREATE TABLE TESS.PROTOCOL_CONFIG (
    CODE INT,
    PROTOCOL VARCHAR(50)
);


CREATE TABLE TESS.SERVICEINFO_LOG (
	STRIP	VARCHAR(40),
	NPORT	INT,
	STRPROTOCOL	VARCHAR(50),
	STRAPPLICATION	VARCHAR(256),
	TMINSERT	DATETIME,
	TMUPDATE	DATETIME,
	INDEX SERVICEINFO_LOG_IDX (TMINSERT, TMUPDATE)
);

CREATE TABLE TESS.SENSOR_ALIVE_STAT_INFO (
	TMDAY	DATETIME,
	DBLMAXPPS	INT,
	DBLMINPPS	INT,
	DBLTOTALPPS	INT,
	DBLAVGPPS	INT,
	DBLMAXBPS	INT,
	DBLMINBPS	INT,
	DBLTOTALBPS	INT,
	DBLAVGBPS	INT,
	DBLMAXMALBPS	INT,
	DBLMINMALBPS	INT,
	DBLTOTALMALBPS	INT,
	DBLAVGMALBPS	INT,
	DBLMAXMALPPS	INT,
	DBLMINMALPPS	INT,
	DBLTOTALMALPPS	INT,
	DBLAVGMALPPS	INT,
	DBLMAXLPS	INT,
	DBLMINLPS	INT,
	DBLTOTALLPS	INT,
	DBLAVGLPS	INT,
	DBLMAXSESSION	INT,
	DBLMINSESSION	INT,
	DBLTOTALSESSION	INT,
	DBLAVGSESSION	INT,
	DBLMAXDPPS1000	INT,
	DBLMINDPPS1000	INT,
	DBLTOTALDPPS1000	INT,
	DBLAVGDPPS1000	INT,
	DBLMAXCPUUSAGE	INT,
	DBLMINCPUUSAGE	INT,
	DBLTOTALCPUUSAGE	INT,
	DBLAVGCPUUSAGE	INT,
	DBLMAXMEMUSED	INT,
	DBLMINMEMUSED	INT,
	DBLTOTALMEMUSED	INT,
	DBLAVGMEMUSED	INT,
	DBLMAXPROCESSNUM	INT,
	DBLMINPROCESSNUM	INT,
	DBLTOTALPROCESSNUM	INT,
	DBLAVGPROCESSNUM	INT,
	INDEX SENSOR_ALIVE_STAT_INFO_IDX (TMDAY)
);
