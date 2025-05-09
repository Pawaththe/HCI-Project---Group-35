package furniture.management.system.model;

import furniture.management.system.model.*;

public class FurnitureFactory {

    public static Furniture create(String type) {
        switch (type) {
            case "CoffeeTable1":
            case "CoffeeTable2":
            case "CoffeeTable3":
                return new CoffeeTableManager(type);
            case "ComputerDesk":
                return new ComputerDeskManager();
            case "DiningChair":
                return new DiningChairManager();
            case "DiningTable":
                return new DiningTableManager();
            case "PantryCupboard":
                return new PantryCupboardManager();
            case "TvStand":
                return new TvStandManager();
            case "Wardrobe":
                return new WardrobeManager();
            case "Bed":
                return new BedManager();
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
            case "ComputerDesk":
                return new ComputerDeskManager.ComputerDesk2D();
            case "DiningChair":
                return new DiningChairManager.DiningChair2D();
            case "DiningTable":
                return new DiningTableManager.DiningTable2D();
            case "PantryCupboard":
                return new PantryCupboardManager.PantryCupboard2D();
            case "TvStand":
                return new TvStandManager.TvStand2D();
            case "Wardrobe":
                return new WardrobeManager.Wardrobe2D();
            case "Bed":
                return new BedManager.Bed2D();
            default:
                throw new IllegalArgumentException("Unknown 2D furniture: " + type);
        }
    }
}