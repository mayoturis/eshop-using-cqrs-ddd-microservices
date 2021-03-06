package com.marekturis.common.application.command;

import com.marekturis.common.application.authorization.AuthorizingCommandHandler;
import com.marekturis.common.application.authorization.Authorizator;
import com.marekturis.common.application.authorization.CustomAuthorize;
import com.marekturis.common.application.transaction.TransactionUnit;
import com.marekturis.common.application.transaction.Transactional;
import com.marekturis.common.application.transaction.TransactionalCommandHandler;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Marek Turis
 */
@Named
public class CommandHandlerBuilder {

	private TransactionUnit transactionUnit;

	private Authorizator authorizator;

	@Inject
	public CommandHandlerBuilder(TransactionUnit transactionUnit, Authorizator authorizator) {
		this.transactionUnit = transactionUnit;
		this.authorizator = authorizator;
	}


	public CommandHandler build(CommandHandler handler) {
		CommandHandler builtHandler = handler;
		if (isTransactional(handler)) {
			builtHandler = new TransactionalCommandHandler(builtHandler, transactionUnit);
		}

		if (shouldBeAuthorized(handler)) {
			String roleName = getRoleName(handler);
			builtHandler = new AuthorizingCommandHandler(builtHandler, roleName, authorizator);
		}

		return builtHandler;
	}

	private boolean isTransactional(CommandHandler handler) {
		try {
			return handler.getClass().isAnnotationPresent(Transactional.class)
					|| handler.getClass().getMethod("handle", Command.class).isAnnotationPresent(Transactional.class);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("CommandHandler doesn't have handle method", e);
		}
	}

	private boolean shouldBeAuthorized(CommandHandler handler) {
		try {
			return handler.getClass().isAnnotationPresent(CustomAuthorize.class)
					|| handler.getClass().getMethod("handle", Command.class).isAnnotationPresent(CustomAuthorize.class);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("CommandHandler doesn't have handle method", e);
		}
	}

	private String getRoleName(CommandHandler handler) {
		try {
			return handler.getClass().getMethod("handle", Command.class).getAnnotation(CustomAuthorize.class).value();
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("CommandHandler doesn't have handle method", e);
		}
	}

}
