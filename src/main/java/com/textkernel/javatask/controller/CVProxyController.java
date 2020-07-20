package com.textkernel.javatask.controller;


import com.textkernel.javatask.domain.document.CVExtract;
import com.textkernel.javatask.domain.document.CVProcess;
import com.textkernel.javatask.service.CVProcessService;
import com.textkernel.javatask.service.CVProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;


/**
 * API Endpoints class
 *
 * @author AKUTLU
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CVProxyController {

    private final CVProxyService cvProxyService;

    private final CVProcessService cvProcessService;

    /**
     * This method expects a file and immediately generate return and a process id
     * In the background it connects the cv parser service
     *
     * @param uploadedFile CV
     * @return CVProcess-Id
     */
    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<CVProcess>> createCVProxy(@RequestParam("uploaded_file") Optional<MultipartFile> uploadedFile) {

        if (uploadedFile.isPresent()) {
            CVProcess cvProcess = cvProcessService.createCVProcess();
            cvProxyService.connect(cvProcess.getProcessId(), uploadedFile.get());
            return Mono.just(cvProcess)
                    .map(ResponseEntity::ok);
        } else return Mono.empty();
    }

    /**
     * This method returns a processed CV in json format
     * If the CV is not yet process it returns its status
     *
     * @param processId PID
     * @return CV in json format
     */
    @GetMapping(value = "/retrieve/{processId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getCVExtract(@PathVariable("processId") String processId) {

        if (cvProcessService.checkCVProcess(processId)) {
            return cvProxyService.getCVExtractByProcessId(processId)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } else return cvProcessService.getProcessByProcessId(processId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping(value = "/accesstoken")
    public void getAccessToken() {
/*        return new ResponseEntity<>(
                "Call for token access has been made",
                HttpStatus.OK);
    */}
}
