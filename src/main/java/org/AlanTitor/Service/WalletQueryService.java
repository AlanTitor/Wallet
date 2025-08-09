package org.AlanTitor.Service;

import lombok.AllArgsConstructor;
import org.AlanTitor.Dto.WalletResponseDto;
import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Exception.WalletNotFoundException;
import org.AlanTitor.Mapper.WalletMapper;
import org.AlanTitor.Repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Service
public class WalletQueryService {

    private final WalletMapper walletMapper;
    private final WalletRepository walletRepository;

    public UUID createWallet(){
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new Wallet(walletId, BigDecimal.ZERO, null));
        return walletId;
    }

    public WalletResponseDto getWallet(UUID id){
        return walletMapper.toDto(walletRepository.findById(id).orElseThrow(WalletNotFoundException::new));
    }
}
