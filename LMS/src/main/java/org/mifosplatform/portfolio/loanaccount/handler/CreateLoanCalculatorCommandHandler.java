package org.mifosplatform.portfolio.loanaccount.handler;

import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.portfolio.loanaccount.service.LoanCalculatorWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateLoanCalculatorCommandHandler implements NewCommandSourceHandler {

	private LoanCalculatorWritePlatformService writePlatformService;

	@Autowired
	public CreateLoanCalculatorCommandHandler(
			final LoanCalculatorWritePlatformService writePlatformService) {
		this.writePlatformService = writePlatformService;
	}

	@Transactional
	@Override
	public CommandProcessingResult processCommand(final JsonCommand command) {
		return this.writePlatformService.createLoanCalculator(command.entityId(), command);
	}

}
