package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Account extends Account_Base {
	private static int counter = 0;

	public Account(Bank bank, Client client) {
		checkArguments(bank, client);

		setIBAN(bank.getCode() + Integer.toString(++Account.counter));
		setBalance(0);

		setClient(client);
		setBank(bank);
	}

	public void delete() {
		setBank(null);
		setClient(null);

		for (Operation operation : getOperationSet()) {
			operation.delete();
		}

		deleteDomainObject();
	}

	private void checkArguments(Bank bank, Client client) {
		if (bank == null || client == null) {
			throw new BankException();
		}

		if (!bank.getClientSet().contains(client)) {
			throw new BankException();
		}

	}

	public String deposit(double amount) {
		if (amount <= 0) {
			throw new BankException();
		}

		setBalance(getBalance() + amount);

		Operation operation = new Operation(Operation.Type.DEPOSIT, this, amount);
		return operation.getReference();
	}

	public String withdraw(double amount) {
		if (amount <= 0 || amount > getBalance()) {
			throw new BankException();
		}

		setBalance(getBalance() - amount);

		return new Operation(Operation.Type.WITHDRAW, this, amount).getReference();
	}

}