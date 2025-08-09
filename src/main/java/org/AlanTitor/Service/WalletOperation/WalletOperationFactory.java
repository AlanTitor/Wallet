package org.AlanTitor.Service.WalletOperation;

import org.AlanTitor.Enum.OperationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WalletOperationFactory {
    private final Map<OperationType, IWalletOperation> strategies;

    // spring инжектит и получает map<OperationType, IWalletOperation>
    public WalletOperationFactory(List<IWalletOperation> operations) {
        this.strategies = operations.stream().collect(Collectors.toMap(IWalletOperation::getType, Function.identity()));
    }

    public IWalletOperation get(OperationType type){
        return strategies.get(type);
    }
}
