package ma.enset.ebankingbackend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.dtos.*;
import ma.enset.ebankingbackend.entities.*;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.exceptions.BalanceNotSufficientExcep;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundExcept;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundExcep;
import ma.enset.ebankingbackend.mappers.BankAccountMapperImpl;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import ma.enset.ebankingbackend.repositories.CostumerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CostumerRepository costumerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountRepository bankAccountRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public  CustomerDto saveCostumer(CustomerDto customerDto) {
        log.info("saving new customer");
        Costumer costumer=dtoMapper.fromCustomerDto(customerDto);
        Costumer savedCostumer=costumerRepository.save(costumer);
        return dtoMapper.formCustomer(savedCostumer);
    }

    @Override
    public CurrentBankAccountDto saveCurrentAccount(double amount, double overDraft, Long customerId) throws CustomerNotFoundExcep {
            Costumer costumer=costumerRepository.findById(customerId).orElse(null);
            if (costumer==null)
                throw new CustomerNotFoundExcep("costumer not found");
            CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreationDate(new Date());
        currentAccount.setBalance(amount);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCostumer(costumer);
        CurrentAccount savedCurrentAccount=bankAccountRepository.save(currentAccount);
            return dtoMapper.fromCurrentBankAccount(savedCurrentAccount);
    }

    @Override
    public SavingBankAccountDto saveSavingAccount(double amount, double interestRate, Long customerId) {
        Costumer costumer=costumerRepository.findById(customerId).orElse(null);
        if (costumer==null)
            throw new RuntimeException("costumer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreationDate(new Date());
        savingAccount.setBalance(amount);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCostumer(costumer);
        SavingAccount savedSavingAccount=bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDto> COSTUMER_LIST() {
        List<Costumer> costumers=costumerRepository.findAll();
        List<CustomerDto> customerDtos= costumers.stream()
                .map(costumer->dtoMapper.formCustomer(costumer))
                .collect(Collectors.toList());
        return customerDtos ;
    }

    @Override
    public BankAccountDto getBankAccount(String AccountId) throws BankAccountNotFoundExcept {
        BankAccount bankAccount = bankAccountRepository.findById(AccountId).orElseThrow(() -> new BankAccountNotFoundExcept(" Account not found!"));
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BalanceNotSufficientExcep, BankAccountNotFoundExcept {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundExcept(" Account not found!"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientExcep, BankAccountNotFoundExcept {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundExcept(" Account not found!"));
        if(bankAccount.getBalance()<amount){
            throw new BalanceNotSufficientExcep("balance Not sufficient !");
        }
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(double tranferAmount, String idAccountSource, String idAccountDest) throws BankAccountNotFoundExcept,BalanceNotSufficientExcep{
        debit(idAccountSource,tranferAmount,"transfer to "+idAccountDest);
        credit(idAccountDest,tranferAmount,"transfer from "+idAccountSource);
    }
    @Override
    public List<BankAccountDto> bankAccountList(){
        List<BankAccount> bankAccounts= bankAccountRepository.findAll();
        List<BankAccountDto> bankAccountDtos=bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount){
                CurrentAccount currentAccount= (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }else {
                SavingAccount savingAccount= (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDtos;
    }

    @Override
    public CustomerDto getCustomer(Long id) throws CustomerNotFoundExcep {
        Costumer costumer=costumerRepository.findById(id).orElseThrow(()->new CustomerNotFoundExcep("Customer not found exception"));
        CustomerDto customerDto =dtoMapper.formCustomer(costumer);
        return customerDto;
    }
    @Override
    public  CustomerDto updateCostumer(CustomerDto customerDto) {
        log.info("update new customer");
        Costumer costumer=dtoMapper.fromCustomerDto(customerDto);
        Costumer savedCostumer=costumerRepository.save(costumer);
        return dtoMapper.formCustomer(savedCostumer);
    }
    @Override
    public void deleteCustomer(Long id){
        costumerRepository.deleteById(id);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
       List<AccountOperation> operations=accountOperationRepository.findByBankAccountId(accountId);
       return operations.stream().map(operation->dtoMapper.fromAccountOperation(operation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundExcept {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null) throw new BankAccountNotFoundExcept("Account not found");
        List<AccountOperation> operations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        List<AccountOperationDTO> operationsDto = operations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        AccountHistoryDto accountHistoryDto=new AccountHistoryDto();
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setAccountOperationDTOS(operationsDto);
        accountHistoryDto.setAmount(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        return accountHistoryDto;
    }

    @Override
    public List<CustomerDto> searchCustomers(String keyword) {
        List<Costumer> customers=costumerRepository.searchCostumerByName(keyword);
       return customers.stream().map(costumer -> dtoMapper.formCustomer(costumer)).collect(Collectors.toList());
    }
}

