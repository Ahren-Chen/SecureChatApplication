# SecureChatApplication
The final group project for 3A04. Designing a prototype for a secure chat application utilizing a Key distribution center, symmetric key encryptions, and a mediated authentication protocol.

## Responsibilities:
- Safwan: The survey system
- Ahren: Calls + Mediated Authentication Protocol
- Jiaming: Messaging system
- Jacob: Account management system
- Richard: KDC + Encryption system

## To do list:
### Ahren:
- ~~Create basic landing page UI~~
- Create the calling UI (Will not be functional)
- Create the mediated authenciation protocol

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