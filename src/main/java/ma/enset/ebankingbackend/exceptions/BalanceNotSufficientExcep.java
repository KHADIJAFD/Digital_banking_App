package ma.enset.ebankingbackend.exceptions;

public class BalanceNotSufficientExcep extends Exception {
    public BalanceNotSufficientExcep(String mssg){
        super(mssg);
    }
}
