package gugudan;

public class GugudanTest {
    public static void main(String[] args) {
        Gugudan gugudan = Gugudan.getInstance();
        while (gugudan.run()) {
            continue;
        }
    }
}
