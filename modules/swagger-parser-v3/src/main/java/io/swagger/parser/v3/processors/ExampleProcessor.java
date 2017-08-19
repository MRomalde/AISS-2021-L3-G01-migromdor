package io.swagger.parser.v3.processors;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.examples.Example;
import io.swagger.parser.v3.ResolverCache;
import io.swagger.parser.v3.models.RefFormat;


import java.util.ArrayList;
import java.util.List;

import static io.swagger.parser.v3.util.RefUtils.computeRefFormat;
import static io.swagger.parser.v3.util.RefUtils.isAnExternalRefFormat;

/**
 * Created by gracekarina on 23/06/17.
 */
public class ExampleProcessor {
    private final ResolverCache cache;
    private final OpenAPI openAPI;
    private final ExternalRefProcessor externalRefProcessor;

    public ExampleProcessor(ResolverCache cache, OpenAPI openAPI) {
        this.cache = cache;
        this.openAPI = openAPI;
        this.externalRefProcessor = new ExternalRefProcessor(cache, openAPI);
    }

    public void processExample(Example example) {

        if (example.get$ref() != null){
           processReferenceExample(example);
        }
    }

    public void processExample(List<Example> examples) {
        for(Example example: examples){
            if (example.get$ref() != null){
                processReferenceExample(example);
            }
        }
    }
    private void processReferenceExample(Example example){
        RefFormat refFormat = computeRefFormat(example.get$ref());
        String $ref = example.get$ref();
        if (isAnExternalRefFormat(refFormat)){
            final String newRef = externalRefProcessor.processRefToExternalExample($ref, refFormat);
            if (newRef != null) {
                example.set$ref(newRef);
            }
        }
    }
}