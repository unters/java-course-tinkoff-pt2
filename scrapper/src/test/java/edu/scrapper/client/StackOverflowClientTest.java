package edu.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.scrapper.client.stackoverflow.StackOverflowClient;
import edu.scrapper.utils.WiremockHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StackOverflowClientTest {

    private static final String PORT = "8043";
    private static final WiremockHelper WIREMOCK_HELPER = WiremockHelper.getWiremockHelperFor("stackoverflow");

    private WireMockServer wireMockServer;
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    void prepareWiremockServer() {
        wireMockServer = new WireMockServer(Integer.valueOf(PORT));
        wireMockServer.start();

        WebClient client = WebClient.builder()
            .baseUrl("http://localhost:" + PORT)
            .defaultHeader("Content-Type", "application/json")
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        stackOverflowClient = factory.createClient(StackOverflowClient.class);
    }

    @AfterEach
    void afterEachTest() {
        wireMockServer.stop();
    }

    @Test
    void getQuestionComments_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/questions/52340027/comments";
        String responseBody = WIREMOCK_HELPER.getStubForUrlPath(requestUrl);
        wireMockServer.stubFor(get(urlPathMatching(requestUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)
            )
        );

        // then
        assertDoesNotThrow(() -> {
            // when
            stackOverflowClient.getQuestionComments("52340027");
        });
    }

    @Test
    void getQuestionAnswers_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/questions/52340027/answers";
        String responseBody = WIREMOCK_HELPER.getStubForUrlPath(requestUrl);
        wireMockServer.stubFor(get(urlPathMatching(requestUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)
            )
        );

        // then
        assertDoesNotThrow(() -> {
            // when
            stackOverflowClient.getQuestionAnswers("52340027");
        });
    }

    @Test
    void getAnswerComments_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/answers/56189743/comments";
        String responseBody = WIREMOCK_HELPER.getStubForUrlPath(requestUrl);
        wireMockServer.stubFor(get(urlPathMatching(requestUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)
            )
        );

        // then
        assertDoesNotThrow(() -> {
            // when
            stackOverflowClient.getAnswerComments("56189743");
        });
    }
}
