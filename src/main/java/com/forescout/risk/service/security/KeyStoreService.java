package com.forescout.risk.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.certificates.CertificateClient;
import com.azure.security.keyvault.certificates.CertificateClientBuilder;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificate;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.models.SignResult;
import com.azure.security.keyvault.keys.cryptography.models.SignatureAlgorithm;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Service
@Slf4j
public class KeyStoreService {

    @Value("${PKCS12-KEY-STORE-PASSWORD}")
    private String pkcs12KeyStorePassword;

//    private CryptographyClient cryptoClient;
//
//    @PostConstruct
//    public void KeyVaultService() {
//        KeyClient keyClient = new KeyClientBuilder()
//                .vaultUrl("https://techxus-kv.vault.azure.net/")
//                .credential(new DefaultAzureCredentialBuilder().build())
//                .buildClient();
//
//        KeyVaultKey key = keyClient.getKey("FORESCOUT-PFX-CERTIFICATE");
//        cryptoClient = new CryptographyClientBuilder()
//                .keyIdentifier(key.getId())
//                .credential(new DefaultAzureCredentialBuilder().build())
//                .buildClient();
//
//        byte[] dt = signData("abababaabba".getBytes());
//        int x = 0;
//    }
//
//    public byte[] signData(byte[] data) {
//        SignResult result = cryptoClient.sign(com.azure.security.keyvault.keys.cryptography.models.SignatureAlgorithm.RS256, data);
//        return result.getSignature();
//    }

//    @PostConstruct
//    public static PublicKey getPublicKey() throws Exception {
//
//
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        InputStream inputStream = new ClassPathResource("keys/certificate.pfx").getInputStream();
//        keyStore.load(inputStream, "2Likethat#".toCharArray());
//
//        PrivateKey privateKey = (PrivateKey) keyStore.getKey("forescout", "2Likethat#".toCharArray());
//        Certificate cert = keyStore.getCertificate("forescout");
//        PublicKey publicKey = cert.getPublicKey();
//
//        String publicCert = getSecret("FORESCOUT-PFX-CERT");
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        ByteArrayInputStream bs = new ByteArrayInputStream(Base64.getDecoder().decode(publicCert));
//        cert = cf.generateCertificate(bs);
//        return cert.getPublicKey();
//    }

    @PostConstruct
    public PublicKey getPublicKey() throws Exception {

        CryptographyClient cryptoClient = new CryptographyClientBuilder()
                .keyIdentifier("https://techxus-kv.vault.azure.net/keys/FORESCOUT-TEST-KEY")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

       String token = JWT.create()
                .withSubject("subject")
                .withIssuer("example.com")
                .sign(Algorithm.none());

        byte[] dataBytes = token.getBytes(StandardCharsets.UTF_8);
        byte[] signature = cryptoClient.signData(com.azure.security.keyvault.keys.cryptography.models.SignatureAlgorithm.RS256, dataBytes).getSignature();
        String test = Base64.getEncoder().encodeToString(signature);

        //byte[] signature = cryptoClient.sign(SignatureAlgorithm.RS256, token.getBytes(StandardCharsets.UTF_8)).getSignature();
        //String test = token + "." + Base64.getUrlEncoder().encodeToString(signature);

        CertificateClient certificateClient = new CertificateClientBuilder()
                .vaultUrl("https://techxus-kv.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        KeyVaultCertificate retrievedCertificate = certificateClient.getCertificate("FORESCOUT-TEST-CERT");
        X509Certificate publicCertificate = toX509Certificate(retrievedCertificate.getCer());
        PublicKey publicKey = publicCertificate.getPublicKey();
        return publicKey;


//        InputStream inputStream = new ByteArrayInputStream(certBytes);
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        keyStore.load(inputStream, "".toCharArray());
//        PrivateKey privateKey = (PrivateKey) keyStore.getKey("", "".toCharArray());
//        return null;

//        String certBase64 = getSecret("FORESCOUT-TEST-CERT");
//        byte[] certBytes = Base64.getDecoder().decode(certBase64);
//        InputStream inputStream = new ByteArrayInputStream(certBytes);
//
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//
//        try {
//
//            keyStore.load(inputStream, "".toCharArray());
//            PrivateKey privateKey = (PrivateKey) keyStore.getKey("", "".toCharArray());
//            Certificate cert = keyStore.getCertificate("mycert");
//            //PrivateKey privateKey = (PrivateKey) keyStore.getKey("forescout", "2Likethat#".toCharArray());
//            //Certificate cert = keyStore.getCertificate("forescout");
//            PublicKey publicKey = cert.getPublicKey();
//
//            cert = cf.generateCertificate(inputStream);
//            publicKey = cert.getPublicKey();
//            return publicKey;
//        }
//        catch(Exception exx) {
//
//            log.error("exception: {}", exx);
//        }
//
//        try {
//            Certificate cert = cf.generateCertificate(inputStream);
//            PublicKey publicKey = cert.getPublicKey();
//            return publicKey;
//        }
//        catch(Exception exx) {
//
//            log.error("exception: {}", exx);
//            return null;
//        }
    }

//    private static String getSecret(String secretName) {
//        SecretClient client = new SecretClientBuilder()
//                .vaultUrl("https://techxus-kv.vault.azure.net/")
//                .credential(new DefaultAzureCredentialBuilder().build())
//                .buildClient();
//        return client.getSecret(secretName).getValue();
//    }

    private X509Certificate toX509Certificate(byte[] certBytes) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(new java.io.ByteArrayInputStream(certBytes));
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException("Failed to convert certificate bytes", e);
        } catch (Exception e) {
            throw new RuntimeException("Error creating X.509 Certificate", e);
        }
    }
}
