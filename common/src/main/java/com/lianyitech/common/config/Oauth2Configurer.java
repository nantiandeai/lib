package com.lianyitech.common.config;

import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

public class Oauth2Configurer {

    public static RemoteTokenServices tokenServices (String clientId, String clientSecret, String authAddr) {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);
        tokenServices.setCheckTokenEndpointUrl(authAddr+"/oauth/check_token");
        return tokenServices;
    }
}
