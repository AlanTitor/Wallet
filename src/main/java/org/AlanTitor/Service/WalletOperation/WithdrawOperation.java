package org.AlanTitor.Service.WalletOperation;

import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Enum.OperationType;
import org.AlanTitor.Exception.LessThanZeroException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawOperation implements IWalletOperation{
    @Override
    public OperationType getType() {
        return OperationType.WITHDRAW;
    }

    @Override
    public BigDecimal count(Wallet wallet, BigDecimal amount) {
        if(wallet.getAmount().compareTo(amount) < 0){
            throw new LessThanZeroException();
        }
        wallet.setAmount(wallet.getAmount().subtract(amount));
        return wallet.getAmount();
    }
}
