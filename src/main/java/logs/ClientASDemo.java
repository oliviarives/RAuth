package logs;

public class ClientASDemo {
    public static void main(String[] args) {
        JsonLogger.log("127.0.0.1", 50000, "TCP", "CHK", "Toto", "GOOD");
        JsonLogger.log("127.0.0.1", 50001, "UDP", "CHK", "Alice", "BAD");
        JsonLogger.log("127.0.0.1", 50002, "TCP", "ADD", "Bob", "DONE");
    }
}