package com.textkernel.javatask.service;


import com.textkernel.javatask.domain.document.CVProcess;
import com.textkernel.javatask.domain.model.StatusEnum;
import com.textkernel.javatask.exception.CustomCVException;
import com.textkernel.javatask.repository.CVProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CVProcessService {

    private final CVProcessRepository  cvProcessRepository;

    public Mono<CVProcess> getProcessByProcessId(String processId) {
        return cvProcessRepository.findById(processId);
    }

    public CVProcess createCVProcess(){
        String processId = UUID.randomUUID().toString();
        return cvProcessRepository.save(new CVProcess(processId, StatusEnum.IN_PROGRESS)).block();
    }

    public void changeStatus(String processId, StatusEnum statusEnum){
        Mono<CVProcess> cvProcess= cvProcessRepository.findById(processId);
        cvProcess.flatMap(
                s->{
                    s.setStatus(statusEnum);
                    return cvProcessRepository.save(s);
                }
        ).subscribe();
    }

    public boolean checkCVProcess(String processId){
        CVProcess cvProcess = cvProcessRepository.findById(processId).block();

        if(cvProcess!=null){
            return cvProcess.getStatus().equals(StatusEnum.COMPLETE)?true:false;
        }
        else throw new CustomCVException("Process Id Not Found");
    }
}
