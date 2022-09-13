import net.sf.jasperreports.engine.*;
import java.sql.SQLException;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args){



        final int[] count = {0};


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                DatabaseConnector db = new DatabaseConnector();
                count[0] = db.getReservas(count[0]);

            }
        }, 0, Constants.REFRESH_RATE * 1000);


        while(true) {}
}
}
