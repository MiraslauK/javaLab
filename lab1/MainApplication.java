public class MainApplication {

    public static void main(String[] args) throws Exception {
        Food[] breakfast = new Food[20];
        int Cheesecount = 0;
        int Applecount = 0;
        int PieCount = 0;

        for (int itemIndex = 0; itemIndex < args.length; itemIndex++) {
            String[] parts = args[itemIndex].split("/");
            if (parts[0].equals("Cheese")) {
                breakfast[itemIndex] = new Cheese();
            } else if (parts[0].equals("Apple")) {
                breakfast[itemIndex] = new Apple(parts[1]);
            } else if (parts[0].equals("Pie")) {
                breakfast[itemIndex] = new Pie(parts[1]);
            }
        }

        for (Food item : breakfast) {
            if (item == null) break;
            item.consume();
        }

        Apple apple1 = new Apple("");
        Cheese cheese1 = new Cheese();
        Pie pie1 = new Pie("");

        for (int i = 0; i < args.length; i++) {
            if (breakfast[i] != null) {
                if (breakfast[i].equals(apple1)) {
                    Applecount++;
                } else if (breakfast[i].equals(cheese1)) {
                    Cheesecount++;
                } else if (breakfast[i].equals(pie1)) {
                    PieCount++;
                }
            }
        }

        if (Cheesecount > 0) {
            System.out.println("Cheese eaten: " + Cheesecount);
        }
        if (Applecount > 0) {
            System.out.println("Apples eaten: " + Applecount);
        }
        if (PieCount > 0) {
            System.out.println("Pie's eaten: " + PieCount);
        }
        System.out.println("Good bye!");
    }
}
