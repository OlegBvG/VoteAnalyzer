import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;

public class XMLHandler extends DefaultHandler
{
    private  Voter voter;
    private long usageMemory;
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private HashMap<Voter, Integer> voterCounts;

    public XMLHandler(){
        voterCounts = new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        super.startElement(uri, localName, qName, attributes);
        try {


            if (qName.equals("voter")){
                Date birthday = birthDayFormat.parse(attributes.getValue("birthDay"));
                voter = new Voter(attributes.getValue("name"), birthday);
                usageMemory = Math.max(usageMemory, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

            }
            else if (qName.equals("visit") && voter != null)
            {
                int count = voterCounts.getOrDefault(voter, 0);
                voterCounts.put(voter, count + 1);
                usageMemory = Math.max(usageMemory, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        };
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        super.endElement(uri, localName, qName);
        if(qName.equals("voter"))
        {
            voter = null;
            usageMemory = Math.max(usageMemory, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

        }
    }

    public long printDublicatedVoiters()
    {
        System.out.println(usageMemory);
        System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        for (Voter voter : voterCounts.keySet())
        {
            int count = voterCounts.get(voter);

            if (count > 1)
            {
                System.out.println(voter.toString() + " - " + count);
            }
        }
        return usageMemory;
    }
}
