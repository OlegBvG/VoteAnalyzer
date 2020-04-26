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
    private long usageMemory2;
    public XMLHandlerOptim() {
        voterCounts = new ArrayList<>();

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
            usageMemory2 = Math.max(usageMemory2, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

            return usageMemory2;
    }
}

