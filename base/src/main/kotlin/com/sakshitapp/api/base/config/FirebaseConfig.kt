package com.sakshitapp.api.base.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.IOException

@Configuration
class FirebaseConfig {

    @Primary
    @Bean
    @Throws(IOException::class)
    fun getFirebaseApp(): FirebaseApp {
        val googleCredentials: String = System.getenv("GOOGLE_APP_CREDENTIALS")
        val dbUrl: String = System.getenv("FIREBASE_DATABASE_URL")
        val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(googleCredentials.byteInputStream()))
                .setDatabaseUrl(dbUrl)
                .build()
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
        return FirebaseApp.getInstance()
    }

    @get:Throws(IOException::class)
    @get:Bean
    val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance(getFirebaseApp())

    @Bean
    @Throws(IOException::class)
    fun firebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @get:Throws(IOException::class)
    @get:Bean
    val database: Firestore
        get() {
            val firestoreOptions = FirestoreOptions.newBuilder()
                    .setCredentials(GoogleCredentials.getApplicationDefault()).build()
            return firestoreOptions.service
        }

    @get:Throws(IOException::class)
    @get:Bean
    val messaging: FirebaseMessaging
        get() = FirebaseMessaging.getInstance(getFirebaseApp())

    @get:Throws(IOException::class)
    @get:Bean
    val remoteConfig: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance(getFirebaseApp())
}