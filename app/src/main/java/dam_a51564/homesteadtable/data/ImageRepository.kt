package dam_a51564.homesteadtable.data

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Repository object responsible for uploading images to Cloudinary.
 * Handles background uploads asynchronously using coroutines.
 */
object ImageRepository {
    /**
     * Uploads an image file specified by its local Uri to Cloudinary.
     * Converts Cloudinary's callback mechanism into a Kotlin Suspend function.
     *
     * @param imageUri The local [Uri] of the image to be uploaded.
     * @return The secure URL string of the uploaded image.
     * @throws Exception if the upload fails or returns an error.
     */
    suspend fun uploadImage(imageUri: Uri): String = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(imageUri)
            // Using an unsigned upload preset for client-side convenience.
            // This relies on the security settings defined in the Cloudinary console
            // rather than server-side signatures.
            .unsigned("HomesteadTable")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val url = resultData?.get("secure_url") as? String ?: ""
                    continuation.resume(url)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception(error?.description ?: "Unknown upload error"))
                }
            }).dispatch()
    }
}