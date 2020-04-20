import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Loader
{
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private static HashMap<Integer, WorkTime> voteStationWorkTimes = new HashMap<>();
    private static HashMap<Voter, Integer> voterCounts = new HashMap<>();
    public static long usageMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    public static void main(String[] args) throws Exception {


        String fileName = "res/data-0.2M.xml";

//        parseWithDoc(fileName); // при парсинге data-18M.xml "Exception in thread "main" java.lang.OutOfMemoryError: Java heap space"
//        parseXMLHandler(fileName); // при парсинге data-18M.xml 38 357 896 байт; при выполнении printDublicatedVoiters() 33 248 640 байт.
//        parseXMLHandlerOptim(fileName); // при парсинге data-18M.xml 43 101 376 байт; при выполнении printDublicatedVoiters() 23 274 984 байт.





//    }
//
//        private static void parseXMLHandlerOptim(String fileName) throws ParserConfigurationException, SAXException, IOException {
//            System.out.println("----------------------------------------------------------------------------------");
////            long usageMemory = 0;
//            SAXParserFactory factoryOptim = SAXParserFactory.newInstance();
//            SAXParser parserOptim = factoryOptim.newSAXParser();
//            XMLHandlerOptim handlerOptim = new XMLHandlerOptim();
//            parserOptim.parse(new File(fileName), handlerOptim);
////            handlerOptim.printDublicatedVoiters();
//            System.out.println("При парсинге файла “data-18M.xml” с использованием SAXParser’а (XMLHandlerOptim) максимум usageMemory = " + handlerOptim.printDublicatedVoiters()); //45 437 472
//        }
//
//
//
//        private static void parseXMLHandler(String fileName) throws ParserConfigurationException, SAXException, IOException {
//            System.out.println("----------------------------------------------------------------------------------");
//            usageMemory = 0;
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            SAXParser parser = factory.newSAXParser();
//            XMLHandler handler = new XMLHandler();
//            parser.parse(new File(fileName), handler);
////            handler.printDublicatedVoiters();
//            System.out.println("При парсинге файла “data-18M.xml” с использованием SAXParser’а (XMLHandler) максимум usageMemory = " + handler.printDublicatedVoiters()); //51 636 248
//        }
//
//        private static void parseWithDoc(String fileName) throws Exception {
//        long usageMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//            long usageMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("При запуске парсинга usageMemory = " + usageMemory); //3 145 728



//        String fileName = "res/data-18M.xml";

            long start = System.currentTimeMillis();
        parseFile(fileName);
        System.out.println("Parsing duration: " + (System.currentTimeMillis() - start) + " ms");

        //Printing results
            DBConnection.printVoterCounts();

//        System.out.println("Voting station work times: ");
//        for (Integer votingStation : voteStationWorkTimes.keySet()) {
//            WorkTime workTime = voteStationWorkTimes.get(votingStation);
////            System.out.println("\t" + votingStation + " - " + workTime);
//            usageMemory = Math.max(usageMemory, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
//        }
//
//        System.out.println("Duplicated voters: ");
//        for (Voter voter : voterCounts.keySet()) {
//            Integer count = voterCounts.get(voter);
//            if (count > 1) {
//                System.out.println("\t" + voter + " - " + count);
//            }
//            usageMemory = Math.max(usageMemory, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
//
//        }
//
//        System.out.println("При парсинге файла “data-18M.xml” с использованием DOM-парсера максимум usageMemory = " + usageMemory); //235 252 136
    }


    private static void parseFile(String fileName) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(fileName));

        findEqualVoters(doc);
//        fixWorkTimes(doc);
    }

    private static void findEqualVoters(Document doc) throws Exception
    {
        NodeList voters = doc.getElementsByTagName("voter");
        int votersCount = voters.getLength();
        for(int i = 0; i < votersCount; i++)
        {
            Node node = voters.item(i);
            NamedNodeMap attributes = node.getAttributes();

            String name = attributes.getNamedItem("name").getNodeValue();
//            Date birthDay = birthDayFormat.parse(attributes.getNamedItem("birthDay").getNodeValue());
            String birthDay = attributes.getNamedItem("birthDay").getNodeValue();

            DBConnection.countVoter(name, birthDay);

//            Voter voter = new Voter(name, birthDay);
//            Integer count = voterCounts.get(voter);
//            voterCounts.put(voter, count == null ? 1 : count + 1);
        }
    }

    private static void fixWorkTimes(Document doc) throws Exception
    {
        NodeList visits = doc.getElementsByTagName("visit");
        int visitCount = visits.getLength();
        for(int i = 0; i < visitCount; i++)
        {
            Node node = visits.item(i);
            NamedNodeMap attributes = node.getAttributes();

            Integer station = Integer.parseInt(attributes.getNamedItem("station").getNodeValue());
            Date time = visitDateFormat.parse(attributes.getNamedItem("time").getNodeValue());
            WorkTime workTime = voteStationWorkTimes.get(station);
            if(workTime == null)
            {
                workTime = new WorkTime();
                voteStationWorkTimes.put(station, workTime);
            }
            workTime.addVisitTime(time.getTime());
        }
    }
}