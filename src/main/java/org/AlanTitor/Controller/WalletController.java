package org.AlanTitor.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.AlanTitor.Dto.WalletRequestDto;
import org.AlanTitor.Dto.WalletResponseDto;
import org.AlanTitor.Service.WalletCommandService;
import org.AlanTitor.Service.WalletQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class WalletController {

    private final WalletQueryService queryService;
    private final WalletCommandService commandService;

    @PostMapping("/wallet")
    public ResponseEntity<WalletResponseDto> updateWallet(@Valid @RequestBody WalletRequestDto request){
        WalletResponseDto response = new WalletResponseDto();
        response.setAmount(commandService.updateWallet(request));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<WalletResponseDto> getWallet(@PathVariable(name = "id") UUID id){
        WalletResponseDto response = queryService.getWallet(id);
        return ResponseEntity.ok().body(response);
    }

    // Create new wallet and return URI
    @GetMapping("/wallet")
    public ResponseEntity<?> createWallet(UriComponentsBuilder uriBuilder){
        UUID id = queryService.createWallet();

        URI uri = uriBuilder.path("/api/v1/wallets/{id}").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).build();
    }
}
