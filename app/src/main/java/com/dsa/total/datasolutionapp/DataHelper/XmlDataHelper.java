package com.dsa.total.datasolutionapp.DataHelper;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.dsa.total.datasolutionapp.DataAdapter.ListAdapter;
import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;
import com.dsa.total.datasolutionapp.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    public void insertXmlData(int _id, String name, String telNumber, String addr, String dataStore){
        mPhoneBookItemObjects = new ArrayList<phoneBookItemObject>();
        mPhoneBookItemObjects.add(new phoneBookItemObject(_id,name,telNumber,addr,dataStore));
        addDataIntoXML();
    }

    /**
     * person 엘리멘트 찾아서 person의 tagname이 받아온 _id와 일치하면 childnode를 remove 한다
     * @param _id
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void removeName(int _id) throws ParserConfigurationException, IOException, SAXException{
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse (new File(mContext.getFilesDir() +"/"+ XmlDataFileName));

        NodeList nodes = doc.getElementsByTagName("person");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element person = (Element)nodes.item(i);
            Element name = (Element)person.getElementsByTagName("_id").item(0);
            String pName = name.getTextContent();
            if(pName.equals(String.valueOf(_id))){
                person.getParentNode().removeChild(person);
            }
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


        File outputFile = new File(mContext.getFilesDir(),XmlDataFileName);
        StreamResult result = new StreamResult(outputFile );
        // creating output stream
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 첫 진입 assets 폴더의 xml 파일 가져와서 파싱하여 arraylist에 넘겨줍니다.
     * 두번째 진입 부터는 패키지내의 files 폴더의 xml 파일을 가져와 파싱합니다
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
            InputStream iss = null;
            FileInputStream is = null;
            ///////////////////////
            File isFile = new File(mContext.getFilesDir() +"/"+ XmlDataFileName);
            if(isFile.exists() == false){
                iss = mContext.getAssets().open(XmlDataFileName);
                parser.setInput(iss,"UTF-8");
                copyFileFromAssets();
            } else {
                    is = new FileInputStream(mContext.getFilesDir() +"/"+ XmlDataFileName);
                    parser.setInput(is,"UTF-8");
            }

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

            //잠시 주석
            FileInputStream is = null;
            is = new FileInputStream(mContext.getFilesDir() +"/"+ XmlDataFileName);
            //잠시 주석 풀기
//            InputStream is = null; // 에셋에서 가져올때 썻었음
//            is = mContext.getAssets().open(XmlDataFileName); // 에셋에서 가져올때 썻었음

            doc = documentBuilder.parse(is);
            root = doc.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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


        File outputFile = new File(mContext.getFilesDir() +"/"+ XmlDataFileName);
        StreamResult result = new StreamResult(outputFile );
        // creating output stream
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * assets의 xml 파일을 내부패키지 폴더로 복사하기
     */
    public void copyFileFromAssets() {
        InputStream is = null;
        FileOutputStream fos = null;
        File outDir = new File(mContext.getFilesDir() +"/");
        outDir.mkdirs();

        try {
            is = mContext.getAssets().open(XmlDataFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            File outfile = new File(outDir + "/" + XmlDataFileName);
            fos = new FileOutputStream(outfile);
            for (int c = is.read(buffer); c != -1; c = is.read(buffer)) {
                fos.write(buffer, 0, c);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
