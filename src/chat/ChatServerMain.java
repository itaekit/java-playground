package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServerMain {
    public static ChatRoom chatRoom = new ChatRoom();
    public static void main(String[] args) throws IOException {
        final int PORT = 8000;
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("[Server] Running...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket + " 접근 확인");
            ChatThread chatThread = new ChatThread(socket);
            chatRoom.addChatThread(chatThread);
            chatThread.start();
        }
    }
}
