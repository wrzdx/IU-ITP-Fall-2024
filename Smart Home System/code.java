import java.util.Scanner;

public class SmartHomeManagementSystem {
    // Constants representing the number of devices in the system.
    private static final int NUMBER_OF_LIGHTS = 4;
    private static final int NUMBER_OF_CAMERAS = 2;
    private static final int NUMBER_OF_HEATERS = 4;
    private static final int NUMBER_OF_SMART_DEVICES = NUMBER_OF_CAMERAS
            + NUMBER_OF_HEATERS + NUMBER_OF_LIGHTS;

    // Interface for devices that can be charged.
    public interface Chargeable {
        boolean isCharging(); // Checks if the device is charging.

        boolean startCharging(); // Starts charging the device.

        boolean stopCharging(); // Stops charging the device.
    }

    // Interface for devices that can be controlled (turned on/off).
    public interface Controllable {
        boolean turnOff(); // Turns the device off.

        boolean turnOn(); // Turns the device on.

        boolean isOn(); // Checks if the device is turned on.
    }

    // Enum representing the status of a device (ON or OFF).
    public enum Status {
        OFF, ON
    }

    // Enum for brightness levels of a light.
    public enum BrightnessLevel {
        HIGH, MEDIUM, LOW
    }

    // Enum for light colors.
    public enum LightColor {
        WHITE, YELLOW
    }

    // Abstract base class for all smart devices.
    public abstract static class SmartDevice implements Controllable {
        protected Status status; // Current status of the device.
        protected int deviceId; // Unique ID of the device.
        protected int numberOfDevices; // Total number of devices.

        public SmartDevice(Status status) {
            this.status = status;
        }

        // Abstract method to display the device's current status (implemented by subclasses).
        public abstract String displayStatus();

        // Getters and setters for device ID and status.
        public final int getDeviceId() {
            return deviceId;
        }

        public final void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public final Status getStatus() {
            return status;
        }

        public final void setStatus(Status status) {
            this.status = status;
        }

        // Implementation of turning the device on or off.
        public final boolean turnOff() {
            if (!isOn()) {
                return false; // If the device is already off, return false.
            }
            status = Status.OFF; // Set the status to OFF.
            return true;
        }

        public final boolean turnOn() {
            if (isOn()) {
                return false; // If the device is already on, return false.
            }
            status = Status.ON; // Set the status to ON.
            return true;
        }

        public final boolean isOn() {
            return status == Status.ON; // Check if the device is ON.
        }

        // Check access to the device's status (relevant for devices that are ON).
        public final boolean checkStatusAccess() {
            return isOn();
        }
    }

    // Class for a Light device, extending SmartDevice and implementing Chargeable.
    public static class Light extends SmartDevice implements Chargeable {
        private boolean charging; // Charging status of the light.
        private BrightnessLevel brightnessLevel; // Brightness level of the light.
        private LightColor lightColor; // Color of the light.

        // Constructor for the Light class.
        public Light(Status status, boolean charging,
                     BrightnessLevel brightnessLevel,
                     LightColor lightColor) {
            super(status);
            this.charging = charging;
            this.brightnessLevel = brightnessLevel;
            this.lightColor = lightColor;
        }

        // Methods for managing light color and brightness.
        public final LightColor getLightColor() {
            return lightColor;
        }

        public final void setLightColor(LightColor lightColor) {
            this.lightColor = lightColor;
            System.out.println("Light " + deviceId + " color is set to " + lightColor);
        }

        public final BrightnessLevel getBrightnessLevel() {
            return brightnessLevel;
        }

        public final void setBrightnessLevel(BrightnessLevel brightnessLevel) {
            this.brightnessLevel = brightnessLevel;
            System.out.println("Light " + deviceId + " brightness level is set to " + brightnessLevel);
        }

        // Implementation of Chargeable interface methods.
        public final boolean isCharging() {
            return charging;
        }

        public final boolean startCharging() {
            if (isCharging()) {
                System.out.println("Light " + deviceId
                        + " is already charging");
                return false;
            }
            charging = true;
            return true;
        }

        public final boolean stopCharging() {
            if (isCharging()) {
                charging = false;
                return true;
            }
            System.out.println("Light " + deviceId
                    + " is not charging");
            return false;
        }

        // Display the current status of the light.
        public final String displayStatus() {
            return "Light " + deviceId + " is "
                    + status + ", the color is "
                    + lightColor + ", the charging status is "
                    + charging
                    + ", and the brightness level is "
                    + brightnessLevel + ".";
        }
    }

