import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Key;
import java.util.Scanner;

public class YTLauncher {

    public static void main(String[] args) {
        int hours = get_hours_from_user();
        System.out.println("The music will start in " + (hours > 0 ? hours + " hours from this moment..." : "\b\b\bnow"));
        run_stopwatch(hours);
    }

    private static void run_stopwatch(int hours) {
        //Run stopwatch
        long stopwatch = System.currentTimeMillis() + hours*60*60*1000; //convert to millisec
        long currentTime = 0;
        while(true){
            currentTime = System.currentTimeMillis();
            if(currentTime >= stopwatch) {
                URL url = create_url();
                browse_url(url);
                break;
            }
            //sleep this thread to non calculate current-time every moment
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static int get_hours_from_user() {
        //Getting hours to run a music
        Scanner in = null;
        while (true) {
            try {
                in = new Scanner(System.in);
                System.out.println("Do you want to play yt music immediately?[YES/NO]");
                String answer = in.nextLine();
                if(answer.equalsIgnoreCase("yes")){
                    return 0;
                }
                else {
                    System.out.println("Enter number of hours to start morning music: ");
                    int hours = Integer.parseInt(in.nextLine());
                    return hours;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Enter integer number!");
            } finally {
                in.close();
            }
        }
    }

    private static URL create_url() {
        //Url object
        URL url = null;
        try {
            url = new URL("https://www.youtube.com/watch?v=36YnV9STBqc");
        } catch (MalformedURLException e) {
            System.err.println("Can't parse url...");
            e.printStackTrace();
        }
        return url;
    }

    private static void browse_url(URL url) {
        //starting browser
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop dt = Desktop.getDesktop();
                if (dt.isSupported(Desktop.Action.BROWSE)) {
                    dt.browse(url.toURI());
                    //sleep while url is opening, 30sec to wakeup web browser from hard disk
                    Thread.sleep(30000);
                    //when browser open, the program has to press key space
                    Robot rb= new Robot();
                    rb.keyPress(KeyEvent.VK_SPACE);
                    rb.keyRelease(KeyEvent.VK_SPACE);
                }
            }

        } catch (IOException | URISyntaxException e) {
            System.err.println("Cant convert url to uri or can't browse that url...");
            throw new RuntimeException(e);
        } catch (AWTException e) {
            System.err.println("Robot problem...");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
