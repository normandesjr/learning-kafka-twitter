package com.hibicode.kafkatwitterproducer.security;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Component
public class KeyDecrypt {

    @Autowired
    private AWSKMS kmsClient;

    public String decrypt() {
        try {
            return doDecryptRequest();
        } catch (Exception e) {
            throw new RuntimeException("Erro desconhecido descriptografando o cipherText", e);
        }
    }

    private String doDecryptRequest() throws IOException {
        byte[] cipherTextBlobArray = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("encryptedSecretAsBlob"));
        ByteBuffer cipherTextBlob = ByteBuffer.wrap(cipherTextBlobArray);

        DecryptRequest request = new DecryptRequest().withCiphertextBlob(cipherTextBlob);
        ByteBuffer plainText = kmsClient.decrypt(request).getPlaintext();
        return Charset.forName("UTF-8").decode(plainText).toString();
    }

}
