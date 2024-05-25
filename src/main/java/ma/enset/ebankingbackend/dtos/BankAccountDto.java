package ma.enset.ebankingbackend.dtos;

import lombok.Data;
import ma.enset.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class BankAccountDto {
    private String type;
    private double balance;
    private Date creationDate;
    private AccountStatus status;
}
