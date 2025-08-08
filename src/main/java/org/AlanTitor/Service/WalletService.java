package org.AlanTitor.Service;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.AlanTitor.Dto.WalletRequestDto;
import org.AlanTitor.Dto.WalletResponseDto;
import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Enum.OperationType;
import org.AlanTitor.Exception.LessThanZeroException;
import org.AlanTitor.Exception.WalletNotFoundException;
import org.AlanTitor.Mapper.WalletMapper;
import org.AlanTitor.Repository.WalletRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Retryable(
            retryFor = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class},
            maxAttempts = 50,
            backoff = @Backoff(delay = 50)
    )
    @Transactional
    public BigDecimal updateWallet(WalletRequestDto request){
        Wallet wallet = walletRepository.findById(request.getId()).orElseThrow(WalletNotFoundException::new);

        if(request.getOperation() == OperationType.DEPOSIT){
            wallet.setAmount(wallet.getAmount().add(request.getAmount()));
        }else{
            if(wallet.getAmount().compareTo(request.getAmount()) < 0){
                throw new LessThanZeroException();
            }
            wallet.setAmount(wallet.getAmount().subtract(request.getAmount()));
        }
        Wallet savedWallet = walletRepository.save(wallet);
        return savedWallet.getAmount();
    }

    public UUID createWallet(){
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new Wallet(walletId, BigDecimal.ZERO, null));
        return walletId;
    }

    public WalletResponseDto getWallet(UUID id){
        return walletMapper.toDto(walletRepository.findById(id).orElseThrow(WalletNotFoundException::new));
    }
}
