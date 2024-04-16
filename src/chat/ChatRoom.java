package chat;

import java.util.ArrayList;
import java.util.Iterator;

public class ChatRoom extends ArrayList<ChatThread> {
    public synchronized void addChatThread(ChatThread thread) {
        this.add(thread);
    }

    public synchronized void removeChatThread(ChatThread thread) {
        this.remove(thread);
    }

    public synchronized void broadcast(String message) {
        Iterator<ChatThread> iter = iterator();
        while (iter.hasNext()) {
            ChatThread thread = iter.next();
            thread.sendMessage(message);
        }
    }
}
