package org.AlanTitor.Service.WalletOperation;

import org.AlanTitor.Enum.OperationType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WalletOperationFactory {
    private final Map<OperationType, IWalletOperation> strategies = new HashMap<>();

    public WalletOperationFactory(){
        strategies.put(OperationType.DEPOSIT, new DepositOperation());
        strategies.put(OperationType.WITHDRAW, new WithdrawOperation());
    }

    public IWalletOperation get(OperationType type){
        return strategies.get(type);
    }
}
