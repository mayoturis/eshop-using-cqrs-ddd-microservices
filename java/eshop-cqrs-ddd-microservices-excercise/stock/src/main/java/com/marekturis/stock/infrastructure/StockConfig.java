package com.marekturis.stock.infrastructure;

import com.marekturis.common.application.command.CommandDispatcher;
import com.marekturis.common.application.command.CommandHandlerBuilder;
import com.marekturis.common.infrastructure.CommonConfig;
import com.marekturis.stock.application.commandhandlers.*;
import com.marekturis.stock.application.commands.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Marek Turis
 */
@Configuration
@ComponentScan({"com.marekturis.stock.application",
		"com.marekturis.stock.domain", "com.marekturis.stock.infrastructure"})
@Import({CommonConfig.class})
public class StockConfig {

	@Autowired
	private CommandHandlerBuilder commandHandlerBuilder;

	@Autowired
	private CreateWarehouseHandler newWarehouseCommandHandler;

	@Autowired
	private AddNewProductToWarehouseHandler addNewProductToWarehouseHandler;

	@Autowired
	private IncreaseProductAmmountInWarehouseHandler increaseProductAmmountInWarehouseHandler;

	@Autowired
	private DecreaseProductAmmountInWarehouseHandler decreaseProductAmmountInWarehouseHandler;

	@Autowired
	private CreateSupplierHandler createSupplierHandler;

	@Autowired
	private AddNewOfferedProductToSupplierHandler addNewOfferedProductToSupplierHandler;

	@Autowired
	private RemoveOfferedProductFromSupplierHandler removeOfferedProductFromSupplierHandler;

	@Bean
	public CommandDispatcher commandDispatcher() {
		CommandDispatcher commandDispatcher = new CommandDispatcher(commandHandlerBuilder);
		commandDispatcher.addHandler(CreateWarehouse.class, newWarehouseCommandHandler);
		commandDispatcher.addHandler(AddNewProductToWarehouse.class, addNewProductToWarehouseHandler);
		commandDispatcher.addHandler(IncreaseProductAmmountInWarehouse.class, increaseProductAmmountInWarehouseHandler);
		commandDispatcher.addHandler(DecreaseProductAmmountInWarehouse.class, decreaseProductAmmountInWarehouseHandler);
		commandDispatcher.addHandler(CreateSupplier.class, createSupplierHandler);
		commandDispatcher.addHandler(AddNewOfferedProductToSupplier.class, addNewOfferedProductToSupplierHandler);
		commandDispatcher.addHandler(RemoveOfferedProductFromSupplier.class, removeOfferedProductFromSupplierHandler);
		return commandDispatcher;
	}
}
