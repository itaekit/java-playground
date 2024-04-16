 package calendar;

 import java.text.ParseException;

 public class CalendarTest {
    public static void main(String[] args) {
        MyCalendar calendar = new MyCalendar();

        while (calendar.run()) {
            continue;
        }
        System.out.println("Thank you.");
    }
}
