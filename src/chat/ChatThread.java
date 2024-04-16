package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatThread extends Thread {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private String userName;

    public ChatThread(Socket socket) {
        this.socket = socket;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            userName = br.readLine();
            ChatServerMain.chatRoom.broadcast(userName + "님이 입장하였습니다.");
        } catch (Exception e) {
            System.out.println("[ERROR] ChatThread()");
        }
    }

    public PrintWriter getPw() {
        return pw;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = br.readLine()) != null) {
                if (message.equals("/quit")) {
                    break;
                }
                ChatServerMain.chatRoom.broadcast(userName + " : " + message);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] ChatThread::run()");
        } finally {
            ChatServerMain.chatRoom.broadcast(userName + "님이 나갔습니다.");
            ChatServerMain.chatRoom.removeChatThread(this);
            try {
                br.close();
                pw.close();
                socket.close();
                System.out.println("스트림 강제 해제 성공");
            } catch (Exception e2) {
                System.out.println("[ERROR] ChatThread::run() - stream 해제 실패");
            }
        }
    }
    public void sendMessage(String message) {
        this.pw.println(message);
        this.pw.flush();
    }
}
