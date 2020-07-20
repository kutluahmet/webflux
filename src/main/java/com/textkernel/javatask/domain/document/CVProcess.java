package com.textkernel.javatask.domain.document;


import com.textkernel.javatask.domain.model.Profile;
import com.textkernel.javatask.domain.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CVProcess {

    @Id
    public String processId;

    public StatusEnum status;
}
