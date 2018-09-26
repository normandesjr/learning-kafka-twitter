package com.hibicode.kafkatwitterproducer.config;

import com.hibicode.kafkatwitterproducer.security.KeyDecrypt;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hibicode.kafkatwitterproducer.config.TwitterCredentialsConfig.TwitterCredential;

public class TwitterCredentialConfigTest {

    private TwitterCredentialsConfig twitterCredentialsConfig;

    @Mock
    private KeyDecrypt keyDecrypt;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        twitterCredentialsConfig = new TwitterCredentialsConfig();
    }

    @Test
    public void should_create_twitter_credential_object_from_string() {
        final String string = "api_key=ak,api_secret_key=ask,access_token=at,access_token_secret=ats";
        when(keyDecrypt.decrypt()).thenReturn(string);

        TwitterCredential twitterCredential = twitterCredentialsConfig.create(keyDecrypt);

        TwitterCredential expectedTwitterCredential = new TwitterCredential("ak", "ask", "at", "ats");
        assertThat(twitterCredential, equalTo(expectedTwitterCredential));
    }

}
