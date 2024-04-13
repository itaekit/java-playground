package gugudan;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Gugudan implements MultiplyCalculator {
    private static Gugudan instance = new Gugudan();
    private Scanner scanner;
    private Gugudan() {
        scanner = new Scanner(System.in);
    }

    public static Gugudan getInstance() {
        if (instance == null) {
            instance = new Gugudan();
        }
        return instance;
    }

    public boolean run() {
        while (true) {
            System.out.println("============[Menu]============");
            System.out.println("[1] n 입력\t-> n*n단 출력");
            System.out.println("[2] m,n 입력\t-> m*n까지 출력");
            System.out.println("[3] 종료");
            System.out.print("Your Input : ");

            int menuId;
            try {
                menuId = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("[ERROR : 001] 잘못된 입력입니다. 다시 시도해주세요.");
                scanner.next(); // buffer 비우는 용도
                continue;
            }

            int[][] ret = null;
            switch (menuId) {
                case 1:
                    ret = simpleMultiply();
                    print(ret);
                    break;
                case 2:
                    ret = complexMultiply();
                    print(ret);
                    break;
                case 3:
                    System.out.println("프로그램을 종료합니다...");
                    return false;
                default:
                    System.out.println("올바른 MenuId를 입력해주세요...");
            }

            return true;
        }
    }

    public int[][] simpleMultiply() {
        while (true) {
            try {
                System.out.print("n을 입력하세요 : ");
                int n = scanner.nextInt();
                int[][] ret = new int[n-1][n];
                int x, y = 0;

                for (int i = 2; i <= n; ++i) {
                    x = 0;
                    for (int j = 1; j <= n; ++j) {
                        ret[y][x] = (i * j);
                        ++x;
                    }
                    ++y;
                }

                return ret;
            } catch (Exception e) {
                System.out.println("[ERROR : 002] 잘못된 입력입니다. 다시 시도해주세요");
                scanner.next(); // buffer 비우는 용도
            }
        }
    }

    public int[][] complexMultiply() {
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("m,n을 입력하세요 : ");

            int m, n;
            try {
                String[] input = br.readLine().split("[,]");
                m = Integer.parseInt(input[0]);
                n = Integer.parseInt(input[1]);
            } catch (Exception e) {
                System.out.println("[ERROR : 003] 잘못된 입력입니다. 다시 시도해주세요");
                continue;
            }

            int[][] ret = new int[m-1][n];
            int x, y = 0;

            for (int i = 2; i <= m; ++i) {
                x = 0;
                for (int j = 1; j <= n; ++j) {
                    ret[y][x] = (i * j);
                    ++x;
                }
                ++y;
            }

            return ret;
        }
    }

    public static void print(int[][] gugudan) {
        int dan = gugudan[0][0];
        int times;

        for (int i = 0; i < gugudan.length; ++i) {
            System.out.println(dan + "단");
            times = 1;
            for (int j = 0; j < gugudan[i].length; ++j) {
                System.out.println(dan + " * " + times + " = " + gugudan[i][j]);
                ++times;
            }
            ++dan;
            System.out.println("-------------");
        }
    }
}
