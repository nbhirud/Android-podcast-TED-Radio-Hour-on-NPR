package com.nbhirud.tedradiohouronnpr;

import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by sumeesh on 18/06/16.
 */
public class XMLPullParser {


    static public class EpisodePullParser extends DefaultHandler {

        static ArrayList<Episode> episodeList = new ArrayList<>();
        static Episode episode;

        static ArrayList<Episode> parseEpisode(InputStream in) throws XmlPullParserException, IOException {

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, "UTF-8");
            int event = parser.getEventType();
            String temp = "";

            while (event != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("title")) {
                            episode = new Episode();
                            episode.setTitle(parser.nextText());
                        } else if (parser.getName().equals("description")) {
                            episode.setDescription(parser.nextText().trim());
                            temp = "";
                        } else if(parser.getName().equals("pubDate")) {
                            episode.setPubDate(temp = parser.nextText().trim());
                            temp = "";
                        } else if (parser.getName().equals("itunes:image")) {
                            episode.setImgUrl(temp = parser.getAttributeValue(null, "href"));
                        } else if (parser.getName().equals("itunes:duration")) {
                            episode.setDuration(temp = parser.nextText().trim());
                        } else if (parser.getName().equals("enclosure")) {
                            episode.setMp3Url(temp = parser.getAttributeValue(null, "url"));
                        }
                        break;

                    case XmlPullParser.TEXT:
                        temp = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            episodeList.add(episode);
                            episode = null;
                        }
                        break;

                }

                event = parser.next();

            }

            return episodeList;

        }

        public static Episode getEpisode() {
            return episode;
        }

    }

}
