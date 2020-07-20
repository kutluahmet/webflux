package com.textkernel.javatask.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Util class to convert xml's into json
 * @author AKUTLU
 */
@Component
@Slf4j
public class XmlToJsonConverter
{
    public String execute(String data)
    {
    String value=null;
        try
        {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(data.getBytes());
            ObjectMapper objectMapper = new ObjectMapper();
             value = objectMapper.writeValueAsString(jsonNode);
        } catch (IOException e)
        {
           log.error("XML parse error");
        }
        return value;
    }
}