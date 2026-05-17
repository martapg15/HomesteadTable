package dam_a51564.homesteadtable.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

object AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun getCurrentUser() = auth.currentUser

    suspend fun signUp(email: String, password: String, fullName: String): Result<AuthResult> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()

            // Attach the name to the Firebase User Profile
            val user = result.user
            if (user != null && fullName.isNotBlank()) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = fullName
                }
                user.updateProfile(profileUpdates).await()
            }

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}