/*
package com.textkernel.javatask.controller;


import com.textkernel.javatask.JavaTaskApplication;
import com.textkernel.javatask.authentication.InterceptorAppConfig;
import com.textkernel.javatask.authentication.TokenServiceInterceptor;
import com.textkernel.javatask.domain.document.CVProcess;
import com.textkernel.javatask.domain.document.User;
import com.textkernel.javatask.domain.model.StatusEnum;
import com.textkernel.javatask.repository.CVProcessRepository;
import com.textkernel.javatask.service.CVProcessService;
import com.textkernel.javatask.service.CVProxyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.mockito.Mock;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CVProxyControllerTest {

    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient webTestClient;

    @Mock
    private CVProcessService cvProcessService;

    @Mock
    private CVProxyService cvProxyService;

    @Autowired
    private CVProcessRepository cvProcessRepository;


    @MockBean
    private InterceptorAppConfig interceptorAppConfig;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToController(applicationContext)
                .configureClient()
                .baseUrl("/retrieve")
                .build();

        CVProcess cvProcess = new CVProcess("2",StatusEnum.IN_PROGRESS);
        cvProcessRepository.save(cvProcess).block();
    }


    @Test
    public void getIsDataReady() {
*/
/*
        String processId= "2";
        CVProcess cvProcess = new CVProcess("2",StatusEnum.IN_PROGRESS);
        when(cvProcessService.checkCVProcess(processId)).thenReturn(true);
        when(cvProcessService.getProcessByProcessId(processId).thenReturn(Mono.just(cvProcess)));
*//*


        webTestClient.get().uri("/2").exchange();
               // .expectStatus().isOk();

    }
}*/
