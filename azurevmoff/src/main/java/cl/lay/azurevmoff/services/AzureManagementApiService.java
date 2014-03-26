package cl.lay.azurevmoff.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class AzureManagementApiService {
    private final static String URL_SUBSCRIPTION_INFORMATION = "https://management.core.windows.net/%s";

    public boolean testConnection(SSLContext sslContext,String accountId) {
        HttpURLConnection urlConnection=null;
        try {
            URL requestedUrl = new URL(String.format(URL_SUBSCRIPTION_INFORMATION,accountId));
            urlConnection = prepareHttpURLConnection(requestedUrl, sslContext);

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if(responseCode!=200){
                return false;
            }
            else{
                return true;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
        }
    }

    private HttpURLConnection prepareHttpURLConnection(URL requestedUrl, SSLContext sslContext) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) requestedUrl.openConnection();
        if(urlConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)urlConnection)
                    .setSSLSocketFactory(sslContext.getSocketFactory());
        }
        urlConnection.addRequestProperty("x-ms-version","2014-02-01");
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(1500);
        urlConnection.setReadTimeout(1500);
        return urlConnection;
    }

}
