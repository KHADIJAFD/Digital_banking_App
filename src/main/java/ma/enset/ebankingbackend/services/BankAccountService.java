package ma.enset.ebankingbackend.services;

import ma.enset.ebankingbackend.dtos.*;
import ma.enset.ebankingbackend.exceptions.BalanceNotSufficientExcep;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundExcept;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundExcep;

import java.util.List;

public interface BankAccountService {
     CustomerDto saveCostumer(CustomerDto costumer);

    CurrentBankAccountDto saveCurrentAccount(double amount, double overDraft, Long customerId) throws  CustomerNotFoundExcep;
    SavingBankAccountDto saveSavingAccount(double amount, double interestRate, Long customerId) throws CustomerNotFoundExcep;
    List<CustomerDto> COSTUMER_LIST();
    BankAccountDto getBankAccount(String AccountId) throws BankAccountNotFoundExcept;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundExcept, BalanceNotSufficientExcep;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundExcept, BalanceNotSufficientExcep;
    void transfer(double tranferAmount,String idAccountSource,String idAccountDest) throws BankAccountNotFoundExcept, BalanceNotSufficientExcep;

    List<BankAccountDto> bankAccountList();

    CustomerDto getCustomer(Long id) throws CustomerNotFoundExcep;

    CustomerDto updateCostumer(CustomerDto customerDto);

    void deleteCustomer(Long id);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundExcept;

    List<CustomerDto> searchCustomers(String keyword);
}
