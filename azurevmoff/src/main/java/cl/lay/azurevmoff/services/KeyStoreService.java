package cl.lay.azurevmoff.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class KeyStoreService {
    private  KeyStore keyStore=null;

    private Exception mLastException=null;

    public Exception getLastException() {
        return mLastException;
    }



    public KeyStoreService() {
        try {
            keyStore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {
            mLastException=e;
        }
    }
    public boolean loadCert(String certLocation, String password){
        FileInputStream fis = null;
        boolean result = false;
        try {
            fis = new FileInputStream(certLocation);
            keyStore.load(fis, password.toCharArray());
            result=true;
        } catch (FileNotFoundException e) {
            mLastException=e;
        } catch (CertificateException e) {
            mLastException=e;
        } catch (NoSuchAlgorithmException e) {
            mLastException=e;
        } catch (IOException e) {
            mLastException=e;
        }
        return result;
    }

    public SSLContext generateSSLContext(String certPassword) {
        KeyManagerFactory kmf = null;
        SSLContext context=null;
        try {
            kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(keyStore, certPassword.toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();
            context = SSLContext.getInstance("TLS");
            context.init(keyManagers, null, null);

        } catch (NoSuchAlgorithmException e) {
            mLastException=e;
        } catch (UnrecoverableKeyException e) {
            mLastException=e;
        } catch (KeyStoreException e) {
            mLastException=e;
        } catch (KeyManagementException e) {
            mLastException=e;
        }
        return context;
    }
}
