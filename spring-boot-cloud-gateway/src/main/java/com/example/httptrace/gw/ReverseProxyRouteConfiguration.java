package com.example.httptrace.gw;

import com.example.httptrace.gw.httptrace.OsbReverseProxyProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@EnableConfigurationProperties(OsbReverseProxyProperties.class)
@EnableAutoConfiguration // necessary for unit tests not starting a springboot app but merely a spring context
public class ReverseProxyRouteConfiguration {

	@Bean
	public RouteLocator osbApiRoute(RouteLocatorBuilder builder,
			OsbReverseProxyProperties osbReverseProxyProperties) {
		return builder.routes()
			.route("debug",
				p -> p
					.path("**")
					.filters(f -> f
						.modifyResponseBody(String.class, String.class,
							(webExchange, originalBody) -> {
								if (originalBody != null) {
									//See https://stackoverflow.com/a/19975149/1484823 for abbreviation
									String abbreviatedBody = StringUtils.abbreviate(originalBody, 10000);
									webExchange.getAttributes().put("cachedResponseBodyObject", abbreviatedBody);
									return Mono.just(originalBody);
								} else {
									return Mono.empty();
								}
							})
						.modifyRequestBody(String.class, String.class,
							(webExchange, originalBody) -> {
								if (originalBody != null) {
									//See https://stackoverflow.com/a/19975149/1484823 for abbreviation
									String abbreviatedBody = StringUtils.abbreviate(originalBody, 10000);
									webExchange.getAttributes().put("cachedRequestBodyObject", abbreviatedBody);
									return Mono.just(originalBody);
								} else {
									return Mono.empty();
								}
							})
					)
					.uri(osbReverseProxyProperties.getBackendBrokerUri())
			)
			.build();
	}
}