package org.mifosplatform.organisation.feemaster.serialization;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.InvalidJsonException;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class FeeMasterCommandFromApiJsonDeserializer {

	/**
	 * The parameters supported for this command.
	 */
	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("feeCode", "feeDescription", "transactionType",
					"amount", "locale","isRefundable","depositCalculationType","depositTimeType","depositOn"));
	
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public FeeMasterCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {

		this.fromApiJsonHelper = fromApiJsonHelper;

	}

	/**
	 * @param json check validation for create feemaster
	 */
	public void validateForCreate(final String json) {
    	
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("feemaster");

        final JsonElement element = fromApiJsonHelper.parse(json);

        final String feeCode = fromApiJsonHelper.extractStringNamed("feeCode", element);
        final String feeDescription = fromApiJsonHelper.extractStringNamed("feeDescription", element);
        final String transactionType = fromApiJsonHelper.extractStringNamed("transactionType", element);
        final Integer depositCalculationType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("depositCalculationType", element);
        final Integer depositTimeType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("depositTimeType", element);
        final Integer depositOn = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("depositOn", element);
        final BigDecimal amount = fromApiJsonHelper.extractBigDecimalNamed("amount", element, fromApiJsonHelper.extractLocaleParameter(element.getAsJsonObject()));
        
        
        baseDataValidator.reset().parameter("feeCode").value(feeCode).notBlank().notExceedingLengthOf(10);
        baseDataValidator.reset().parameter("feeDescription").value(feeDescription).notBlank();
        baseDataValidator.reset().parameter("transactionType").value(transactionType).notBlank();
        baseDataValidator.reset().parameter("depositCalculationType").value(depositCalculationType).notNull();
        baseDataValidator.reset().parameter("depositTimeType").value(depositTimeType).notNull();
        baseDataValidator.reset().parameter("depositOn").value(depositOn).notNull();
		baseDataValidator.reset().parameter("amount").value(amount).notNull();
		

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

	
	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
		if (!dataValidationErrors.isEmpty()) {
			throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist","Validation errors exist.", dataValidationErrors);
		}
	}

}
