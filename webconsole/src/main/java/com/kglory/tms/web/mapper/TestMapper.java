/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.mapper;

import java.util.Map;

/**
 *
 * @author leecjong
 */
public interface TestMapper {
    public void insertDetectionLog(Map<String, String> map);
    public void insertFileMetaLog(Map<String, String> map);
}
