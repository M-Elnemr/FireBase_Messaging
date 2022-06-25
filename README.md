# FireBase_Messaging
it's a show case application for sending and receiving firebase messaging notifications on android without backend (only firebase API) 

- subscribe to a topic
- can send notification to a topic or a token


<img src="https://user-images.githubusercontent.com/50822992/175774060-b14321fb-f854-4444-be22-b485093b5c7c.jpg" alt="alt text" width="250">

# Major Steps

1- Create a Service that extends FirebaseMessagingService()

2- override onMessageReceived and onNewToken

3- don't forget to declare the service in the manifest file with that intent-filter

        <service android:name=".FireBaseService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT"/>
            </intent-filter>
        </service>

