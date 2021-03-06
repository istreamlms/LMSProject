package org.mifosplatform.logistics.item.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.chargecode.data.ChargesData;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.service.Page;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.logistics.item.data.ItemData;
import org.mifosplatform.logistics.item.domain.ItemPrice;
import org.mifosplatform.logistics.item.service.ItemReadPlatformService;
import org.mifosplatform.logistics.supplier.service.SearchSqlQuery;
import org.mifosplatform.organisation.mcodevalues.api.CodeNameConstants;
import org.mifosplatform.organisation.mcodevalues.data.MCodeData;
import org.mifosplatform.organisation.mcodevalues.service.MCodeReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Path("/items")
@Component
@Scope("singleton")
public class ItemApiResource {
	
	private static final Set<String> RESPONSE_ITEM_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("itemId","chargedatas","unitData",
			           "itemclassData","chargeCode","unit","warranty","itemDescription","itemCode","unitPrice","manufacturer"));
	private final String resourceNameForPermissions = "ITEM";
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final DefaultToApiJsonSerializer<ItemData> toApiJsonSerializer;
	private final PlatformSecurityContext context;
	private final ItemReadPlatformService itemReadPlatformService;
	private final MCodeReadPlatformService mCodeReadPlatformService;
	/*private final RegionReadPlatformService regionReadPlatformService;*/ 
	
	
	@Autowired
	public ItemApiResource(final PlatformSecurityContext context,final PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService,
			ApiRequestParameterHelper requestParameterHelper,DefaultToApiJsonSerializer<ItemData> defaultToApiJsonSerializer,
			final ItemReadPlatformService itemReadPlatformService,final MCodeReadPlatformService mCodeReadPlatformService/*,final RegionReadPlatformService regionReadPlatformService*/){
		
		this.context = context;
		this.commandsSourceWritePlatformService=portfolioCommandSourceWritePlatformService;
		this.apiRequestParameterHelper = requestParameterHelper;
		this.toApiJsonSerializer = defaultToApiJsonSerializer;
		this.itemReadPlatformService = itemReadPlatformService;
		this.mCodeReadPlatformService=mCodeReadPlatformService;
		/*this.regionReadPlatformService = regionReadPlatformService;*/
	}
	
	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveItemTemplateData(@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		ItemData itemData = handleTemplateData();
		return this.toApiJsonSerializer.serialize(settings, itemData, RESPONSE_ITEM_DATA_PARAMETERS);
	}

	private ItemData handleTemplateData() {
		final List<EnumOptionData> itemClassdata = this.itemReadPlatformService.retrieveItemClassType();
		final List<EnumOptionData> unitTypeData = this.itemReadPlatformService.retrieveUnitTypes();
		//final List<ChargesData> chargeDatas = this.itemReadPlatformService.retrieveChargeCode();
		Collection<MCodeData> manufacturerDatas=this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_ITEM_MANUFACTURER);
		Collection<MCodeData> chargeCodeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_CHARGECODE);
		/*final List<RegionData> regionDatas = this.regionReadPlatformService.getRegionDetails();*/
		return new ItemData(itemClassdata, unitTypeData, /*chargeDatas,*/manufacturerDatas,chargeCodeData/*, regionDatas*/);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createNewItem(final String jsonRequestBody) {
		
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createItem().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializer.serialize(result);
		
	}

	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveAllItems(@Context final UriInfo uriInfo,  @QueryParam("sqlSearch") final String sqlSearch, 
			@QueryParam("limit") final Integer limit, @QueryParam("offset") final Integer offset) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final SearchSqlQuery searchItems =SearchSqlQuery.forSearch(sqlSearch, offset,limit );
		Page<ItemData> itemData=this.itemReadPlatformService.retrieveAllItems(searchItems);
		return this.toApiJsonSerializer.serialize(itemData);
	}

	@GET
	@Path("{itemId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveSingletemData(@PathParam("itemId") final Long itemId, @Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		ItemData itemData=this.itemReadPlatformService.retrieveSingleItemDetails(null, itemId,null, false); // If you pass clientId set to 'true' else 'false'
		final List<EnumOptionData> itemClassdata = this.itemReadPlatformService.retrieveItemClassType();
		final List<EnumOptionData> unitTypeData = this.itemReadPlatformService.retrieveUnitTypes();
		//final List<ChargesData> chargeDatas = this.itemReadPlatformService.retrieveChargeCode();
		final List<ItemData> auditDetails = this.itemReadPlatformService.retrieveAuditDetails(itemId);
		final Collection<MCodeData> manufacturerDatas = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_ITEM_MANUFACTURER);
		/*final List<RegionData> regionDatas = this.regionReadPlatformService.getRegionDetails();*/
		final List<ItemData> itemPricesDatas = this.itemReadPlatformService.retrieveItemPrice(itemId);
		Collection<MCodeData> chargeCodeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_CHARGECODE);
   		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
   		itemData=new ItemData(itemData,itemClassdata,unitTypeData/*,chargeDatas*/,chargeCodeData,auditDetails,manufacturerDatas);
   		/*itemData.setRegionDatas(regionDatas);*/
   		itemData.setItemPricesDatas(itemPricesDatas);
   		return this.toApiJsonSerializer.serialize(settings, itemData, RESPONSE_ITEM_DATA_PARAMETERS);
	}

	@PUT
	@Path("{itemId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateItem(@PathParam("itemId") final Long itemId,final String jsonRequestBody) {

		final CommandWrapper commandRequest = new CommandWrapperBuilder().updateItem(itemId).withJson(jsonRequestBody).build();
		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializer.serialize(result);
	}
	
	@DELETE
	@Path("{itemId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String deleteItem(@PathParam("itemId") final Long itemId) {

		final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteItem(itemId).build();
		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializer.serialize(result);
	}
	
}
