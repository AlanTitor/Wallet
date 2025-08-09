package org.AlanTitor.Service;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.AlanTitor.Dto.WalletRequestDto;
import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Exception.WalletNotFoundException;
import org.AlanTitor.Repository.WalletRepository;
import org.AlanTitor.Service.WalletOperation.IWalletOperation;
import org.AlanTitor.Service.WalletOperation.WalletOperationFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class WalletCommandService {

    private final WalletRepository walletRepository;
    private final WalletOperationFactory operationFactory;

    @Retryable(
            retryFor = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class},
            maxAttempts = 500,
            backoff = @Backoff(delay = 50)
    )
    @Transactional
    public BigDecimal updateWallet(WalletRequestDto request){
        Wallet wallet = walletRepository.findById(request.getId()).orElseThrow(WalletNotFoundException::new);

        IWalletOperation operation = operationFactory.get(request.getOperation());
        operation.count(wallet, request.getAmount());

        Wallet savedWallet = walletRepository.save(wallet);
        return savedWallet.getAmount();
    }

}
