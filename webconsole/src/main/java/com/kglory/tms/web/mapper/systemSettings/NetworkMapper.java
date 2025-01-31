package com.kglory.tms.web.mapper.systemSettings;

import com.kglory.tms.web.model.dto.IpBlockDto;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.NetworkDto;
import com.kglory.tms.web.model.vo.NetworkVO;

public interface NetworkMapper {

	@Transactional(readOnly = true)
	List<NetworkVO> selectNetworkList();
	
	@Transactional(readOnly = true)
	NetworkVO selectNetworkDetailList(NetworkDto dto);
	
	// 네트워크 IP블록 리스트 
	@Transactional(readOnly = true)
	ArrayList<NetworkVO> selectNetworkIpBlockList(NetworkDto dto);
        
	// 네트워크 IP블록 리스트 
	@Transactional(readOnly = true)
	NetworkVO selectNetworkIpBlock(NetworkDto dto);
	
	// 네트워크 IP블록, lid 정보 삭제 
	@Transactional(readOnly = true)
	void deleteNetworkIpBlockLid(NetworkDto dto);
        
	// 네트워크 IP블록 삭제
	@Transactional(readOnly = true)
	void deleteNetworkIpBlockDetail(IpBlockDto dto);

	// 네트워크 IP블록, lid 정보 등록 
	@Transactional(readOnly = true)
	void insertNetworkIpBlock(IpBlockDto dto);
        
	// 네트워크 IP블록, lid 정보 등록 
	@Transactional(readOnly = true)
	void updateNetworkIpBlock(IpBlockDto dto);

	// 네트워크 정보 Update 
	@Transactional(readOnly = true)
	void updateNetworkDetailInfo(NetworkDto dto);

	// 네트워크 정보 신규등록 
	@Transactional(readOnly = true)
	void insertNetworkDetailInfo(NetworkDto dto);

	// 네트워크 삭제 
	@Transactional(readOnly = true)
	void deleteNetworkSettingInfo(NetworkDto dto);

	// 네트워크 인덱스 중복확인 
	@Transactional(readOnly = true)
	int isDuplicatelnetworkIndex(NetworkDto dto);

	// 네트워크 이름 중복확인 
	@Transactional(readOnly = true)
	NetworkVO isDuplicateNetworkName(NetworkDto dto);
}
