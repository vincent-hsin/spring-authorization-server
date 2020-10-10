/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.server.authorization;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.TestRegisteredClients;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * @author Joe Grandja
 * @author Daniel Garnier-Moiroux
 */
public class TestOAuth2Authorizations {

	public static OAuth2Authorization.Builder authorization() {
		return authorization(TestRegisteredClients.registeredClient().build());
	}

	public static OAuth2Authorization.Builder authorization(RegisteredClient registeredClient) {
		return authorization(registeredClient, Collections.emptyMap());
	}

	public static OAuth2Authorization.Builder authorization(RegisteredClient registeredClient,
			Map<String, Object> authorizationRequestAdditionalParameters) {
		OAuth2AccessToken accessToken = new OAuth2AccessToken(
				OAuth2AccessToken.TokenType.BEARER, "access-token", Instant.now(), Instant.now().plusSeconds(300));
		OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
				.authorizationUri("https://provider.com/oauth2/authorize")
				.clientId(registeredClient.getClientId())
				.redirectUri(registeredClient.getRedirectUris().iterator().next())
				.scopes(registeredClient.getScopes())
				.additionalParameters(authorizationRequestAdditionalParameters)
				.state("state")
				.build();
		return OAuth2Authorization.withRegisteredClient(registeredClient)
				.principalName("principal")
				.accessToken(accessToken)
				.attribute(OAuth2AuthorizationAttributeNames.CODE, "code")
				.attribute(OAuth2AuthorizationAttributeNames.AUTHORIZATION_REQUEST, authorizationRequest)
				.attribute(OAuth2AuthorizationAttributeNames.AUTHORIZED_SCOPES, authorizationRequest.getScopes());
	}
}
