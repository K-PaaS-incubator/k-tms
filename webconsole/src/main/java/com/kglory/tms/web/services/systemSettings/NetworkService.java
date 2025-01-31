package com.kglory.tms.web.services.systemSettings;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.systemSettings.NetworkMapper;
import com.kglory.tms.web.model.dto.IpBlockDto;
import com.kglory.tms.web.model.dto.NetworkDto;
import com.kglory.tms.web.model.vo.NetworkVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.file.FileUtil;

@Service
public class NetworkService {

    private static Logger logger = LoggerFactory.getLogger(NetworkService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    NetworkMapper networkMapper;

    public List<NetworkVO> selectNetworkList() throws BaseException {

        List<NetworkVO> result = null;
        result = networkMapper.selectNetworkList();
        return result;
    }
    
    public List<NetworkVO> selectAllNetworkList() throws BaseException {
        List<NetworkVO> result = null;
        result = networkMapper.selectNetworkList();
        if (result != null && result.size() > 0) {
            for(int i = 0 ; i < result.size() ; i++) {
                NetworkDto dto = new NetworkDto();
                dto.setLnetworkIndex(result.get(i).getLnetworkIndex());
                result.get(i).setIpBolckList(selectNetworkIpBlockList(dto));
            }
        }
        return result;
    }

    public NetworkVO selectNetworkDetailList(NetworkDto dto) throws BaseException {

        NetworkVO result = null;
        result = networkMapper.selectNetworkDetailList(dto);

        return result;
    }

    public ArrayList<NetworkVO> selectNetworkIpBlockList(NetworkDto dto) throws BaseException {

        ArrayList<NetworkVO> selectedList = null;

        selectedList = networkMapper.selectNetworkIpBlockList(dto);
        return selectedList;
    }

    public boolean updateNetworkDetailInfo(NetworkDto dto) throws BaseException {
        boolean rtn = false;
        NetworkVO vo = networkMapper.selectNetworkDetailList(dto);
        boolean chk = false;
        if (!vo.getStrName().equals(dto.getStrName()) || !vo.getStrDescription().equals(dto.getStrDescription())) {
            networkMapper.updateNetworkDetailInfo(dto);
            chk = true;
        }
        
        boolean ipchk = updateNetworkIpBlockList(dto);
        if (chk == true || ipchk == true) {
            rtn = true;
            writeNetwork();
        }

        return rtn;
    }

    public void deleteNetworkIpBlockLid(NetworkDto dto) throws BaseException {
        networkMapper.deleteNetworkIpBlockLid(dto);
            
    }

    public void insertNetworkIpBlockList(NetworkDto dto) throws BaseException {

        for (int i = 0; i < dto.getIpBlockList().size(); i++) {
            if (IpUtil.isIPv6Address(dto.getIpBlockList().get(i).getDwToIp())) {
                dto.getIpBlockList().get(i).setStrToIpv6(dto.getIpBlockList().get(i).getDwToIp());
                dto.getIpBlockList().get(i).setbType(1);
            } else {
                dto.getIpBlockList().get(i).setLongToIp(IpUtil.getHostByteOrderIpToLong(dto.getIpBlockList().get(i).getDwToIp()));
                dto.getIpBlockList().get(i).setbType(0);
            }
            if (IpUtil.isIPv6Address(dto.getIpBlockList().get(i).getDwFromIp())) {
                dto.getIpBlockList().get(i).setStrFromIpv6(dto.getIpBlockList().get(i).getDwFromIp());
            } else {
                dto.getIpBlockList().get(i).setLongFromIp(IpUtil.getHostByteOrderIpToLong(dto.getIpBlockList().get(i).getDwFromIp()));
            }
            networkMapper.insertNetworkIpBlock(dto.getIpBlockList().get(i));
        }
    }
    
    public boolean updateNetworkIpBlockList(NetworkDto dto) throws BaseException {
        boolean rtn = false;
        for (int i = 0; i < dto.getIpBlockList().size(); i++) {
            IpBlockDto blockDto = dto.getIpBlockList().get(i);
            if (IpUtil.isIPv6Address(blockDto.getDwToIp())) {
                blockDto.setStrToIpv6(blockDto.getDwToIp());
                blockDto.setbType(1);
            } else {
                blockDto.setLongToIp(IpUtil.getHostByteOrderIpToLong(blockDto.getDwToIp()));
                blockDto.setbType(0);
            }
            if (IpUtil.isIPv6Address(blockDto.getDwFromIp())) {
                blockDto.setStrFromIpv6(blockDto.getDwFromIp());
            } else {
                blockDto.setLongFromIp(IpUtil.getHostByteOrderIpToLong(blockDto.getDwFromIp()));
            }
            if (blockDto.getIndex() != null && blockDto.getIndex() > 0) {
                NetworkDto pa = new NetworkDto();
                pa.setIndex(blockDto.getIndex());
                NetworkVO vo = networkMapper.selectNetworkIpBlock(pa);
                if (vo != null && (!vo.getDwFromIp().equals(blockDto.getDwFromIp()) || !vo.getDwToIp().equals(blockDto.getDwToIp()))) {
                    networkMapper.updateNetworkIpBlock(blockDto);
                    rtn = true;
                }
            } else {
                blockDto.setlId((int)dto.getLnetworkIndex());
                networkMapper.insertNetworkIpBlock(blockDto);
                rtn = true;
            }
        }
        for(int i = 0 ; i < dto.getIpBlockListDel().size() ; i++) {
            networkMapper.deleteNetworkIpBlockDetail(dto.getIpBlockListDel().get(i));
            rtn = true;
        }
        return rtn;
    }

    public long insertNetworkDetailInfo(NetworkDto dto) throws BaseException {
        Long lnetworkIndex = 0L;
        networkMapper.insertNetworkDetailInfo(dto);

        lnetworkIndex = dto.getLnetworkIndex();
        if (dto.getIpBlockList() != null && dto.getIpBlockList().size() > 0) {
            for (int i = 0; i < dto.getIpBlockList().size(); i++) {
                dto.getIpBlockList().get(i).setlId(lnetworkIndex.intValue());
            }
            insertNetworkIpBlockList(dto);
        }
        writeNetwork();
        return lnetworkIndex;
    }

    public void deleteNetworkSettingInfo(NetworkDto dto) throws BaseException {
        NetworkVO result = null;
        deleteNetworkIpBlockLid(dto);

        networkMapper.deleteNetworkSettingInfo(dto);
        writeNetwork();
    }

    public Integer isDuplicatelnetworkIndex(NetworkDto dto) throws BaseException {
        int duplicationCount = 0;

        duplicationCount = networkMapper.isDuplicatelnetworkIndex(dto);
        return duplicationCount;
    }

    /**
     * 네트워크 이름 중복검사
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public NetworkVO isDuplicateNetworkName(NetworkDto dto) throws BaseException {
        NetworkVO result = new NetworkVO();
        result = networkMapper.isDuplicateNetworkName(dto);
        if (result == null) {
            return new NetworkVO();
        } else {
            return result;
        }
    }
    
    public void writeNetwork() throws BaseException {
        FileUtil.writeNetwork(selectAllNetworkList());
    }
}
