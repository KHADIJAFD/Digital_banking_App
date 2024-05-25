package ma.enset.ebankingbackend.dtos;

import lombok.Data;
import ma.enset.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccountDto extends BankAccountDto{

    private String id;
    private double balance;
    private Date creationDate;
    private AccountStatus status;
    private CustomerDto costumerDto;
    private double overDraft;
}
