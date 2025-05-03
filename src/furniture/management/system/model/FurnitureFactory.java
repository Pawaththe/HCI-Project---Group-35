import furniture.management.system.model.Furniture;

public class FurnitureFactory {
    public FurnitureFactory() {
    }

    public static Furniture create(String type) {
        switch (type) {
            case "CoffeeTable1":
            case "CoffeeTable2":
            case "CoffeeTable3":
                return new CoffeeTableManager(type);
            default:
                throw new IllegalArgumentException("Unknown furniture: " + type);
        }
    }

    public static Furniture2D create2D(String type) {
        switch (type) {
            case "CoffeeTable1":
            case "CoffeeTable2":
            case "CoffeeTable3":
                return new CoffeeTableManager.CoffeeTable2D(type);
            default:
                throw new IllegalArgumentException("Unknown 2D furniture: " + type);
        }
    }
}