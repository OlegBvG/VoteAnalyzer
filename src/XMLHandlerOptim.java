import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

//import java.util.*;

public class XMLHandlerOptim extends DefaultHandler
{
    private  Voter voter;
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private ArrayList<String> voterCounts;
//    private  String voterCountsAr[];
    private long usageMemory2;
//    private int ind;
    public XMLHandlerOptim() {
        voterCounts = new ArrayList<>();
//        voterCountsAr = new String[149146];
//        ind = 0;

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        try {

            if (qName.equals("voter")){
                Date birthday = birthDayFormat.parse(attributes.getValue("birthDay"));
                voter = new Voter(attributes.getValue("name"), birthday);

                usageMemory2 = Math.max(usageMemory2, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

            }
            else if (qName.equals("visit") && voter != null)
            {
               voterCounts.add(voter.toString());
//               voterCountsAr[ind] = voter.toString();
//               ind++;
               usageMemory2 = Math.max(usageMemory2, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        };

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if(qName.equals("voter"))
        {
            voter = null;
        }
    }

    public long printDublicatedVoiters()
    {
        System.out.println(usageMemory2);
        System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        int counts = 0;

        Collections.sort(voterCounts);
        String votersName = voterCounts.get(0);
        for (String v : voterCounts){
            if (v.equals(votersName)){
                counts++;
            } else {
                if (counts > 1) System.out.println(votersName + " - " + counts);
                counts = 1;
                votersName = v;
            }
        }

//        Arrays.sort(voterCountsAr);
//        String v = voterCountsAr[0];
//        for (int i = 0; i < voterCountsAr.length; i++){
//            if (v.equals(voterCountsAr[i])){
//                c++;
//            } else {
//                if (c > 1) System.out.println(v + " - " + c);
//                c = 1;
//                v = voterCountsAr[i];
//            }
//        }


            usageMemory2 = Math.max(usageMemory2, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

            return usageMemory2;
    }
}