    public static class Heater extends SmartDevice {
        // Constants defining the minimum and maximum temperature for the heater.
        private static final int MIN_HEATER_TEMP = 15;
        private static final int MAX_HEATER_TEMP = 30;
        private int temperature; // Current temperature of the heater.

        // Constructor for the Heater class, initializing the status and temperature.
        public Heater(Status status, int temperature) {
            super(status);
            this.temperature = temperature;
        }

        // Getter for the current temperature of the heater.
        public final int getTemperature() {
            return temperature;
        }

        // Method to set the heater's temperature within the allowed range.
        public final boolean setTemperature(int temperature) {
            if (temperature >= MIN_HEATER_TEMP && temperature <= MAX_HEATER_TEMP) {
                this.temperature = temperature;
                return true;
            }
            return false; // Return false if the temperature is out of range.
        }

        // Displays the current status and temperature of the heater.
        public final String displayStatus() {
            return "Heater " + deviceId + " is " + status
                    + " and the temperature is " + temperature + ".";
        }
    }

    public static class Camera extends SmartDevice implements Chargeable {
        // Constants defining the minimum and maximum angle for the camera.
        private static final int MIN_CAMERA_ANGLE = -60;
        private static final int MAX_CAMERA_ANGLE = 60;
        private boolean charging; // Charging status of the camera.
        private boolean recording; // Recording status of the camera.
        private int angle; // Current angle of the camera.

        // Constructor for the Camera class, initializing status, charging, recording, and angle.
        public Camera(Status status, boolean charging, boolean recording, int angle) {
            super(status);
            this.charging = charging;
            this.recording = recording;
            this.angle = angle;
        }

        // Getter for the current angle of the camera.
        public final int getAngle() {
            return angle;
        }

        // Sets the camera angle within the allowed range.
        public final boolean setCameraAngle(int angle) {
            if (angle <= MAX_CAMERA_ANGLE && angle >= MIN_CAMERA_ANGLE) {
                this.angle = angle;
                return true;
            }
            return false; // Return false if the angle is out of range.
        }

        // Starts recording if the camera is not already recording.
        public final boolean startRecording() {
            if (isRecording()) {
                System.out.println("Camera " + deviceId + " is already recording");
                return false;
            }
            recording = true;
            return true;
        }

        // Stops recording if the camera is currently recording.
        public final boolean stopRecording() {
            if (isRecording()) {
                recording = false;
                return true;
            }
            System.out.println("Camera " + deviceId + " is not recording");
            return false;
        }

        // Checks if the camera is currently recording.
        public final boolean isRecording() {
            return recording;
        }

        // Implementation of the Chargeable interface to check if the camera is charging.
        public final boolean isCharging() {
            return charging;
        }

        // Starts charging the camera if it's not already charging.
        public final boolean startCharging() {
            if (isCharging()) {
                System.out.println("Camera " + deviceId + " is already charging");
                return false;
            }
            charging = true;
            return true;
        }

        // Stops charging the camera if it's currently charging.
        public final boolean stopCharging() {
            if (isCharging()) {
                charging = false;
                return true;
            }
            System.out.println("Camera " + deviceId + " is not charging");
            return false;
        }

        // Displays the current status, angle, charging, and recording status of the camera.
        public final String displayStatus() {
            return "Camera " + deviceId + " is " + status
                    + ", the angle is " + angle
                    + ", the charging status is " + charging
                    + ", and the recording status is "
                    + (recording && (charging || isOn())) + ".";
        }
    }

    // Initializes an array of Light devices with default settings.
    public static Light[] initializeLights() {
        Light[] lightDevices = new Light[NUMBER_OF_LIGHTS];

        for (int i = 0; i < NUMBER_OF_LIGHTS; i++) {
            lightDevices[i] = new Light(Status.ON, false, BrightnessLevel.LOW, LightColor.YELLOW);
        }

        return lightDevices;
    }

    // Initializes an array of Camera devices with default settings.
    public static Camera[] initializeCameras() {
        Camera[] cameraDevices = new Camera[NUMBER_OF_CAMERAS];
        final int initialAngle = 45; // Default camera angle.

        for (int i = 0; i < NUMBER_OF_CAMERAS; i++) {
            cameraDevices[i] = new Camera(Status.ON, false, false, initialAngle);
        }

        return cameraDevices;
    }

    // Initializes an array of Heater devices with default settings.
    public static Heater[] initializeHeaters() {
        Heater[] heaterDevices = new Heater[NUMBER_OF_HEATERS];
        final int initialTemperature = 20; // Default temperature for heaters.

        for (int i = 0; i < NUMBER_OF_HEATERS; i++) {
            heaterDevices[i] = new Heater(Status.ON, initialTemperature);
        }

        return heaterDevices;
    }

