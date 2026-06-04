package dam_a51564.homesteadtable.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

/**
 * Repository object that manages authentication operations using Firebase Authentication.
 * It provides methods for user signup, login, password reset, and session checking.
 */
object AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Retrieves the currently logged-in Firebase user, or null if no user is authenticated.
     *
     * @return The current [com.google.firebase.auth.FirebaseUser] or null.
     */
    fun getCurrentUser() = auth.currentUser

    /**
     * Registers a new user with an email, password, and full name.
     * After successful creation, updates the user's display name profile.
     *
     * @param email The user's email address.
     * @param password The user's account password.
     * @param fullName The full name to associate with the user profile.
     * @return A [Result] encapsulating the successful [AuthResult] or an exception on failure.
     */
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

    /**
     * Authenticates a user with their email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] encapsulating the successful [AuthResult] or an exception on failure.
     */
    suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sends a password reset email link to the specified email address.
     *
     * @param email The email address to send the reset link to.
     * @return A [Result] containing true if successful, or an exception on failure.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Signs the current user out of Firebase, destroying the active authentication session.
     */
    fun logout() {
        auth.signOut()
    }
}