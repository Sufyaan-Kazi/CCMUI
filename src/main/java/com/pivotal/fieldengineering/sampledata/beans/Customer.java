package com.pivotal.fieldengineering.sampledata.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

	private int id = 0;
	private int current_balance = 0;
	private short account_count=0;
	private short statement_count=0;
	private int txn_count=0;
	
	public Customer() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + account_count;
		result = prime * result + (int) (current_balance ^ (current_balance >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + statement_count;
		result = prime * result + (int) (txn_count ^ (txn_count >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (account_count != other.account_count)
			return false;
		if (current_balance != other.current_balance)
			return false;
		if (id != other.id)
			return false;
		if (statement_count != other.statement_count)
			return false;
		if (txn_count != other.txn_count)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Customer [id=%s, current_balance=%s, account_count=%s, statementcount=%s, txn_count=%s]", id, current_balance,
				account_count, statement_count, txn_count);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCurrent_balance() {
		return current_balance;
	}

	public void setCurrent_balance(int current_balance) {
		this.current_balance = current_balance;
	}

	public short getAccount_count() {
		return account_count;
	}

	public void setAccount_count(short account_count) {
		this.account_count = account_count;
	}

	public short getStatement_count() {
		return statement_count;
	}

	public void setStatement_count(short statement_count) {
		this.statement_count = statement_count;
	}

	public int getTxn_count() {
		return txn_count;
	}

	public void setTxn_count(int txn_count) {
		this.txn_count = txn_count;
	}
}
