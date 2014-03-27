package cl.lay.azurevmoff.services;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import cl.lay.azurevmoff.models.AzureSubscriptionModel;

public class AzureManagementApiService {
    private final static String URL_SUBSCRIPTION_INFORMATION = "https://management.core.windows.net/%s";

    public AzureSubscriptionModel fetchSubscriptionDetails(SSLContext sslContext, String accountId) {
        HttpURLConnection urlConnection = null;
        AzureSubscriptionModel subscription=null;
        try {
            URL requestedUrl = new URL(String.format(URL_SUBSCRIPTION_INFORMATION, accountId));
            urlConnection = prepareHttpURLConnection(requestedUrl, sslContext);

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode != 200) {
                return null;
            } else {
                InputStream xmlStream = urlConnection.getInputStream();
                subscription= XmlParserService.parseAzureSubscriptionModel(xmlStream);
                return subscription;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }



    private HttpURLConnection prepareHttpURLConnection(URL requestedUrl, SSLContext sslContext) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) requestedUrl.openConnection();
        if (urlConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) urlConnection)
                    .setSSLSocketFactory(sslContext.getSocketFactory());
        }
        urlConnection.addRequestProperty("x-ms-version", "2014-02-01");
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(1500);
        urlConnection.setReadTimeout(1500);
        return urlConnection;
    }

}
