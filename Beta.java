// ===== Player.java =====
public class Player {
    private int hp = 20;
    private Room currentRoom;

    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void moveTo(Room room) {
        this.currentRoom = room;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}

// ===== Room.java =====
import java.util.HashMap;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits = new HashMap<>();

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getLongDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are in ").append(name).append(". ").append(description).append("\nExits: ");
        sb.append(String.join(", ", exits.keySet()));
        return sb.toString();
    }
}

// ===== Game.java =====
import java.util.Scanner;

public class Game {
    private Player player;
    private Scanner input = new Scanner(System.in);

    private void createWorld() {
        Room hall = new Room("the Entry Hall", "Cold torches flicker on damp stone walls.");
        Room armory = new Room("an Old Armory", "Rusty weapons line the racks.");
        Room treasury = new Room("the Treasury", "Gold glitters in broken chests—victory is near!");

        hall.setExit("north", armory);
        armory.setExit("south", hall);
        armory.setExit("east", treasury);
        treasury.setExit("west", armory);

        player = new Player(hall);
    }

    private void play() {
        System.out.println("Welcome to Depths of Shadows! Type 'help' for commands.\n");
        while (player.isAlive()) {
            System.out.println(player.getCurrentRoom().getLongDescription());
            System.out.print("\n> ");
            String command = input.nextLine().trim().toLowerCase();

            switch (command) {
                case "help" -> System.out.println("Commands: go <dir>, look, quit");
                case "look" -> System.out.println(player.getCurrentRoom().getLongDescription());
                case "quit" -> {
                    System.out.println("Thanks for playing!");
                    return;
                }
                default -> {
                    if (command.startsWith("go ")) {
                        String dir = command.substring(3);
                        Room next = player.getCurrentRoom().getExit(dir);
                        if (next == null) {
                            System.out.println("You can't go that way!");
                        } else {
                            player.moveTo(next);
                            if (next.getLongDescription().contains("Treasury")) {
                                System.out.println("\nYou found the treasure and won the game! 🎉");
                                return;
                            }
                        }
                    } else {
                        System.out.println("I don't understand that.");
                    }
                }
            }
        }
        System.out.println("You have perished in the dungeon. Game over.");
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.createWorld();
        g.play();
    }
}
