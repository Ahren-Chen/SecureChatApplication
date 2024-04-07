# SecureChatApplication
The final group project for 3A04. Designing a prototype for a secure chat application utilizing a Key distribution center, symmetric key encryptions, and a mediated authentication protocol.

## Icons:
- Home: https://www.flaticon.com/free-icons/menu. Icon created by Fengquanli - Flaticon
- Messages:https://www.flaticon.com/free-icons/message. Message icon created by Freepik - Flaticon
- Calls: https://www.flaticon.com/free-icons/android. Call icon created by Afdalul Zikri - Flaticon
- Account: https://www.flaticon.com/free-icons/user. User account icon created by Freepik - Flaticon
- Survey: https://www.flaticon.com/free-icons/questionnaire. Survey icon created by IYIKON - Flaticon

# To run on CMD:
1. Ensure that Java JDK version 8 or higher is installed. run `java -version` and see that it is "1.8.____"
2. Ensure Gradle is installed and on the PATH. Check by running `gradle -v`. Can install from https://gradle.org/install/
3. Use gitbash or equivalent and go to the folder for this application (SecureChatApplication)
4. Run `gradle wrapper` in the main folder of SecureChatApplication
5. Run `./gradlew build`
6. Run `cd server/bin/main`
7. Run `java com/example/server/KDC`
8. Open up another gitbash and repeat previous step 6
9. Run `java com/example/server/AccountManagement` on the new gitbash

10. Install `cmdline-tools` from Android Studio tools package and extract the zip anywhere. Can also locate an existing version of it (Commonly found in `Program Files (x86)/Android/android-sdk`)
11. launching gitbash as admin and locate the `cmdline-tools` folder. 
12. Run `./sdkmanager platform-tools emulator` or `./sdkmanager.bat platform-tools emulator`
13. Create Environment Variables `ANDROID_SDK_ROOT=`Path of the `cmdline-tools` folder. Same with `ANDROID_HOME`
14. Add `emulator` directory, `platform-tools` directory, and `cmdline-tools/{version num}/bin` to PATH and restart gitbash in admin mode
15. Run `./sdkmanager platforms;android-34` in `cmdline-tools` folder
16. Run `./sdkmanager build-tools;34.0.0`
17. Run `./sdkmanager system-images;android-34;google_apis;x86_64`
18. Run `avdmanager create avd --name android34 --package "system-images;android-34;google_apis;x86_64"` and use default settings
19. Run `emulator -avd android34`
20. Navigate to where the apk file is stored and run `abd install app-release.apk`

# Alternate way to run:
1. Ensure that Java JDK version 8 or higher is installed. run `java -version` and see that it is "1.8.____"
2. Ensure Gradle is installed and on the PATH. Check by running `gradle -v`. Can install from https://gradle.org/install/
3. Use gitbash or equivalent and go to the folder for this application (SecureChatApplication)
4. Run `gradle wrapper` in the main folder of SecureChatApplication
5. Run `./gradlew build`
6. Run `cd server/bin/main`
7. Run `java com/example/server/KDC`
8. Open up another gitbash and repeat previous step 6
9. Run `java com/example/server/AccountManagement` on the new gitbash

10. Open the project in Android Studio, create an emulator in device manager, run module SecureChatApplication.app.main

# When making APK: password is 'admin1'