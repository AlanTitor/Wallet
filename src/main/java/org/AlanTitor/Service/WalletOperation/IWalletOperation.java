package org.AlanTitor.Service.WalletOperation;

import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Enum.OperationType;

import java.math.BigDecimal;

public interface IWalletOperation {
    OperationType getType();
    BigDecimal count(Wallet wallet, BigDecimal amount);
}
