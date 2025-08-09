package org.AlanTitor.Service.WalletOperation;

import org.AlanTitor.Entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositOperation implements IWalletOperation{
    @Override
    public BigDecimal count(Wallet wallet, BigDecimal amount) {
        wallet.setAmount(wallet.getAmount().add(amount));
        return wallet.getAmount();
    }
}
