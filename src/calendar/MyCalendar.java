package calendar;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyCalendar {
    private static final int STANDARD_YEAR = 1970;
    private static final int STANDARD_MONTH = 1;
    private static final int STANDARD_WEEKDAY = 4;
    private BufferedReader br;
    private HashMap<Date, List<String>> scheduler;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private File file;

    public MyCalendar() {
        // init
        br = new BufferedReader(new InputStreamReader(System.in));
        file = new File("calendar.dat");

        if (!file.exists()) {
            scheduler = new HashMap<>();
        } else {
            try {
                in = new ObjectInputStream(new FileInputStream(file));
                scheduler = (HashMap)in.readObject();
            } catch (Exception e) {
                System.out.println("ERROR : FileInputStream");
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    System.out.println("ERROR : Fail to close InputStream");
                }
            }
        }
    }

    public boolean run() {
        System.out.println(Prompt.HEADER);
        System.out.println(Prompt.BODY);
        System.out.println(Prompt.FOOTER);
        System.out.print(Prompt.PROMPT);

        try {
            int menuId = Integer.parseInt(br.readLine());

            switch (menuId) {
                case 1:
                    registerSchedule();
                    break;
                case 2:
                    changeSchedule();
                    break;
                case 3:
                    searchSchedule();
                    break;
                case 4:
                    printCalendar();
                    break;
                case 5:
                    removeSchedule();
                    break;
                case 6:
                    System.out.println("프로그램을 종료합니다...");
                    save();
                    return false;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        } catch (Exception e) {
            System.out.println("ERROR : run");
            return false;
        }

        return true;
    }

    private void save() {
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
        } catch (Exception e) {
            System.out.println("ERROR : Open FileOutputStream");
        }
        try {
            out.writeObject(scheduler);
            System.out.println("성공적으로 저장하였습니다!");
        } catch (Exception e) {
            System.out.println("ERROR : SAVE");
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                System.out.println("ERROR : Fail to close OutputStream");
            }
        }
    }

    public void registerSchedule() {
        try {
            System.out.println("일정을 등록합니다...");
            System.out.print("Date (usage : 2024-04-15) > ");
            String dateStr = br.readLine();
            Date date = (new SimpleDateFormat("yyyy-mm-dd")).parse(dateStr);
            System.out.print("Schedule > ");
            String schedule = br.readLine();

            if (scheduler.containsKey(date)) {
                scheduler.get(date).add(schedule);
            } else {
                List<String> tmp = new ArrayList<>();
                tmp.add(schedule);
                scheduler.put(date, tmp);
            }

        } catch (Exception e) {
            System.out.println("ERROR : register");
        }
    }

    public void changeSchedule() {
        try {
            System.out.println("일정을 수정합니다...");
            System.out.print("Date (usage : 2024-04-15) > ");
            String dateStr = br.readLine();
            Date date = (new SimpleDateFormat("yyyy-mm-dd")).parse(dateStr);
            System.out.println("==========================");

            if (scheduler.containsKey(date)) {
                for (int i = 0; i < scheduler.get(date).size(); ++i) {
                    System.out.println((i + 1) + " " + scheduler.get(date).get(i));
                }
            } else {
                System.out.println("찾을 수 없습니다!");
                return;
            }

            System.out.print("수정할 일정 번호 > ");
            int scheduleId = Integer.parseInt(br.readLine());

            System.out.print("Schedule > ");
            String schedule = br.readLine();
            
            scheduler.get(date).remove(scheduleId);
            scheduler.get(date).add(schedule);
            System.out.println("일정이 변경되었습니다.");
        } catch (Exception e) {
            System.out.println("ERROR : register");
        }
    }

    public void removeSchedule() {
        try {
            System.out.println("일정을 삭제합니다...");
            System.out.print("Date (usage : 2024-04-15) > ");
            String dateStr = br.readLine();
            Date date = (new SimpleDateFormat("yyyy-mm-dd")).parse(dateStr);
            System.out.println("==========================");

            if (scheduler.containsKey(date)) {
                for (int i = 0; i < scheduler.get(date).size(); ++i) {
                    System.out.println((i + 1) + " " + scheduler.get(date).get(i));
                }
            } else {
                System.out.println("찾을 수 없습니다!");
                return;
            }

            System.out.print("삭제할 일정 번호 > ");
            int scheduleId = Integer.parseInt(br.readLine());

            scheduler.get(date).remove(scheduleId - 1);
            System.out.println("일정이 삭제되었습니다.");

            if (scheduler.get(date).size() == 0) {
                scheduler.remove(date);
            }

        } catch (Exception e) {
            System.out.println("ERROR : register");
        }
    }
    
    public int getDays(boolean isLeapYear, int month) {
        return switch (month) {
            case 1, 3, 5, 7, 9, 10, 12 -> {
                yield 31;
            } case 4, 6, 8, 11 -> {
                yield 30;
            } case 2 -> {
                yield (isLeapYear) ? 29 : 28;
            } default -> {
                yield 0;
            }
        };
    }

    private int getWeekday(int year, int month) {
        int days = 0;

        for (int y = MyCalendar.STANDARD_YEAR; y < year; ++y) {
            if (isLeapYear(y)) {
                days += 366;
            } else {
                days += 365;
            }
        }

        for (int m = MyCalendar.STANDARD_MONTH; m < month; ++m) {
            days += getDays(isLeapYear(year), m);
        }

        int startWeekday = (MyCalendar.STANDARD_WEEKDAY + days) % 7;

        return startWeekday;
    }

    private StringBuilder getExactCalendar(int year, int month) {
        StringBuilder sb = new StringBuilder();

        sb.append("     [" + year + "년 " + month + "월]\n");
        sb.append("----------------------------\n");
        sb.append("\tSU\tMO\tTU\tWE\tTH\tFR\tSA\n");

        // 시작 날짜를 위한 공백 먼저 출력
        int startWeekday = getWeekday(year, month);
        for (int d = 0; d < startWeekday; ++d) {
            sb.append("\t");
        }

        for (int d = 1; d <= getDays(isLeapYear(year), month); ++d) {
            if (d < 10) {
                sb.append("\t" + d);
            } else {
                sb.append("\t" + d);
            }

            if (!isOffday(year, month, d)) {
                sb.append(".");
            }



            if ((d + startWeekday) % 7 == 0) {
                sb.append("\n");
            }
        }

        sb.append("\n----------------------------\n");
        return sb;
    }

    private boolean isOffday(int year, int month, int day) {
        String dateStr = year + "-" + month + "-" + day;
        Date date;
        try {
            date = (new SimpleDateFormat("yyyy-mm-dd")).parse(dateStr);
        } catch (ParseException e) {
            System.out.println("ERROR : ifOffday");
            return false;
        }

        if (scheduler.containsKey(date)) {
            return false;
        } else {
            return true;
        }
    }

    public void printCalendar() {
        System.out.print("Date (usage : 2024-04) > ");
        try {
            String[] input = br.readLine().split("-");
            int year = Integer.parseInt(input[0]);
            int month = Integer.parseInt(input[1]);

            StringBuilder calculatedCalendar = getExactCalendar(year, month);
            System.out.println(calculatedCalendar);
        } catch (Exception e) {
            System.out.println("ERROR : print");
        }
    }

    public void searchSchedule() {
        try {
            System.out.println("일정을 조회합니다...");
            System.out.print("Date (usage : 2024-04-15) > ");
            String dateStr = br.readLine();
            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(dateStr);

            if (scheduler.containsKey(date)) {
                for (int i = 0; i < scheduler.get(date).size(); ++i) {
                    System.out.println(scheduler.get(date).get(i));
                }
            } else {
                System.out.println("찾을 수 없습니다!");
            }

        } catch (Exception e) {
            System.out.println("ERROR : search");
        }
    }

    public static boolean isLeapYear(int year) {
        if (year % 4 != 0 || (year % 100 == 0 && year % 400 != 0)) {
            return false;
        } else {
            return true;
        }
    }
}
