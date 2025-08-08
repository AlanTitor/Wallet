package org.AlanTitor.Mapper;

import org.AlanTitor.Dto.WalletResponseDto;
import org.AlanTitor.Entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponseDto toDto(Wallet wallet);
}
