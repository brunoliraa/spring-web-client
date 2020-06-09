package com.consumidor.service;

import com.consumidor.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class ProdutoService {

    @Autowired
    private WebClient webClient;

    @PostConstruct
    public Produto getProdutoById(){
        /*mono fornece métodos e funcionalidades para tratar o retorno quando a requisição finalizar
        sem bloquear o método
         */
        Mono<Produto> monoProduto = this.webClient.method(HttpMethod.GET)
                .uri("/produtos/{id}", 1)
                .headers(headers ->headers.setBearerAuth("719872ae-7933-4594-b85e-739cdd773260"))
                .retrieve()//dispara a requisição
                .bodyToMono(Produto.class);//converte

        //fica escutando a resposta do mono, e é executado na hora que termina a requisição
        monoProduto.subscribe(produto -> System.out.println("agora finalizou "
                + produto.getNome()+" " +produto.getValor()));

        System.out.println("ainda n finalizou, vai exibir antes");

        //espera terminar a requisição para poder retornar o valor, e ja retorna o objeto java convertido
        Produto produto = monoProduto.block();
        return produto;
        //assincrono não bloqueante
    }

    /*para mais de 1 requisição simutanea,o Zip agrupa as requisições ta buscando a informação de 2 api
    para formar 1 objeto */
    //Mono.zip(produtoMono, monoPreco)
//        .map(tuple -> {
//            tuple.getT1().setPreco(tuple.getT2().getPreco());
//            return tuple.getT1();
//    }).block(); para retornar o objeto convertido


    @PostConstruct
    public Produto saveProduto(){
        Produto produto = new Produto(null,"gabinete",1, new BigDecimal(5));
        Mono<Produto> mono = this.webClient.post().uri("/produto")
                .body(BodyInserters.fromValue(produto))
                .headers(headers ->headers.setBearerAuth("719872ae-7933-4594-b85e-739cdd773260"))
                .retrieve()
                .bodyToMono(Produto.class);
        System.out.println(mono.block());
                return null;
        /*depende do retorno da api para o método post, caso ela retorna o proprio objeto
          deve criar um Mono<Produto> monoProduto*/
    }
}

/*
se um endpoint falhar, no cenário síncrono-bloqueante, da pra usar um try/catch
no modo assincrono tem outras formas de tratar o erro
 */
