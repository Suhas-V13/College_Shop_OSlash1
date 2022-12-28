import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;


public class Geektrust {

    static final String error = "ERROR_QUANTITY_EXCEEDED";
    static final String success = "ITEM_ADDED";
    static final int tax = 10;
    static double netPrice = 0f;
    static double discount = 0f;
    static int clothCount = 0;
    static int stationaryCount = 0;

    static HashMap<String, Item> priceList = new HashMap<>();
    static ArrayList<CartItem> cart = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // the file to be opened for reading
            FileInputStream fis = new FileInputStream(args[0]);
            Scanner sc = new Scanner(fis); // file to be scanned
            // returns true if there is another line to read
            while (sc.hasNextLine()) {
                String inputCommand = sc.nextLine();
                //Add your code here to process input commands.

                String[] inputArray = inputCommand.split(" ");

                initSystem();

                if (Objects.equals(inputArray[0], "ADD_ITEM")) {
                    String itemName = inputArray[1];
                    int quantity = Integer.parseInt(inputArray[2]);
                    addItem(itemName, quantity);
                }

                String output = ""; //process the input command and get the output

                if (Objects.equals(inputArray[0], "PRINT_BILL")) {
                    applyDiscount();
                    applySaleTax();
                    output += "TOTAL_DISCOUNT " + discount;
                    output += "\nTOTAL_AMOUNT_TO_PAY " + netPrice;
                }

                //Once it is processed print the output using the command System.out.println()
                System.out.println(output);
            }
            sc.close(); // closes the scanner
        } catch (IOException ignored) {
        }
    }

    private static void applySaleTax() {
        double temp = (tax * netPrice) / 100;
        netPrice += temp;
    }

    private static void initSystem() {
        priceList.put("TSHIRT", new Item("TSHIRT", "Clothing", 1000, 10));
        priceList.put("JACKET", new Item("JACKET", "Clothing", 2000, 5));
        priceList.put("CAP", new Item("CAP", "Clothing", 500, 20));
        priceList.put("NOTEBOOK", new Item("NOTEBBOOK", "Stationary", 200, 20));
        priceList.put("PENS", new Item("PENS", "Stationary", 300, 10));
        priceList.put("MARKERS", new Item("MARKERS", "Stationary", 500, 5));
    }

    public static boolean check(String itemName, int quantity) {
        if (Objects.equals(itemName, "TSHIRT") || Objects.equals(itemName, "JACKET") || Objects.equals(itemName, "CAP")) {
            return (clothCount + quantity) <= 2;
        } else {
            return (stationaryCount + quantity) <= 3;
        }
    }

    public static void addItem(String itemName, int quantity) {

        if (check(itemName, quantity)) {
            Item item = priceList.get(itemName);
            System.out.println(success);
            cart.add(new CartItem(item, quantity));
            netPrice += (priceList.get(itemName).price * quantity);
            if (Objects.equals(item.getCategory(), "Clothing")) {
                clothCount+=quantity;
            } else {
                stationaryCount+=quantity;
            }
            System.out.println(netPrice);
//            return;
        } else {
            System.out.println(error);
            System.out.println(netPrice);
            return;
        }

    }

    public static void applyDiscount() {
        if (netPrice >= 1000) {
            for (CartItem cartItem :
                    cart) {
                discount += ((cartItem.item.discount * (cartItem.item.getPrice() * cartItem.getQuantity())) / 100);
            }
            netPrice -= discount;
        }

        if (netPrice >= 3000) {
            discount += ((netPrice * 5) / 100);
            netPrice -= discount;
        }

    }
}
