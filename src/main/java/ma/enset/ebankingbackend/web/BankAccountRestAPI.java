package ma.enset.ebankingbackend.web;

import com.sun.source.doctree.SeeTree;
import ma.enset.ebankingbackend.dtos.*;
import ma.enset.ebankingbackend.exceptions.BalanceNotSufficientExcep;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundExcept;
import ma.enset.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping(path = "/accounts/{accountId}")
    public BankAccountDto getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundExcept {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping(path = "/accounts")
    public List<BankAccountDto> listAccounts(){
        return bankAccountService.bankAccountList();
    }
    @GetMapping(path = "/accounts/{accountId}/operations")
    public List<AccountOperationDTO> operationDTOS(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }
    @GetMapping(path = "/accounts/{accountId}/accountOperations")
    public AccountHistoryDto getOperationDTOS(@PathVariable String accountId,
                                                    @RequestParam(name = "page",defaultValue = "0") int page,
                                                    @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundExcept {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
    @PostMapping(path = "accounts/debit")
    public DebitDto debitDto(@RequestBody DebitDto debitDto) throws BankAccountNotFoundExcept, BalanceNotSufficientExcep {
         this.bankAccountService.debit(debitDto.getAccountId(),debitDto.getAmount(),debitDto.getDescription());
         return debitDto;
    }
    @PostMapping(path = "accounts/credit")
    public CreditDto creditDto(@RequestBody CreditDto creditDto) throws BankAccountNotFoundExcept, BalanceNotSufficientExcep {
         this.bankAccountService.debit(creditDto.getAccountId(),creditDto.getAmount(),creditDto.getDescription());
         return creditDto;
    }
    @PostMapping(path = "/accounts/transfer")
    public void transfer(@RequestBody TransferDto transferDto) throws BankAccountNotFoundExcept, BalanceNotSufficientExcep {
        this.bankAccountService.transfer(transferDto.getAmount(),
                                         transferDto.getAccountIdSource(),
                                         transferDto.getAccountIdDest());
    }

}
