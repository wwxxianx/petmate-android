package com.example.petsapplication.model.service.impl

import android.net.Uri
import com.example.petsapplication.common.Constants
import com.example.petsapplication.model.Message
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.Response
import com.example.petsapplication.model.User
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorageService: FirebaseStorageService,
    private val authService: AuthService,
) : FirestoreService {
    private fun userCollectionRef(uid: String): DocumentReference =
        firestore.collection(Constants.USER_COLLECTION).document(uid)

    private fun petCollectionRef(): CollectionReference =
        firestore.collection(Constants.PET_COLLECTION)

    private fun testingCollectionRef(): CollectionReference =
        firestore.collection("test")

    override suspend fun getFavouritePetList(): Response<List<Pet>> {
        try {
            val favouritePetList = userCollectionRef(authService.currentUser.first().id)
                .collection(Constants.FAVOURITE_COLLECTION)
                .get()
                .await()
                .documents
                .map { it.toObject(Pet::class.java)!! }
            return Response.Success(favouritePetList)
        } catch(e: Exception) {
            return Response.Failure(message = e.localizedMessage)
        }
    }

    override suspend fun createUser() {
        val initialUserData = User(
            id = authService.currentUser.first().id,
            name = "Logan",
            // Default image url
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/pet-app-63469.appspot.com/o/avatar-1.jpeg?alt=media&token=1d01351d-0952-4e3d-8489-ba4cec48b968",
            anonymous = false
        )
        // Use the userId as the document Id to create a user document
        userCollectionRef(authService.currentUser.first().id).set(initialUserData).await()
    }

    override suspend fun getPets(): Response<List<Pet>> {
        try {
            val petList = mutableListOf<Pet>()
            val petSnapshot = petCollectionRef().get().await()
            for (document in petSnapshot.documents) {
                val pet = document.toObject(Pet::class.java)!!
                petList.add(pet)
            }
            return Response.Success(petList)
        } catch(e: Exception) {
            return Response.Failure(message = e.localizedMessage)
        }
    }

    override suspend fun getPet(petId: String): Pet =
        petCollectionRef().document(petId).get().await().toObject(Pet::class.java) ?: Pet()

    override suspend fun addPetToFavourite(pet: Pet) {
        userCollectionRef(authService.currentUser.first().id)
            .collection(Constants.FAVOURITE_COLLECTION)
            .document(pet.id)
            .set(pet)
            .await()
    }

    override suspend fun removePetFromFavourite(petId: String) {
        userCollectionRef(authService.currentUser.first().id)
            .collection(Constants.FAVOURITE_COLLECTION)
            .document(petId)
            .delete()
            .await()
    }

    override suspend fun getUser(uid: String): User =
        userCollectionRef(uid)
            .get()
            .await()
            .toObject(User::class.java) ?: User()

    override suspend fun getCurrentUser(): User =
        userCollectionRef(authService.currentUser.first().id)
            .get()
            .await()
            .toObject(User::class.java) ?: User()

    override suspend fun saveUser(user: User) {
        userCollectionRef(authService.currentUser.first().id)
            .set(user)
            .await()
    }

    override suspend fun savePet(pet: Pet, petImageUri: Uri) {
        val docRef = petCollectionRef().add(pet).await()
        firebaseStorageService.savePetImage(petId = docRef.id, uri = petImageUri)
    }

    override suspend fun getCurrentUserChannel(): Response<List<User>> {
        try {
            val channelUserList = mutableListOf<User>()
            userCollectionRef(authService.currentUser.first().id)
                .collection("channel")
                .get()
                .await()
                .documents
                .onEach { doc ->
                    val user = getUser(doc.id)
                    channelUserList.add(user)
                }
            return Response.Success(channelUserList)
        } catch(e: Exception) {
            return Response.Failure(message = e.localizedMessage)
        }
    }

    override suspend fun getMessages(chatUserId: String): Response<List<Message>> {
        try {
            val messageDocRef = userCollectionRef(authService.currentUser.first().id)
                .collection("channel")
                .document(chatUserId)
                .get()
                .await()

            if (messageDocRef.exists()) {
                println("Message Doc exists!!!!!!!!!!!")
                var messages = mutableListOf<Map<String, Any>>()
                messages = messageDocRef.data?.get("messages") as MutableList<Map<String, Any>>
                val messageList = messages.map {
                    val message = it["message"] as String
                    val senderId = it["senderId"] as String
                    val timestamp = it["timestamp"] as Long
                    Message(message, senderId, timestamp)
                }
                return Response.Success(messageList)
            } else {
                println("->>>>>Message Doc Not Existss!!!!!!!!!!!")
                throw Exception("Unexpected error occurred")
            }
        } catch(e: Exception) {
            println("->>>>>Message Doc Not Existss!!!!!!!!!!!")
            return Response.Failure(message = e.localizedMessage)
        }
    }

    override suspend fun saveMessage(chatUserId: String, messages: Any) {
        // Save into current user collection
        userCollectionRef(authService.currentUser.first().id)
            .collection("channel")
            .document(chatUserId)
            .set(messages)
            .await()

        // Save into chat user collection
        userCollectionRef(chatUserId)
            .collection("channel")
            .document(authService.currentUser.first().id)
            .set(messages)
            .await()
    }

    override suspend fun saveTestingData(data: Any) {
        testingCollectionRef().document("test").set(data).await()
    }
}