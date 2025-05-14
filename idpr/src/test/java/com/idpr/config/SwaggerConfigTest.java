package com.idpr.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SwaggerConfigTest {

    @InjectMocks
    private SwaggerConfig swaggerConfig;

    @Test
    void publicApi_returnsGroupedOpenApiWithCorrectGroupNameAndPaths() {
        GroupedOpenApi groupedOpenApi = swaggerConfig.publicApi();
        assertNotNull(groupedOpenApi);
        assertEquals("public", groupedOpenApi.getGroup());
        assertTrue(groupedOpenApi.getPathsToMatch().contains("/**"));
    }

    @Test
    void customOpenAPI_returnsOpenAPIWithCorrectTitleAndVersion() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        assertNotNull(openAPI);
        assertEquals("IDPR API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
    }
}