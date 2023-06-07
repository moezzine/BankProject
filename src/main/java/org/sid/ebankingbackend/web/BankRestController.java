package org.sid.ebankingbackend.web;

import lombok.AllArgsConstructor;
import org.sid.ebankingbackend.Exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.Exceptions.BankAccountNotFounfException;
import org.sid.ebankingbackend.Exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankRestController {
private BankAccountService bankAccountService;
@GetMapping("/account/{id}")
public BankAccountDto getAcccount(@PathVariable String id) throws BankAccountNotFounfException {
	return bankAccountService.getBankAccount(id);
}
@GetMapping("/account")
public List<BankAccountDto> getListAccount(){
	return bankAccountService.bankAccountList();
}
@GetMapping("/account/{id}/operations")
public List<AccountOperationDto> getAccountOperations(@PathVariable String id) {
	return bankAccountService.accountHistory(id);
}
@GetMapping("/account/{id}/pagesOprations")
public AccountHistoryDto getAccountHistory(@PathVariable String id,
												 @RequestParam(name="page", defaultValue = "0") int page,
												 @RequestParam(name="size", defaultValue = "5") int size) throws BankAccountNotFounfException {
	return bankAccountService.getAccountHistory(id,page,size);
}
@PostMapping("/account/debit")
public DebitDto debit(@RequestBody DebitDto debitDto) throws BankAccountNotFounfException, BalanceNotSufficientException {
	this.bankAccountService.debit(debitDto.getAccountId(),debitDto.getAmount(),debitDto.getDescription());
return debitDto;
}
	@PostMapping("/account/credit")
	public CreditDto credit(@RequestBody CreditDto creditDto) throws BankAccountNotFounfException, CustomerNotFoundException, BalanceNotSufficientException {
		this.bankAccountService.credit(creditDto.getAccountId(),creditDto.getAmount(),creditDto.getDescription());
		return creditDto;
	}
	@PostMapping("/account/transfer")
	public void transfer(@RequestBody TransferRequestDto transferRequestDto) throws BankAccountNotFounfException, BalanceNotSufficientException {
		this.bankAccountService.transfer(transferRequestDto.getAccountSource(),
				transferRequestDto.getAccountDestination(),
				transferRequestDto.getAmount());
	}
}