    // Combines all smart devices (lights, cameras, and heaters) into a single array.
    public static SmartDevice[] initializeSmartDevices() {
        SmartDevice[] smartDevices = new SmartDevice[NUMBER_OF_SMART_DEVICES];
        Light[] lightDevices = initializeLights();
        Camera[] cameraDevices = initializeCameras();
        Heater[] heaterDevices = initializeHeaters();

        int index = 0;
        System.arraycopy(lightDevices, 0, smartDevices, index, NUMBER_OF_LIGHTS);
        index += NUMBER_OF_LIGHTS;
        System.arraycopy(cameraDevices, 0, smartDevices, index, NUMBER_OF_CAMERAS);
        index += NUMBER_OF_CAMERAS;
        System.arraycopy(heaterDevices, 0, smartDevices, index, NUMBER_OF_HEATERS);

        return smartDevices;
    }

    // Checks if a device with the given name and ID exists in the system.
    public static boolean isNotThereInSystem(String deviceName, int deviceId) {
        switch (deviceName) {
            case "Light":
                if (deviceId >= 0 && deviceId < NUMBER_OF_LIGHTS) {
                    return false;
                }
                System.out.println("The smart device was not found");
                return true;
            case "Camera":
                if (deviceId >= NUMBER_OF_LIGHTS && deviceId < NUMBER_OF_LIGHTS + NUMBER_OF_CAMERAS) {
                    return false;
                }
                System.out.println("The smart device was not found");
                return true;
            case "Heater":
                if (deviceId >= NUMBER_OF_LIGHTS + NUMBER_OF_CAMERAS && deviceId < NUMBER_OF_SMART_DEVICES) {
                    return false;
                }
            default:
                System.out.println("The smart device was not found");
                return true;
        }
    }

    // Displays the status of all smart devices in the system.
    public static void displayAllStatus(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 1; // Expected number of command terms.
        if (commandTerms.length != numberOfFields) {
            printInvalidCommand(); // Print an error if the command is invalid.
            return;
        }

        for (int i = 0; i < NUMBER_OF_SMART_DEVICES; i++) {
            smartDevices[i].setDeviceId(i); // Assign IDs to devices dynamically.
            System.out.println(smartDevices[i].displayStatus()); // Print the status of each device.
        }
    }


    // Method to turn on a smart device
    public static void turnOn(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 3;

        // Validate command input: must have exactly 3 terms, and the 3rd term must be a number
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand(); // Print an error message if validation fails
            return;
        }

