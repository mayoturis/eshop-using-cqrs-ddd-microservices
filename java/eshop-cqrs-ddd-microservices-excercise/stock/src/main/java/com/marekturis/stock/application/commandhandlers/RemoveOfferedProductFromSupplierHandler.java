package com.marekturis.stock.application.commandhandlers;

import com.marekturis.common.application.authorization.Authorize;
import com.marekturis.common.application.command.CommandHandler;
import com.marekturis.common.application.transaction.Transactional;
import com.marekturis.common.domain.RoleTypes;
import com.marekturis.stock.application.commands.RemoveOfferedProductFromSupplier;
import com.marekturis.stock.domain.supplier.Supplier;
import com.marekturis.stock.domain.supplier.SupplierRepository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Marek Turis
 */
@Named
public class RemoveOfferedProductFromSupplierHandler implements CommandHandler<RemoveOfferedProductFromSupplier> {

	private SupplierRepository supplierRepository;

	@Inject
	public RemoveOfferedProductFromSupplierHandler(SupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	@Transactional
	@Authorize(RoleTypes.SALESMAN)
	public void handle(RemoveOfferedProductFromSupplier command) {
		Supplier supplier = supplierRepository.getById(command.getSupplierId());

		if (supplier == null) {
			throw new IllegalArgumentException("Supplier with given id doesn't exist");
		}

		supplier.removeOfferedProduct(command.getProductId());
	}
}
