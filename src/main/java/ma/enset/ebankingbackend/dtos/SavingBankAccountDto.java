package ma.enset.ebankingbackend.dtos;

import lombok.Data;
import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class SavingBankAccountDto extends BankAccountDto {

    private String id;
    private double balance;
    private Date creationDate;
    private AccountStatus status;
    private CustomerDto costumerDto;
    private double interestRate;
}
