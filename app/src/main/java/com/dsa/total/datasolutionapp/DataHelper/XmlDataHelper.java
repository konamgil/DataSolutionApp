package com.dsa.total.datasolutionapp.DataHelper;

import android.content.Context;
import android.content.res.AssetManager;

import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;
import com.dsa.total.datasolutionapp.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by jisun on 2017-05-21.
 */

public class XmlDataHelper {

    private Context mContext;
    private String XmlDataFileName = "xmlphonebook.xml";
    private ArrayList<phoneBookItemObject> mXmlArray;

    //xml insert 부분
    ArrayList<phoneBookItemObject> mPhoneBookItemObjects;
    private String text;

    /**
     * 생성자
     * @param context
     */
    public XmlDataHelper(Context context) {
       this.mContext = context;
        this.mXmlArray =  new ArrayList<phoneBookItemObject>();
//        addDataIntoXML();
        this.mXmlArray = xmlParser();

    }

    /**
     * 첫 초기화 XML 파일로 부터 읽어온 XML데이터들
     * @return
     */
    public ArrayList getXmlData(){
       return  mXmlArray;
    }

    /**
     * XML Insert 후 초기화
     * @param _id
     * @param name
     * @param telNumber
     * @param addr
     */
    public void insertXmlData(int _id, String name, String telNumber, String addr){
        mPhoneBookItemObjects = new ArrayList<phoneBookItemObject>();
        mPhoneBookItemObjects.add(new phoneBookItemObject(_id,name,telNumber,addr,"XML"));
        addDataIntoXML();


    }
    /**
     * assets 폴더의 xml 파일 가져와서 파싱하여 arraylist에 넘겨줍니다.
     * @return
     */
    private ArrayList<phoneBookItemObject> xmlParser() {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        ArrayList<phoneBookItemObject> arrayList = new ArrayList<phoneBookItemObject>();

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
//            InputStream is = mContext.getAssets().open(XmlDataFileName);
            FileInputStream is = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                is = new FileInputStream(mContext.getDataDir() +"/files/"+ XmlDataFileName);
            }

//            InputStream is = mContext.getResources().openRawResource(R.raw.xmlphonebook);
            parser.setInput(is,"UTF-8");
            int eventType = parser.getEventType();
            phoneBookItemObject item = null;

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String startTag = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        if(startTag.equalsIgnoreCase("person")) {
                            item = new phoneBookItemObject();
                        }
                        if (startTag.equalsIgnoreCase("_id")){
                            item.set_id(Integer.parseInt(parser.nextText()));
                        }
                        if(startTag.equalsIgnoreCase("name")) {
                            item.setTelName(parser.nextText());
                        }
                        if(startTag.equalsIgnoreCase("phone")) {
                            item.setTelNumber(parser.nextText());
                        }
                        if(startTag.equalsIgnoreCase("addr")) {
                            item.setTelAddress(parser.nextText());
                        }
                        if(startTag.equalsIgnoreCase("telFromDataHelper")) {
                            item.setTelFromDataHelper(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("person")) {
                            arrayList.add(item);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                }
                eventType = parser.next();
            }
        }catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * add 후 초기화 후 getFilesDir() 에 저장
     */
    public void addDataIntoXML(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;

        Document doc = null;
        Element root = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            FileInputStream is = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                is = new FileInputStream(mContext.getDataDir() +"/files/"+ XmlDataFileName);
            }
            //InputStream is = null; // 에셋에서 가져올때 썻었음
            //is = mContext.getAssets().open(XmlDataFileName); // 에셋에서 가져올때 썻었음
            doc = documentBuilder.parse(is);
            root = doc.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//         mPhoneBookItemObjects = new ArrayList<phoneBookItemObject>();


        for(phoneBookItemObject item : mPhoneBookItemObjects ){
            Element newItem = doc.createElement("person");

            Element _id = doc.createElement("_id");
            _id.appendChild(doc.createTextNode(String.valueOf(item.get_id())));
            newItem.appendChild(_id);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(item.getTelName()));
            newItem.appendChild(name);

            Element phone = doc.createElement("phone");
            phone.appendChild(doc.createTextNode(item.getTelNumber()));
            newItem.appendChild(phone);

            Element addr = doc.createElement("addr");
            addr.appendChild(doc.createTextNode(item.getTelAddress()));
            newItem.appendChild(addr);

            Element telFromDataHelper = doc.createElement("telFromDataHelper");
            telFromDataHelper.appendChild(doc.createTextNode(item.getTelFromDataHelper()));
            newItem.appendChild(telFromDataHelper);

            root.appendChild(newItem);
        }

        DOMSource source = new DOMSource(doc);

        // writing xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }


        File outputFile = new File(mContext.getFilesDir(),"xmlphonebook.xml");
        StreamResult result = new StreamResult(outputFile );
        // creating output stream
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
