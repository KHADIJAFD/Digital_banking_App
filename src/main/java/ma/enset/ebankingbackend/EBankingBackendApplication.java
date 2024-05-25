package ma.enset.ebankingbackend;

import ma.enset.ebankingbackend.dtos.BankAccountDto;
import ma.enset.ebankingbackend.dtos.CurrentBankAccountDto;
import ma.enset.ebankingbackend.dtos.CustomerDto;
import ma.enset.ebankingbackend.dtos.SavingBankAccountDto;
import ma.enset.ebankingbackend.entities.*;
import ma.enset.ebankingbackend.enums.AccountStatus;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.exceptions.BalanceNotSufficientExcep;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundExcept;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundExcep;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import ma.enset.ebankingbackend.repositories.CostumerRepository;
import ma.enset.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@SpringBootApplication
public class EBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }

    @Bean
   CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("khadija","zineb","hafssa").forEach(name->{
                CustomerDto costumer=new CustomerDto();
                costumer.setName(name);
                costumer.setEmail(name+"@gmail.com");
                bankAccountService.saveCostumer(costumer);
            });

            /*bankAccountService.COSTUMER_LIST().forEach(costumer -> {
                try {
                    bankAccountService.saveCurrentAccount(Math.random() * 9000, 9000, costumer.getId());
                    bankAccountService.saveSavingAccount(Math.random() * 12000, 5.5, costumer.getId());
                    List<BankAccountDto> bankAccounts=bankAccountService.bankAccountList();
                    for (BankAccountDto bankAccount:bankAccounts){
                        for (int i=0;i<10;i++){
                            String accountId;
                            if (bankAccount instanceof CurrentBankAccountDto){
                                accountId=((CurrentBankAccountDto) bankAccount).getId();
                            }else {
                                accountId=((SavingBankAccountDto)bankAccount).getId();
                            }
                            bankAccountService.credit(accountId,1000+Math.random()*120000,"credit");
                            bankAccountService.debit(accountId,1000+Math.random()*9000,"debit");
                        }
                    }
                }catch (CustomerNotFoundExcep excep){
                    excep.printStackTrace();
                } catch (BankAccountNotFoundExcept e) {
                    e.printStackTrace();
                } catch (BalanceNotSufficientExcep e) {
                    e.printStackTrace();
                }
            });*/

            bankAccountService.COSTUMER_LIST().forEach(customer->{
                try {
                    bankAccountService.saveCurrentAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingAccount(Math.random()*120000,5.5,customer.getId());

                } catch (CustomerNotFoundExcep e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDto> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDto bankAccount:bankAccounts){
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDto){
                        accountId=((SavingBankAccountDto) bankAccount).getId();
                    } else{
                        accountId=((CurrentBankAccountDto) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                }
            }
        };
   }
    // @Bean
    public CommandLineRunner start(CostumerRepository costumerRepository,
                                   BankAccountRepository bankAccountRepository,
                                   AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("khadija", "hamza", "zineb").forEach(name->{
                Costumer costumer=new Costumer();
                costumer.setName(name);
                costumer.setEmail(name+"@gmail.com");
                costumerRepository.save(costumer);
            });

            costumerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCostumer(cust);
                currentAccount.setOverDraft(9000);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCreationDate(new Date());
                bankAccountRepository.save(currentAccount);
            });

            costumerRepository.findAll().forEach(cust->{
                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCostumer(cust);
                savingAccount.setInterestRate(5.5);
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCreationDate(new Date());
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i =0 ; i<10;i++){
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };
    }
}
