import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class  XMLHandlerToMySQL extends DefaultHandler
{
    private  Voter voter;
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private StringBuilder insertQuery = new StringBuilder();

    public XMLHandlerToMySQL() {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equals("voter")){
            Date birthday = null;
            try {
                birthday = birthDayFormat.parse(attributes.getValue("birthDay"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            voter = new Voter(attributes.getValue("name"), birthday);


        }
        else if (qName.equals("visit") && voter != null)
        {
            insertQuery.append((insertQuery.length() == 0 ? "":", ") + "('" + voter.getName() + "', '" + voter.getBirthDayString() + "', 1) ");

            if (insertQuery.length() > 3000000) {
                    try {
                        DBConnection.executeMultiInsert(insertQuery.toString());

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                insertQuery.delete(0, (insertQuery.length()));
            }

        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if(qName.equals("voter"))
        {
            voter = null;
        }
    }

    public void printDublicatedVoiters() throws SQLException {

        DBConnection.executeMultiInsert(insertQuery.toString());
        //Printing results
        DBConnection.printVoterCounts();
    }
}
