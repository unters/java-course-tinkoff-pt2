package edu.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.scrapper.client.github.GitHubClient;
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

@WireMockTest
public class GitHubClientTest {

    private static final String PORT = "8042";
    private static final WiremockHelper WIREMOCK_HELPER = WiremockHelper.getWiremockHelperFor("github");

    private WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    @BeforeEach
    void prepareWiremockServer() {
        wireMockServer = new WireMockServer(Integer.valueOf(PORT));
        wireMockServer.start();

        WebClient client = WebClient.builder()
            .baseUrl("http://localhost:" + PORT)
            .defaultHeader("Content-Type", "application/json")
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        gitHubClient = factory.createClient(GitHubClient.class);
    }

    @AfterEach
    void afterEachTest() {
        wireMockServer.stop();
    }

    @Test
    void getRepositoryIssues_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/repos/demouser/demorepo/issues";
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
            gitHubClient.getRepositoryIssues("demouser", "demorepo");
        });
    }

    @Test
    void getRepositoryPulls_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/repos/demouser/demorepo/pulls";
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
            gitHubClient.getRepositoryPulls("demouser", "demorepo");
        });
    }

    @Test
    void getPullRequestComments_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/repos/alyx/multitool-firmware/pulls/13/comments";
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
            gitHubClient.getPullRequestComments("alyx", "multitool-firmware", 13);
        });
    }

    @Test
    void getPullRequestReviews_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/repos/demouser/demorepo/pulls/1/reviews";
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
            gitHubClient.getPullRequestReviews("demouser", "demorepo", 1);
        });
    }

    @Test
    void checkIfPullRequestHasBeenMerged_ValidRequestSentAndValidResponseReceived_NoExceptionIsThrown() {
        // given
        String requestUrl = "/repos/demouser/demorepo/pulls/1/merge";
        wireMockServer.stubFor(get(urlPathMatching(requestUrl))
            .willReturn(aResponse()
                .withStatus(204)
                .withHeader("Content-Type", "application/json")
            )
        );

        // then
        assertDoesNotThrow(() -> {
            // when
            gitHubClient.checkIfPullRequestHasBeenMerged("demouser", "demorepo", 1);
        });
    }
}
