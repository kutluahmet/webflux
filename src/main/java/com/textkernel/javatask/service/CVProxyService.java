package com.textkernel.javatask.service;


import com.google.gson.Gson;
import com.textkernel.javatask.domain.document.CVExtract;
import com.textkernel.javatask.domain.model.Profile;
import com.textkernel.javatask.domain.model.StatusEnum;
import com.textkernel.javatask.repository.CVExtractRepository;
import com.textkernel.javatask.util.XmlToJsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;


/**
 * @author AKUTLU
 * Service class to process uploaded CV
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CVProxyService {

    private final TKClientService tkClientService;

    private final CVExtractRepository cvExtractRepository;

    private final CVProcessService cvProcessService;

    private final XmlToJsonConverter xmlToJsonConverter;


    /**
     * This methods connect CV Parser service asynchronously
     * When it receives a response it assigns the corresponding process id and saves to database
     * @param processId
     * @param uploadedFile
     */
    public void connect(String processId, MultipartFile uploadedFile) {

        Gson gson = new Gson();

        tkClientService.createWebClient(uploadedFile)
                .flatMap(
                        response ->{
                            cvProcessService.changeStatus(processId, StatusEnum.COMPLETE);
                            var profile = gson.fromJson(xmlToJsonConverter.execute(response), Profile.class);
                            return cvExtractRepository.save(new CVExtract(processId, profile));
                        }
                ).doOnError(e ->{
                cvProcessService.changeStatus(processId, StatusEnum.INVALID);
                log.error("Error occurred during CV Parsing process for processId {} ", processId);
              }).subscribe();
    }

    /**
     * Returns a cv extract by process id
     * @param processId pid
     * @return cv extract
     */
    public Mono<CVExtract> getCVExtractByProcessId(String processId) {

        return cvExtractRepository.findById(processId);
    }
}
