package edu.scrapper.service.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.scrapper.dto.github.PullRequestCommentTo;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WireMockTest
public class GitHubClientTest {

    private static final String PORT = "8042";

    private WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    @BeforeEach
    void prepareWiremockServer() {
        wireMockServer = new WireMockServer(Integer.valueOf(PORT));
        wireMockServer.start();

        WebClient client = WebClient.builder()
            .baseUrl("http://localhost:" + PORT)
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        gitHubClient = factory.createClient(GitHubClient.class);
    }

    @AfterEach
    void afterEachTest() {
        wireMockServer.stop();
    }

    @Test
    void getPullRequestComments() {
        // given
        wireMockServer.stubFor(get(urlPathMatching("/repos/alyx/multitool-firmware/pulls/13/comments"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                        {
                            "id": 10,
                            "body": "Great stuff!",
                            "user": {
                                "id": 167237,
                                "login": "russell"
                            },
                            "updated_at": "2020-04-14T16:00:49Z",
                            "created_at": "2020-04-14T16:00:49Z"
                        },
                        {
                            "id": 11,
                            "body": "What's the purpose of this method?",
                            "user": {
                                "id": 52718,
                                "login": "ilai"
                            },
                            "updated_at": "2020-04-14T17:33:02Z",
                            "created_at": "2020-04-14T17:33:02Z"
                        }
                    ]
                    """)
            )
        );

        // when
        List<PullRequestCommentTo> comments = gitHubClient.getPullRequestComments("alyx", "multitool-firmware", 13);

        // then
        assertThat(comments.size()).isEqualTo(2);
    }
}
