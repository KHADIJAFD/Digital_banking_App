package ma.enset.ebankingbackend.services;

import lombok.ToString;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ToString
public class BankService {
    @Autowired
    public BankAccountRepository bankAccountRepository;
    public void consulter(){

    }

}
