package com.serpare.serpare_beta.xmlParseData;

import com.serpare.serpare_beta.database.DataModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class xmlParser {
    public static List<DataModel> productData(String content){
        try{
            boolean inDataitemTag = false;
            String currentTagName = "";
            DataModel product = null;
            List<DataModel> productList = new ArrayList<>();

            DataModel dataFeed = new DataModel();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){


                switch (eventType){
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        product = new DataModel();
                        productList.add(product);
                        dataFeed.setProduct(currentTagName);
                    default:
                        break;

                }break;
            }
            eventType = parser.next();


            return productList;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
