/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.file;

import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.ICsvBeanWriter;

/**
 *
 * @author leecjong
 */
public class DetectionPolicyCsv extends AbstractCsvView {
    private Logger logger = LoggerFactory.getLogger(DetectionPolicyCsv.class);

    @Override
    protected void buildCsvDocument(ICsvBeanWriter csvWriter, Map<String, Object> model) throws IOException {
		List<DetectionPolicyVO> policyList = (List<DetectionPolicyVO>) model.get("policyList");
		String[] policyHeader = (String[]) model.get("policyHeader");
		// List<DetectionPolicyVO> responseList = (List<DetectionPolicyVO>)
		// model.get("responseList");
		// String[] responseHeader = (String[]) model.get("responseHeader");

		csvWriter.writeHeader(new String[] { "#DetectionPolicy" });
		csvWriter.writeHeader(policyHeader);
		if (policyList != null) {
			for (DetectionPolicyVO item : policyList) {
				csvWriter.write(item, policyHeader);
			}
		}

		csvWriter.writeHeader(new String[] { "" });

		csvWriter.writeHeader(new String[] { "#DetectionPolicy Response" });
		// csvWriter.writeHeader(responseHeader);
		// if(responseList != null) {
		// for(DetectionPolicyVO item : responseList) {
		// csvWriter.write(item, responseHeader);
		// }
		// }
	}
}
