# Appium 2 + Cucumber + Java + Maven Mobile Test Automation Framework

This is a beginner-friendly mobile test automation framework built with Appium 2, Cucumber, and Maven.
It supports testing on Android and iOS devices (emulators, simulators, and real devices).
The framework is designed to be easy to set up, understand, and extend.

## Features ✨
Cross-Platform: Works on both Android and iOS.

No Hardcoding: Device details (e.g., platformName, deviceName) are passed via command line.

Reusable Utilities: Common mobile actions like click, type, scroll, and wait are available in the ElementActions class.

Readable Tests: Uses Cucumber for behavior-driven development (BDD) with .feature files.

Extensible: Follows the Page Object Model (POM) for maintainable and scalable tests.

## Prerequisites 🛠️
Java JDK 8+

Maven 3.8+

Appium 2.x 

IntelliJ IDEA (or any Java IDE)

Android SDK (for Android) or Xcode (for iOS)

Emulator/Simulator or a Real Device

## Framework Structure 📂

```
src/test/
├── java/
│   ├── com/example/
│   │   ├── pages/          # Page Object Model classes
│   │   ├── runners/        # Test runners (TestNG/Cucumber)
│   │   ├── stepdefs/       # Cucumber step definitions
│   │   └── utils/          # Utilities (DriverManager, ElementActions)
├── resources/
│   ├── features/           # Cucumber .feature files
│   └── config.properties   # (Optional) Global configurations

```


## Setup 🚀
Clone the Repository:
git clone <repository-url>
cd <repository-folder>

Install Dependencies:
mvn clean install

Start Appium Server:
appium

Set Up Device:
For Android: Start an emulator or connect a real device.

For iOS: Start a simulator or connect a real device.

## Running Tests 🧪
### Command Line
Run tests with the following command:
Android:
mvn test -DplatformName=Android -DplatformVersion=14 -DdeviceName="emulator-5554" -DappPath="<apk/ipa_Path>"

Replace platformName with Android or iOS, and platformVersion with OS version.
Replace appPath with your app binary path.
Replace deviceName with your emulator/simulator or real device name.

To find OS version of android device- adb shell getprop ro.build.version.release

To find OS version of iOS device- ideviceinfo -k ProductVersion (if ideviceinfo is not installed, use 'brew install libimobiledevice' )

To find deviceName for a connected Android emulator or real device- adb shell getprop ro.product.model (If deviceName contains whitespace, escape using \)

For iPhones and Simulators, use UDID instead of deviceName.

mvn test -DplatformName=iOS -DplatformVersion=18.2 -DudId="977B9EA5-56E9-46D1-BBE7-56DA0CCD5CE2" -DappPath="<apk/ipa_Path>"

To find UDID for a connected iPhone- idevice_id -l (if idevice_id is not installed, use 'brew install libimobiledevice' )

To find UDID and OS version of simulator- xcrun simctl list devices | grep Booted



### IntelliJ IDEA
Go to Run > Edit Configurations.
Add a new Maven configuration.
Set the command line parameters in VM Options:
-DplatformName=Android -DplatformVersion=14 -DdeviceName="Pixel_6_Pro" -DappPath="<apkPath>"

## Writing Tests ✍️
Feature Files:
Add .feature files in src/test/resources/features

Step Definitions:
Add step definitions in src/test/java/com/example/tests/steps

Page Objects:
Add page classes in src/test/java/com/example/pages

## Utilities 🛠️
The ElementActions and CommonActions classes provide reusable methods for common mobile interactions.
AppLaunchHelper provides method to handle permissions and alerts on the app launch.

## Reports 📊
After running tests, Cucumber generates an HTML report in the target/ folder.
Open target/report.html to view the results.
Also, there is surefire-reports in target directory.


Happy Testing! 🎉