        String deviceName = commandTerms[1]; // Extract the device name
        int deviceId = Integer.parseInt(commandTerms[2]); // Parse the device ID

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return; // Exit if the device is not found
        }

        // Attempt to turn the device on
        if (smartDevices[deviceId].turnOn()) {
            System.out.println(deviceName + " " + deviceId + " is on");
        } else {
            System.out.println(deviceName + " " + deviceId + " is already on");
        }
    }

    // Method to turn off a smart device
    public static void turnOff(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 3;

        // Validate command input
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand();
            return;
        }

        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return;
        }

        smartDevices[deviceId].setDeviceId(deviceId); // Set the device ID for further actions

        // Attempt to turn the device off
        if (smartDevices[deviceId].turnOff()) {
            System.out.println(deviceName + " " + deviceId + " is off");
        } else {
            System.out.println(deviceName + " " + deviceId + " is already off");
        }
    }

    // Method to start charging a chargeable device
    public static void startCharging(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 3;

        // Validate command input
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand();
            return;
        }

        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return;
        }

        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is chargeable
        if (!(smartDevices[deviceId] instanceof Chargeable)) {
            System.out.println(deviceName + " " + deviceId + " is not chargeable");
            return;
        }

        // Start charging
        if (((Chargeable) smartDevices[deviceId]).startCharging()) {
            System.out.println(deviceName + " " + deviceId + " is charging");
        }
    }

    // Method to stop charging a chargeable device
    public static void stopCharging(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 3;

        // Validate command input
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand();
            return;
        }

        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return;
        }

        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is chargeable
        if (!(smartDevices[deviceId] instanceof Chargeable)) {
            System.out.println(deviceName + " " + deviceId + " is not chargeable");
            return;
        }

        // Stop charging
        if (((Chargeable) smartDevices[deviceId]).stopCharging()) {
            System.out.println(deviceName + " " + deviceId + " stopped charging");
        }
    }

    // Method to set the temperature of a Heater device
    public static void setTemperature(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 4;

        // Validate command input: must have 4 terms, and the 3rd and 4th terms must be numbers
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2]) || isNotNumber(commandTerms[3])) {
            printInvalidCommand();
            return;
        }

        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return;
        }

        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is a Heater
        if (!(smartDevices[deviceId] instanceof Heater)) {
            System.out.println(deviceName + " " + deviceId + " is not a heater");
            return;
        }

        int temperature = Integer.parseInt(commandTerms[3]);

        // Check access status and attempt to set temperature
        if (smartDevices[deviceId].checkStatusAccess()) {
            if (((Heater) smartDevices[deviceId]).setTemperature(temperature)) {
                System.out.println(deviceName + " " + deviceId + " temperature is set to " + temperature);
            } else {
                System.out.println("Heater " + deviceId + " temperature should be in the range [15, 30]");
            }
        } else {
            printNotStatusAccess(deviceName, deviceId);
        }
    }

    // Method to set the brightness level of a Light device
    public static void setBrightness(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 4;

        // Validate command input
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand();
            return;
        }

        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return;
        }

        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is a Light
        if (!(smartDevices[deviceId] instanceof Light)) {
            System.out.println(deviceName + " " + deviceId + " is not a light");
            return;
        }

        // Parse the brightness level
        BrightnessLevel brightnessLevel;
        switch (commandTerms[3]) {
            case "LOW":
                brightnessLevel = BrightnessLevel.LOW;
                break;
            case "MEDIUM":
                brightnessLevel = BrightnessLevel.MEDIUM;
                break;
            case "HIGH":
                brightnessLevel = BrightnessLevel.HIGH;
                break;
            default:
                System.out.println("The brightness can only be one of \"LOW\", \"MEDIUM\", or \"HIGH\"");
                return;
        }

        // Check access status or charging status, and set brightness level
        if (smartDevices[deviceId].checkStatusAccess() || ((Light) smartDevices[deviceId]).isCharging()) {
            ((Light) smartDevices[deviceId]).setBrightnessLevel(brightnessLevel);
        } else {
            printNotStatusAccess(deviceName, deviceId);
        }
    }

    // Method to set the color of a Light device
    public static void setColor(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 4;

        // Validate command input
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand();
            return;
        }

        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return;
        }

        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is a Light
        if (!(smartDevices[deviceId] instanceof Light)) {
            System.out.println(deviceName + " " + deviceId + " is not a light");
            return;
        }

        // Parse the light color
        LightColor lightColor;
        switch (commandTerms[3]) {
            case "WHITE":
                lightColor = LightColor.WHITE;
                break;
            case "YELLOW":
                lightColor = LightColor.YELLOW;
                break;
            default:
                System.out.println("The light color can only be \"YELLOW\" or \"WHITE\"");
                return;
        }

        // Check access status or charging status, and set light color
        if (smartDevices[deviceId].checkStatusAccess() || ((Light) smartDevices[deviceId]).isCharging()) {
            ((Light) smartDevices[deviceId]).setLightColor(lightColor);
        } else {
            printNotStatusAccess(deviceName, deviceId);
        }
    }

    // Method to set the angle of a Camera device
    public static void setAngle(String[] commandTerms, SmartDevice[] smartDevices) {
        final int numberOfFields = 4;

        // Validate command input
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2]) || isNotNumber(commandTerms[3])) {
            printInvalidCommand();
            return;
        }

        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return;
        }

        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is a Camera
        if (!(smartDevices[deviceId] instanceof Camera)) {
            System.out.println(deviceName + " " + deviceId + " is not a camera");
            return;
        }

        int angle = Integer.parseInt(commandTerms[3]);

        // Check access status or charging status, and set camera angle
        if (smartDevices[deviceId].checkStatusAccess() || ((Camera) smartDevices[deviceId]).isCharging()) {
            if (((Camera) smartDevices[deviceId]).setCameraAngle(angle)) {
                System.out.println(deviceName + " " + deviceId + " angle is set to " + angle);
            } else {
                System.out.println("Camera " + deviceId + " angle should be in the range [-60, 60]");
            }
        } else {
            printNotStatusAccess(deviceName, deviceId);
        }
    }

    public static void startRecording(String[] commandTerms, SmartDevice[] smartDevices) {
        // Define the required number of fields for the command
        final int numberOfFields = 3;

        // Validate the command length and the third field as a number
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand(); // Print error if command is invalid
            return; // Exit the method
        }

        // Extract device name and ID from the command
        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return; // Exit if the device is not found
        }

        // Set the device ID in the smart device instance
        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is of type Camera
        if (!(smartDevices[deviceId] instanceof Camera)) {
            System.out.println(deviceName + " " + deviceId + " is not a camera"); // Error if not a camera
            return; // Exit the method
        }

        // Check if the device can start recording based on its status
        if (smartDevices[deviceId].checkStatusAccess() || ((Camera) smartDevices[deviceId]).isCharging()) {
            if (((Camera) smartDevices[deviceId]).startRecording()) {
                System.out.println(deviceName + " " + deviceId + " started recording"); // Recording started
            }
        } else {
            printNotStatusAccess(deviceName, deviceId); // Error if the device is off
        }
    }

    public static void stopRecording(String[] commandTerms, SmartDevice[] smartDevices) {
        // Define the required number of fields for the command
        final int numberOfFields = 3;

        // Validate the command length and the third field as a number
        if (commandTerms.length != numberOfFields || isNotNumber(commandTerms[2])) {
            printInvalidCommand(); // Print error if command is invalid
            return; // Exit the method
        }

        // Extract device name and ID from the command
        String deviceName = commandTerms[1];
        int deviceId = Integer.parseInt(commandTerms[2]);

        // Check if the device exists in the system
        if (isNotThereInSystem(deviceName, deviceId)) {
            return; // Exit if the device is not found
        }

        // Set the device ID in the smart device instance
        smartDevices[deviceId].setDeviceId(deviceId);

        // Check if the device is of type Camera
        if (!(smartDevices[deviceId] instanceof Camera)) {
            System.out.println(deviceName + " " + deviceId + " is not a camera"); // Error if not a camera
            return; // Exit the method
        }

        // Check if the device can stop recording based on its status
        if (smartDevices[deviceId].checkStatusAccess() || ((Camera) smartDevices[deviceId]).isCharging()) {
            if (((Camera) smartDevices[deviceId]).stopRecording()) {
                System.out.println(deviceName + " " + deviceId + " stopped recording"); // Recording stopped
            }
        } else {
            printNotStatusAccess(deviceName, deviceId); // Error if the device is off
        }
    }

    public static boolean isNotNumber(String str) {
        // Check if the given string can be parsed as an integer
        try {
            Integer.parseInt(str); // Attempt to parse the string
        } catch (Exception e) {
            return true; // Return true if parsing fails
        }
        return false; // Return false if parsing succeeds
    }

    public static void printNotStatusAccess(String deviceName, int deviceId) {
        // Print a message indicating the device is off and cannot be accessed
        System.out.println("You can't change the "
                + "status of the " + deviceName + " "
                + deviceId + " while it is off");
    }

    public static void printInvalidCommand() {
        // Print a message indicating the command is invalid
        System.out.println("Invalid command");
    }

    public static void main(String[] args) {
        // Initialize the smart devices
        SmartDevice[] smartDevices = initializeSmartDevices();
        Scanner scanner = new Scanner(System.in);

        // Infinite loop to process user commands
        while (true) {
            String commandLine = scanner.nextLine(); // Read user input
            String[] commandTerms = commandLine.split(" "); // Split input into command terms
            String command = commandTerms[0]; // Extract the command name

            // Exit the loop if the "end" command is issued
            if (command.equals("end")) {
                break;
            }

            // Handle different commands using a switch statement
            switch (command) {
                case "DisplayAllStatus":
                    displayAllStatus(commandTerms, smartDevices); // Display device statuses
                    break;
                case "TurnOn":
                    turnOn(commandTerms, smartDevices); // Turn on a device
                    break;
                case "TurnOff":
                    turnOff(commandTerms, smartDevices); // Turn off a device
                    break;
                case "StartCharging":
                    startCharging(commandTerms, smartDevices); // Start charging a device
                    break;
                case "StopCharging":
                    stopCharging(commandTerms, smartDevices); // Stop charging a device
                    break;
                case "SetTemperature":
                    setTemperature(commandTerms, smartDevices); // Set temperature for a heater
                    break;
                case "SetBrightness":
                    setBrightness(commandTerms, smartDevices); // Set brightness for a light
                    break;
                case "SetColor":
                    setColor(commandTerms, smartDevices); // Set color for a light
                    break;
                case "SetAngle":
                    setAngle(commandTerms, smartDevices); // Set angle for a camera
                    break;
                case "StartRecording":
                    startRecording(commandTerms, smartDevices); // Start recording for a camera
                    break;
                case "StopRecording":
                    stopRecording(commandTerms, smartDevices); // Stop recording for a camera
                    break;
                default:
                    printInvalidCommand(); // Handle invalid commands
            }
        }
    }
}
