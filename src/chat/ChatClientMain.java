package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class InputThread extends Thread {
    private BufferedReader br;

    public InputThread(BufferedReader br) {
        this.br = br;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println(message);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] InputThread::run()");
        }
    }
}

public class ChatClientMain {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("[ChatClient] usage : java chat.ChatClientMain <YOUR NAME>");
            System.exit(1);
        }

        String userName = args[0];
        BufferedReader br = null;
        BufferedReader keyboard = null;
        PrintWriter pw = null;

        try (Socket socket = new Socket("127.0.0.1", 8000)) {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            // 이름 전송
            pw.println(userName);
            pw.flush();
            
            InputThread inputThread = new InputThread(br);
            inputThread.start();
            String message;

            while ((message = keyboard.readLine()) != null) {
                pw.println(message);
                pw.flush();
                if (message.equals("/quit")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] ChatClientMain #1");
        } finally {
            try {
                pw.close();
                keyboard.close();
                br.close();
                System.out.println("로그아웃 성공");
            } catch (Exception e) {
                System.out.println("[ERROR] ChatClientMain #2");
            }
        }
    }
}
