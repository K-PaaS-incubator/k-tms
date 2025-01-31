/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kglory.tms.web.exception.BaseException;

/**
 *
 * @author leecjong
 */
public class FileMonitoring {
    private static Logger logger = LoggerFactory.getLogger(FileMonitoring.class);
    
    private static final String SENSOR_AUDIT_FOLDER = "D:\\TESS_TMS_SAMPLE\\";
    private static final String SENSOR_AUDIT_FILE = "test.txt";
    
    public static void startSensorAudit() {
        try {
            Path path = Paths.get(SENSOR_AUDIT_FOLDER);
            startSensorAuditMonitoring(path, SENSOR_AUDIT_FILE);
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
    
    private static void startSensorAuditMonitoring(Path path, String checkFile) throws BaseException{
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            boolean option = true;
            while(option) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch(InterruptedException ie) {
                    logger.debug("interrupt exception ~~~~");
                    option = false;
                    return;
                }
                
                for(WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
//                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY && fileName.toString().equals(checkFile)) {
//                        // file read
//                    }
                }
                
                boolean valid = key.reset();
                if (!valid) {
                    logger.debug("valid break ~~~~~");
                    option = false;
                    break;
                }
            }
        } catch(IOException e) {
            throw new BaseException(e);
        }
    }
}
