import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Reads animal data from a file and creates a list of Animal objects
    private static List<Animal> readAnimals() {
        String line = "0";
        List<Animal> animals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int n;
            for (int i = 0; i < 3; i++) { // Skip the first 3 lines
                line = reader.readLine();
            }
            n = Integer.parseInt(line); // Read the number of animals
            for (int i = 0; i < n; i++) {
                line = reader.readLine();
                if (line == null) {
                    throw new InvalidInputsException(); // Null line indicates invalid input
                }
                String[] parts = line.split(" "); // Split the line into components
                if (parts.length != 4) {
                    throw new InvalidNumberOfAnimalParametersException(); // Ensure correct number of parameters
                }
                String type = parts[0];
                float weight = Float.parseFloat(parts[1].replace("F", "")); // Parse weight
                float speed = Float.parseFloat(parts[2].replace("F", "")); // Parse speed
                float energy = Float.parseFloat(parts[3].replace("F", "")); // Parse energy

                // Instantiate the appropriate Animal subclass
                switch (type) {
                    case "Lion" -> animals.add(new Lion(weight, speed, energy));
                    case "Zebra" -> animals.add(new Zebra(weight, speed, energy));
                    case "Boar" -> animals.add(new Boar(weight, speed, energy));
                    default -> throw new InvalidInputsException(); // Unknown animal type
                }
            }
            if (reader.readLine() != null) {
                throw new InvalidInputsException(); // Extra lines after expected input
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Handle file read errors
        }
        return animals;
    }

    // Prints the sounds made by each animal
    public static void printAnimals(List<Animal> animals) {
        animals.forEach(Animal::makeSound);
    }

    // Removes animals with zero or negative energy
    public static void removeDeadAnimals(List<Animal> animals) {
        animals.removeIf(animal -> animal.energy <= 0);
    }

    // Runs the simulation for a specified number of days
    public static void runSimulation(int days, float grassAmount, List<Animal> animals) {
        Field field;
        try {
            field = new Field(grassAmount); // Initialize the field with grass
        } catch (RuntimeException e) {
            System.out.println(e.getMessage()); // Handle invalid grass amount
            return;
        }
        removeDeadAnimals(animals); // Remove animals with no energy
        for (int i = 0; i < days; i++) {
            for (Animal animal : animals) {
                if (animal.energy == 0) {
                    continue; // Skip animals with zero energy
                }
                animal.eat(animals, field); // Simulate eating
            }
            animals.forEach(Animal::decrementEnergy); // Decrease energy of all animals
            removeDeadAnimals(animals); // Remove dead animals after energy decrement
            field.makeGrassGrow(); // Grow grass in the field
        }
        printAnimals(animals); // Print the remaining animals
    }

    public static void main(String[] args) {
        int days;
        final int minDays = 1;
        final int maxDays = 30;
        float grassAmount;
        String[] data = new String[2];

        // Read simulation parameters from file
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            data[0] = reader.readLine(); // Read number of days
            data[1] = reader.readLine(); // Read initial grass amount
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            days = Integer.parseInt(data[0]); // Parse number of days
            grassAmount = Float.parseFloat(data[1].replace("F", "")); // Parse grass amount
            if (days < minDays || days > maxDays) {
                throw new InvalidInputsException(); // Ensure days are within bounds
            }
        } catch (RuntimeException e) {
            RuntimeException ex = new InvalidInputsException();
            System.out.println(ex.getMessage());
            return;
        }
        List<Animal> animals;
        try {
            animals = readAnimals(); // Read animals from file
        } catch (RuntimeException e) {
            RuntimeException ex = e;
            if (ex instanceof NumberFormatException) {
                ex = new InvalidInputsException();
            }
            System.out.println(ex.getMessage());
            return;
        }
        runSimulation(days, grassAmount, animals); // Run the simulation
    }

    // Zebra class, representing herbivorous animals
    public static class Zebra extends Animal implements Herbivore {
        public Zebra(float weight, float speed, float energy) {
            super(weight, speed, energy);
        }

        @Override
        public final void eat(List<Animal> animals, Field field) {
            grazeInTheField(this, field); // Eat grass from the field
        }
    }

    // Exception for invalid weight
    public static final class WeightOutputOfBoundsException extends RuntimeException {
        @Override
        public String getMessage() {
            return "The weight is out of bounds";
        }
    }

    // Exception for prey that is too strong
    public static final class TooStrongPreyException extends RuntimeException {
        @Override
        public String getMessage() {
            return "The prey is too strong or too fast to attack";
        }
    }

    // Exception for invalid speed
    public static final class SpeedOutOfBoundsException extends RuntimeException {
        @Override
        public String getMessage() {
            return "The speed is out of bounds";
        }
    }

    // Exception for self-hunting scenarios
    public static final class SelfHuntingException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Self-hunting is not allowed";
        }
    }

    // Interface for omnivorous animals
    public interface Omnivore<T extends Animal> extends Carnivore<T>, Herbivore {
    }

    // Abstract class representing an animal
    public abstract static class Animal {
        private static final float MIN_SPEED = 5;
        private static final float MAX_SPEED = 60;
        private static final float MIN_ENERGY = 0;
        private static final float MAX_ENERGY = 100;
        private static final float MIN_WEIGHT = 5;
        private static final float MAX_WEIGHT = 200;
        protected float weight;
        protected float speed;
        protected float energy;

        // Constructor for the Animal class, ensuring valid input parameters for weight, speed, and energy
        protected Animal(float weight, float speed, float energy) {
            RuntimeException e = null; // Placeholder for potential exception

            // Check if the weight is out of bounds
            if (weight > MAX_WEIGHT || weight < MIN_WEIGHT) {
                e = new WeightOutputOfBoundsException();
            } else if (speed > MAX_SPEED || speed < MIN_SPEED) {
                // Check if the speed is out of bounds
                e = new SpeedOutOfBoundsException();
            } else if (energy > MAX_ENERGY || energy < MIN_ENERGY) {
                // Check if the energy is out of bounds
                e = new EnergyOutputOfBoundsException();
            }

            // If any parameter is invalid, throw the appropriate exception
            if (e != null) {
                throw e;
            }

            // Initialize the animal's attributes if all parameters are valid
            this.weight = weight;
            this.speed = speed;
            this.energy = energy;
        }


        // Print the sound made by the animal
        public final void makeSound() {
            String name = this.getClass().getSimpleName();
            String sound = switch (name) {
                case "Lion" -> AnimalSound.LION.getSound();
                case "Boar" -> AnimalSound.BOAR.getSound();
                case "Zebra" -> AnimalSound.ZEBRA.getSound();
                default -> "Unknown sound";
            };
            System.out.println(sound);
        }

        // Decrease the energy of the animal
        public final void decrementEnergy() {
            energy -= 1;
        }

        // Abstract method for eating behavior
        public abstract void eat(List<Animal> animals, Field field);
    }

    // Enum representing animal sounds
    public enum AnimalSound {
        LION("Roar"), ZEBRA("Ihoho"), BOAR("Oink");
        private final String sound;

        AnimalSound(String sound) {
            this.sound = sound;
        }

        public String getSound() {
            return sound;
        }
    }

    // Boar class, representing omnivorous animals
    public static class Boar extends Animal implements Omnivore<Boar> {
        public Boar(float weight, float speed, float energy) {
            super(weight, speed, energy);
        }

        @Override
        public final void eat(List<Animal> animals, Field field) {
            grazeInTheField(this, field); // Eat grass from the field
            try {
                Animal prey = choosePrey(animals, this); // Choose prey to hunt
                huntPrey(this, prey); // Attempt to hunt the prey
            } catch (SelfHuntingException | CannibalismException | TooStrongPreyException e) {
                System.out.println(e.getMessage()); // Handle hunting exceptions
            }
        }
    }

    // Exception for cannibalism attempts
    public static final class CannibalismException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Cannibalism is not allowed";
        }
    }

    // Interface for carnivorous animals
    public interface Carnivore<T extends Animal> {
        default Animal choosePrey(List<Animal> animals, T hunter) {
            int preyIndex = (animals.indexOf(hunter) + 1) % animals.size();
            Animal prey = animals.get(preyIndex);
            if (prey == hunter) {
                throw new SelfHuntingException();
            }
            if (prey.getClass() == hunter.getClass()) {
                throw new CannibalismException();
            }
            if (prey.energy >= hunter.energy && prey.speed >= hunter.speed) {
                throw new TooStrongPreyException();
            }
            return prey;
        }

        default void huntPrey(Animal hunter, Animal prey) {
            final float hundredPercent = 100F;
            hunter.energy = Math.min(hunter.energy + prey.weight, hundredPercent);
            prey.energy = 0;
        }
    }

    // Exception for invalid number of animal parameters
    public static final class InvalidNumberOfAnimalParametersException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Invalid number of animal parameters";
        }
    }

    // Lion class, representing carnivorous animals
    public static class Lion extends Animal implements Carnivore<Lion> {
        public Lion(float weight, float speed, float energy) {
            super(weight, speed, energy); // Initialize the lion with weight, speed, and energy
        }

        @Override
        public final void eat(List<Animal> animals, Field field) {
            try {
                // Select a prey from the list of animals and attempt to hunt it
                Animal prey = choosePrey(animals, this);
                huntPrey(this, prey);
            } catch (SelfHuntingException | CannibalismException | TooStrongPreyException e) {
                // Handle exceptions during hunting and print the error message
                System.out.println(e.getMessage());
            }
        }
    }

    // Exception thrown when energy is out of bounds
    public static final class EnergyOutputOfBoundsException extends RuntimeException {
        @Override
        public String getMessage() {
            return "The energy is out of bounds";
        }
    }

    // Field class, representing the area with grass for animals
    public static class Field {
        private float grassAmount; // Amount of grass available in the field

        public Field(float grassAmount) {
            final float max = 100F;
            final float min = 0F;
            // Validate grass amount to ensure it is within the valid range
            if (grassAmount < min || grassAmount > max) {
                throw new GrassOutOfBoundsException();
            }
            this.grassAmount = grassAmount;
        }

        // Simulates grass growth by doubling the current grass amount, up to a maximum limit
        public final void makeGrassGrow() {
            final float hundred = 100F;
            grassAmount = Math.min(grassAmount * 2, hundred);
        }

        // Returns the current amount of grass
        public final float getGrassAmount() {
            return grassAmount;
        }

        // Sets the grass amount to a new value
        public final void setGrassAmount(float grassAmount) {
            this.grassAmount = grassAmount;
        }
    }

    // Exception thrown when grass amount is out of bounds
    public static final class GrassOutOfBoundsException extends RuntimeException {
        @Override
        public String getMessage() {
            return "The grass is out of bounds";
        }
    }

    // Interface for herbivorous behavior
    public interface Herbivore {
        // Allows an animal to graze in the field, consuming grass and gaining energy
        default void grazeInTheField(Animal grazer, Field field) {
            final float ten = 10F;
            final float hundredPercent = 100F;
            float gainEnergy = grazer.weight / ten; // Calculate energy gain based on animal weight
            if (field.getGrassAmount() > gainEnergy) {
                // Increase grazer's energy and decrease field's grass amount
                grazer.energy = Math.min(grazer.energy + gainEnergy, hundredPercent);
                field.setGrassAmount(field.getGrassAmount() - gainEnergy);
            }
        }
    }

    // Exception for invalid input parameters
    public static final class InvalidInputsException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Invalid inputs";
        }
    }
}
 