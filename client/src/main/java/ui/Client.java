package ui;

import java.util.Scanner;

public class Client {

    public void run() {
        System.out.println("hi");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            System.out.print("\n" + ">>> ");
            String line = scanner.nextLine();

        }
    }
}
