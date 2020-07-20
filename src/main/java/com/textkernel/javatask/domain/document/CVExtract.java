package com.textkernel.javatask.domain.document;

import com.textkernel.javatask.domain.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author AKUTLU
 * Model class for CV Extract
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CVExtract {

    @Id
    public String processId;

    public Profile profile;
}
