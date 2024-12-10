package com.mizutest.demo.utilities;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Set;

public class JsonValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Set<ValidationMessage> validateJson(Object jsonData) throws Exception {
        // Load the JSON schema from the resources folder
        InputStream schemaStream = new ClassPathResource("schemas/upload.json").getInputStream();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance();
        JsonSchema schema = schemaFactory.getSchema(schemaStream);

        // Parse the JSON data
        JsonNode jsonNode = objectMapper.valueToTree(jsonData);

        // Validate the JSON data against the schema
        return schema.validate(jsonNode);
    }
}
