package sato.olx.olx_anuncios.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sato.olx.olx_anuncios.domain.LoginRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnuncioScheduler {
    private final ExternalLoginService externalLoginService;
    private final AnuncioService anuncioService;
    private final ClienteConfig clienteConfig;

    @Scheduled(cron = "0 0 12 * * *", zone = "America/Sao_Paulo")
    public void rodarAoMeioDia() {
        log.info("Iniciando rotina automática de anúncios às 12h");
        executarFluxo();
    }

    @Scheduled(cron = "0 0 19 * * *", zone = "America/Sao_Paulo")
    public void rodarAsDezenove() {
        log.info("Iniciando rotina automática de anúncios às 19h");
        executarFluxo();
    }

    private void executarFluxo() {
        for (ClienteConfig.Cliente cliente : clienteConfig.getClientes()) {
            try {
                var login = LoginRequest.builder().email(cliente.getEmail()).senha(cliente.getSenha()).build();
                var loginResponse = externalLoginService.login(login);
                var anuncios = anuncioService.buscarAnuncios(loginResponse);
                anuncioService.salvarAnuncios(anuncios);
                log.info("Rotina automática finalizada para {}. {} anúncios processados.", cliente.getEmail(), anuncios.size());
            } catch (Exception e) {
                log.error("Erro na rotina automática de anúncios para {}", cliente.getEmail(), e);
            }
        }
    }
} 