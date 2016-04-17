package com.serpare.serpare_beta.database;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;

import com.serpare.serpare_beta.MainActivity;
import com.serpare.serpare_beta.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nishant on 14/09/2015.
 */
public class XmlDatabse {

    static String input = MainActivity.USER_INPUT;
    public static final ArrayList<String> list = new ArrayList<>();
    public static final ArrayList<String> textList = new ArrayList<>();


    public static void PullData(Context cntx) throws MalformedURLException, IOException, XmlPullParserException {
//        URL url = new URL("https://91ac7a6688151f94eddd1abc6d153048cb27ba88.googledrive.com/host/0BwYrL5UHVGjWfmJBdnlnY3E0REVNYzZ4TXhrVXJmSVNxSGVON2hOeWczMTkyaHZOLWVtQmc/SearchItems.xml");
//        URLConnection ucon = url.openConnection();
//
//        InputStream is = ucon.getInputStream();
//        BufferedInputStream bis = new BufferedInputStream(is);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

//        UserInputSuggestions.setSuggestionList(list);
//





        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
//        XmlPullParser xpp = factory.newPullParser();


        /*
        This method works fine when setter method for list is called onCreate
        */
        XmlResourceParser xpp = cntx.getResources().getXml(R.xml.searchitems);



        /*This method works fine too if not using XmlResourceParser method. This is input stream method. Just remember to call set list method from oncreate in MAIN ACTIVITY*/
//        InputStream is = cntx.getResources().openRawResource(R.raw.searchitems);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        xpp.setInput(reader);










        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                //System.out.println("Start document");
            } else if (eventType == XmlPullParser.END_DOCUMENT) {
                //System.out.println("End document");
            } else if (eventType == XmlPullParser.START_TAG) {
                //System.out.println("Start tag "+xpp.getName());
                list.add(xpp.getName());
            } else if (eventType == XmlPullParser.END_TAG) {
                //System.out.println("End tag "+xpp.getName());
            } else if (eventType == XmlPullParser.TEXT) {
                //System.out.println("Text "+xpp.getText());
                textList.add(xpp.getText());
            }
            eventType = xpp.next();
        }

        xpp.close();



		/*This checks user input and if it is present in list then it takes it as index and prints the value*/
//        if (list.contains(input)) {
//            System.out.println(list.get(list.indexOf(input)));
//            if (textList.contains(input)) {
//                System.out.println(textList.get(textList.indexOf(input)));
//            }
//
//        }


    }


}