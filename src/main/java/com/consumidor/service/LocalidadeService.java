package com.consumidor.service;

import com.consumidor.model.Localidade;
import com.consumidor.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class LocalidadeService {

    @Autowired
    private WebClient localidadesClient;

    //get list
//    @PostConstruct
    public List<Localidade> getLocalidades() {

        Flux<Localidade> produtosFlux = this.localidadesClient.get()
                .uri("/localidades/estados")
                .retrieve()//dispara a requisição
                .bodyToFlux(Localidade.class);//converte

        produtosFlux.subscribe(produto -> System.out.println("agora finalizou "
                + produto.getNome()));

        System.out.println("ainda n finalizou, vai exibir antes");

        List<Localidade> produtoList = produtosFlux.collectList().block();
        produtoList.stream().forEach(p -> System.out.println(p.getNome()));
        return produtoList;
    }
}
