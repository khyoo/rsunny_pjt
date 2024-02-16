package com.regroup.rsunny.forsale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.regroup.rsunny.common.model.SaleDTO;
import com.regroup.rsunny.common.model.SaleSearchDTO;
import com.regroup.rsunny.system.model.UserDTO;

@Mapper
public interface ForsaleMapper {

	List<SaleDTO> getList(SaleSearchDTO form);

	List<SaleDTO> getListForAdmin(SaleSearchDTO form);

	List<SaleDTO> getSalesOpenList(SaleSearchDTO form);
	
	SaleDTO getSale(SaleDTO form);
	
	SaleDTO getSaleEditing(SaleDTO form);
	
	int insertSaleEditing(SaleDTO form);
	
	int updateSale02Editing(SaleDTO form);
	
	int updateSale03Editing(SaleDTO form);
	
	int updateSale04Editing(SaleDTO form);
	
	int updateSale05Editing(SaleDTO form);
	
	int insertSale(SaleDTO form);
	
	int updateSale(SaleDTO form);
	
	int updateStatus(SaleDTO form);
	
	int deleteSale(SaleDTO form);
	
	int deleteSaleEditing(SaleDTO form);
	
	//알선받기 공인중개사 목록.
	List<UserDTO> getAgentList(SaleDTO form);
	
	int updateViewCount(SaleDTO form);
	
	int updateOpenCount(SaleDTO form);

	
	List<SaleDTO> getAddrSido(SaleDTO form);
	List<SaleDTO> getAddrSigungu(SaleDTO form);
	List<SaleDTO> getAddrEmd(SaleDTO form);
	
	
	List<SaleDTO> getRealTradingsList(SaleDTO form);	
	List<SaleDTO> getRealTradingsAllList(SaleDTO form);	
	int updateRealTradings(SaleDTO form);	
	SaleDTO getRealTradingsListFromPnu(SaleDTO form);
	
	List<SaleDTO> getRealTradingsAptListFromPnu(SaleDTO form);
	List<SaleDTO> getRealTradingsApt2ListFromPnu(SaleDTO form);
	List<SaleDTO> getRealTradingsOfficetelListFromPnu(SaleDTO form);
	List<SaleDTO> getRealTradingsOfficetel2ListFromPnu(SaleDTO form);
	List<SaleDTO> getRealTradingsPresaleListFromPnu(SaleDTO form);	
	List<SaleDTO> getRealTradingsSinglehouseListFromPnu(SaleDTO form);
	List<SaleDTO> getRealTradingsSinglehouse2ListFromPnu(SaleDTO form);
	List<SaleDTO> getRealTradingsMultihouseListFromPnu(SaleDTO form);
	List<SaleDTO> getRealTradingsMultihouse2ListFromPnu(SaleDTO form);
	
	List<SaleDTO> getRealTradingsListFromApt1(SaleDTO form);

	List<SaleDTO> getRealTradingsListFromApt2(SaleDTO form);
	
	List<SaleDTO> getRealTradingsListFromOfficetel1(SaleDTO form);
	
	List<SaleDTO> getRealTradingsListFromOfficetel2(SaleDTO form);
	
	List<SaleDTO> getRealTradingsListFromMultihouse1(SaleDTO form);
	
	List<SaleDTO> getRealTradingsListFromMultihouse2(SaleDTO form);	
	
	
	
	List<SaleDTO> getRealTradingsSeoulList(SaleDTO form);
	List<SaleDTO> getRealTradingsBusanList(SaleDTO form);
	List<SaleDTO> getRealTradingsIncheonList(SaleDTO form);
	List<SaleDTO> getRealTradingsDaejeonList(SaleDTO form);
	List<SaleDTO> getRealTradingsDaeguList(SaleDTO form);
	List<SaleDTO> getRealTradingsGwangjuList(SaleDTO form);
	List<SaleDTO> getRealTradingsUlsanList(SaleDTO form);
	List<SaleDTO> getRealTradingsSejongList(SaleDTO form);
	List<SaleDTO> getRealTradingsJejuList(SaleDTO form);	

	List<SaleDTO> getRealTradingsGyeonggiList(SaleDTO form);
	List<SaleDTO> getRealTradingsGangwonList(SaleDTO form);	
	List<SaleDTO> getRealTradingsChungcheongbukdoList(SaleDTO form);
	List<SaleDTO> getRealTradingsChungcheongnamdoList(SaleDTO form);	
	List<SaleDTO> getRealTradingsJeollabukdoList(SaleDTO form);
	List<SaleDTO> getRealTradingsJeollanamdoList(SaleDTO form);	
	List<SaleDTO> getRealTradingsGyeongsangbukdoList(SaleDTO form);
	List<SaleDTO> getRealTradingsGyeongsangnamdoList(SaleDTO form);
	
	
	int insertTest(SaleDTO form);
	
	
	List<SaleDTO> getCollectCoords(SaleDTO form);
}
