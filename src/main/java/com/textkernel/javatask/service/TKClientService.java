package com.textkernel.javatask.service;


import com.textkernel.javatask.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * A connector class to build a connection to cv parser service
 * @author AKUTLU
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TKClientService {

    private final WebClient webClient;

    private final MultiValueMap<String, Object> multiValueMap;

    public Mono<String> createWebClient(MultipartFile uploadedFile) {

        multiValueMap.set(Constants.UPLOADED_FILE, new FileSystemResource(convertFile(uploadedFile)));


        return webClient.post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multiValueMap))
                .exchange()
                .flatMap(s -> s.bodyToMono(String.class))
                .doOnError(e ->{
                    log.error("a connection error happened");
                });
    }

    private static File convertFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            convertedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
          log.error("io exception happened", e);
        }
        return convertedFile;
    }

}
