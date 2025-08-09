package org.AlanTitor.Service.WalletOperation;

import org.AlanTitor.Entity.Wallet;

import java.math.BigDecimal;

public interface IWalletOperation {
    BigDecimal count(Wallet wallet, BigDecimal amount);
}
