package org.mifosplatform.logistics.mrn.data;


import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.logistics.item.data.ItemData;
import org.mifosplatform.organisation.office.data.OfficeData;

public class MRNDetailsData {

	private String id;
	private LocalDate requestedDate;
	private String fromOffice;
	private String toOffice;
	private Long orderdQuantity;
	private Long receivedQuantity;
	private String status;
	private Long officeId;
	private Long parentId;
	private String officeName;
	private Collection<OfficeData> officeData;
	private Collection<ItemData> itemMasterData;
	private Long itemId;
	private String itemCode;
	private String itemDescription;
	private Collection<MRNDetailsData> mrnIds;
	private List<MRNDetailsData> itemsaleIds;
	private Long mrnId;
	private List<String> serialNumber;
	private Long fromOfficeNum;
	private Long toOfficeNum;
	
	
	public MRNDetailsData(final String id, final LocalDate requestedDate, final String fromOffice, final String toOffice, final Long orderdQuantity, 
			        final Long receivedQuantity, final String status, final String itemDescription){
		this.id = id;
		this.requestedDate = requestedDate;
		this.fromOffice = fromOffice;
		this.toOffice = toOffice;
		this.orderdQuantity = orderdQuantity;
		this.receivedQuantity = receivedQuantity;
		this.status = status;
		this.itemDescription = itemDescription;
	}

	
	public MRNDetailsData(Collection<OfficeData> officeData, Collection<ItemData> itemMasterData) {
		this.officeData = officeData;
		this.itemMasterData = itemMasterData;
	}
	
	public MRNDetailsData(Collection<MRNDetailsData> mrnIds,List<MRNDetailsData> itemsaleIds) {
	this.mrnIds = mrnIds;
	this.itemsaleIds=itemsaleIds;
	}
	
	public MRNDetailsData(Long mrnId,String itemDescription, Long itemId,String itemsaleId) {
		
		this.id=itemsaleId;
		this.mrnId = mrnId;
		this.itemDescription = itemDescription;
		this.itemId = itemId;
	}

	public MRNDetailsData(List<String> serialNumber) {
		this.serialNumber = serialNumber;
	}

	public MRNDetailsData(Long agentId) {
		
			this.officeId=agentId;
	}

	public MRNDetailsData(Collection<OfficeData> officeData2) {
		// TODO Auto-generated constructor stub
	}


	public LocalDate getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(LocalDate requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getFromOffice() {
		return fromOffice;
	}

	public void setFromOffice(String fromOffice) {
		this.fromOffice = fromOffice;
	}

	public String getToOffice() {
		return toOffice;
	}

	public void setToOffice(String toOffice) {
		this.toOffice = toOffice;
	}

	public Long getOrderdQuantity() {
		return orderdQuantity;
	}

	public void setOrderdQuantity(Long orderdQuantity) {
		this.orderdQuantity = orderdQuantity;
	}

	public Long getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Long receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public Collection<OfficeData> getOfficeData() {
		return officeData;
	}

	public void setOfficeData(Collection<OfficeData> officeData) {
		this.officeData = officeData;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Collection<ItemData> getItemMasterData() {
		return itemMasterData;
	}

	
	public Collection<MRNDetailsData> getMrnIds() {
		return mrnIds;
	}

	public void setMrnIds(Collection<MRNDetailsData> mrnIds) {
		this.mrnIds = mrnIds;
	}

	public Long getMrnId() {
		return mrnId;
	}

	public void setMrnId(Long mrnId) {
		this.mrnId = mrnId;
	}

	public List<String> getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(List<String> serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Long getFromOfficeNum() {
		return fromOfficeNum;
	}

	public void setFromOfficeNum(Long fromOfficeNum) {
		this.fromOfficeNum = fromOfficeNum;
	}

	public Long getToOfficeNum() {
		return toOfficeNum;
	}

	public void setToOfficeNum(Long toOfficeNum) {
		this.toOfficeNum = toOfficeNum;
	} 
	
	
}
