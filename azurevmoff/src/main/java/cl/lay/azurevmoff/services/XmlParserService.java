package cl.lay.azurevmoff.services;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import cl.lay.azurevmoff.models.AzureSubscriptionModel;

public  class XmlParserService {

    public static AzureSubscriptionModel parseAzureSubscriptionModel(InputStream xmlStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(xmlStream, null);
        parser.require(XmlPullParser.START_TAG, null, "Subscription");

        AzureSubscriptionModel subscription = new AzureSubscriptionModel();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("AccountAdminLiveEmailId")) {
                String emailId = readTextNode(parser, "AccountAdminLiveEmailId");
                subscription.setAccountAdminLiveEmail(emailId);
            } else if (name.equals("SubscriptionName")) {
                String subscriptionName = readTextNode(parser,"SubscriptionName");
                subscription.setSubscriptionName(subscriptionName);
            } else if (name.equals("SubscriptionStatus")) {
                String subscriptionStatus = readTextNode(parser, "SubscriptionStatus");
                subscription.setSubscriptionStatus(subscriptionStatus);
            } else {
                skipNode(parser);
            }
        }
        return subscription;
    }

    private static void skipNode(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    private static String readTextNode(XmlPullParser parser, String nodeName) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, nodeName);
        String text = "";
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            parser.nextTag();
        }
        String title = text;
        parser.require(XmlPullParser.END_TAG, null, nodeName);
        return title;
    }
}
