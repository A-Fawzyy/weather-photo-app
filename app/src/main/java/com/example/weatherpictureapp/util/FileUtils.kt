package com.example.weatherpictureapp.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import com.example.weatherpictureapp.presentation.weather_photo_app.WeatherPhotoApplication.Companion.WEATHER_PHOTO_FOLDER
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun saveImageToStorage(imageBitmap: Bitmap, context: Context, imageUri: Uri, block: (file: File) -> Unit) {
	var fOut: FileOutputStream? = null
	try {
		val directoryPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
		val  fileName = File(FileUtils.getPath(context, imageUri)).name.split(".")[0]
		val file = File(directoryPath, "${fileName}.png")
		if (file.length() == 0L) {
			fOut = FileOutputStream(file)
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
		}
		//start Intent
		block.invoke(file)

	} catch (e: Exception) {
		e.printStackTrace()
	} finally {
		try {
			fOut?.flush()
			fOut?.close()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

}

object FileUtils {
	private var contentUri: Uri? = null

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.<br></br>
	 * <br></br>
	 * Callers should check whether the path is local before assuming it
	 * represents a local file.
	 *
	 * @param context The context.
	 * @param uri     The Uri to query.
	 */
	@SuppressLint("NewApi", "ObsoleteSdkInt")
	fun getPath(context: Context, uri: Uri): String? {
		// check here to KITKAT or new version
		val isKitKat: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
		var selection: String? = null
		var selectionArgs: Array<String>? = null
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).toTypedArray()
				val type = split[0]
				val fullPath = getPathFromExtSD(split)
				return if (fullPath !== "") {
					fullPath
				} else {
					null
				}
			} else if (isDownloadsDocument(uri)) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					val id: String
					var cursor: Cursor? = null
					try {
						var cursor = context.contentResolver.query(uri, null, null, null, null)
						if (cursor != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
							var path: String? = null
							cursor.moveToFirst()
							var document_id = cursor.getString(0)
							document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
							cursor.close()
							cursor = context.contentResolver.query(
								MediaStore.Downloads.EXTERNAL_CONTENT_URI,
								null,
								MediaStore.Images.Media._ID + " = ? ",
								arrayOf(document_id),
								null
							)
							if (cursor != null) {
								cursor.moveToFirst()
								path =
									cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
								cursor.close()
							}
							return path
						}


					} finally {
						cursor?.close()
					}
					id = DocumentsContract.getDocumentId(uri)
					if (!TextUtils.isEmpty(id)) {
						if (id.startsWith("raw:")) {
							return id.replaceFirst("raw:".toRegex(), "")
						}
						val contentUriPrefixesToTry = arrayOf(
							"content://downloads/public_downloads",
							"content://downloads/my_downloads"
						)
						for (contentUriPrefix in contentUriPrefixesToTry) {
							return try {
								val contentUri: Uri = ContentUris.withAppendedId(
									Uri.parse(contentUriPrefix),
									java.lang.Long.valueOf(id)
								)

								/*   final Uri contentUri = ContentUris.withAppendedId(
															Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));*/
								getDataColumn(context, contentUri, null, null)
							} catch (e: NumberFormatException) {
								//In Android 8 and Android P the id is not a number
								uri.path?.replaceFirst("^/document/raw:", "")
									?.replaceFirst("^raw:", "")
							}
						}
					}
				} else {
					val id = DocumentsContract.getDocumentId(uri)
					val isOreo: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
					if (id.startsWith("raw:")) {
						return id.replaceFirst("raw:".toRegex(), "")
					}
					try {
						contentUri = ContentUris.withAppendedId(
							Uri.parse("content://downloads/public_downloads"),
							java.lang.Long.valueOf(id)
						)
					} catch (e: NumberFormatException) {
						e.printStackTrace()
					}
					if (contentUri != null) {
						return getDataColumn(context, contentUri, null, null)
					}
				}
			} else if (isMediaDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).toTypedArray()
				val type = split[0]
				var contentUri: Uri? = null
				if ("image" == type) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
				} else if ("video" == type) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
				} else if ("audio" == type) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
				}
				selection = "_id=?"
				selectionArgs = arrayOf(split[1])
				return getDataColumn(
					context, contentUri, selection,
					selectionArgs
				)
			} else if (isGoogleDriveUri(uri)) {
				return getDriveFilePath(uri, context)
			}
		} else if ("content".equals(uri.scheme, ignoreCase = true)) {
			if (isGooglePhotosUri(uri)) {
				return uri.lastPathSegment
			}
			if (isGoogleDriveUri(uri)) {
				return getDriveFilePath(uri, context)
			}
			return if (Build.VERSION.SDK_INT === Build.VERSION_CODES.N) {
				// return getFilePathFromURI(context,uri);
				getMediaFilePathForN(uri, context)
				// return getRealPathFromURI(context,uri);
			} else {
				getDataColumn(context, uri, null, null)
			}
		} else if ("file".equals(uri.scheme, ignoreCase = true)) {
			return uri.path
		}
		return null
	}

	/**
	 * Check if a file exists on device
	 *
	 * @param filePath The absolute file path
	 */
	private fun fileExists(filePath: String): Boolean {
		val file = File(filePath)
		return file.exists()
	}



	/**
	 * Get full file path from external storage
	 *
	 * @param pathData The storage type and the relative path
	 */
	private fun getPathFromExtSD(pathData: Array<String>): String {
		val type = pathData[0]
		val relativePath = "/" + pathData[1]
		var fullPath = ""

		// on my Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
		// something like "71F8-2C0A", some kind of unique id per storage
		// don't know any API that can get the root path of that storage based on its id.
		//
		// so no "primary" type, but let the check here for other devices
		if ("primary".equals(type, ignoreCase = true)) {
			fullPath = Environment.getExternalStorageDirectory().toString() + relativePath
			if (fileExists(fullPath)) {
				return fullPath
			}
		}

		// Environment.isExternalStorageRemovable() is `true` for external and internal storage
		// so we cannot relay on it.
		//
		// instead, for each possible path, check if file exists
		// we'll start with secondary storage as this could be our (physically) removable sd card
		fullPath = System.getenv("SECONDARY_STORAGE") + relativePath
		if (fileExists(fullPath)) {
			return fullPath
		}
		fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath
		return if (fileExists(fullPath)) {
			fullPath
		} else fullPath
	}

	private fun getDriveFilePath(uri: Uri, context: Context): String {
		val returnUri: Uri = uri

		context.contentResolver?.query(returnUri, null, null, null, null)?.let { returnCursor ->
			val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
			val sizeIndex: Int = returnCursor.getColumnIndex(OpenableColumns.SIZE)
			returnCursor.moveToFirst()
			val name: String = returnCursor.getString(nameIndex)
			val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
			val file = File(context.cacheDir, name)
			try {
				context.contentResolver.openInputStream(uri)?.let { inputStream ->
					val outputStream = FileOutputStream(file)
					var read = 0
					val maxBufferSize = 1 * 1024 * 1024
					val bytesAvailable: Int = inputStream.available()

					//int bufferSize = 1024;
					val bufferSize = Math.min(bytesAvailable, maxBufferSize)
					val buffers = ByteArray(bufferSize)
					while (inputStream.read(buffers).also { read = it } != -1) {
						outputStream.write(buffers, 0, read)
					}
					inputStream.close()
					outputStream.close()

				}
			} catch (e: Exception) {
				Log.e("Exception", e.message ?: "Error")
			}
		}

		return ""
	}

	private fun getMediaFilePathForN(uri: Uri, context: Context): String {
		val returnUri: Uri = uri

		context.contentResolver.query(returnUri, null, null, null, null)?.let { returnCursor ->

			val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
			val sizeIndex: Int = returnCursor.getColumnIndex(OpenableColumns.SIZE)
			returnCursor.moveToFirst()
			val name: String = returnCursor.getString(nameIndex)
			val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
			val file = File(context.filesDir, name)
			try {
				context.contentResolver.openInputStream(uri)?.let { inputStream ->
					val outputStream = FileOutputStream(file)
					var read = 0
					val maxBufferSize = 1 * 1024 * 1024
					val bytesAvailable: Int = inputStream.available()

					//int bufferSize = 1024;
					val bufferSize = Math.min(bytesAvailable, maxBufferSize)
					val buffers = ByteArray(bufferSize)
					while (inputStream.read(buffers).also({ read = it }) != -1) {
						outputStream.write(buffers, 0, read)
					}
					inputStream.close()
					outputStream.close()
				}

			} catch (e: Exception) {
				Log.e("Exception", e.message ?: "Error")
			}
		}
		/*
		 * Get the column indexes of the data in the Cursor,
		 *     * move to the first row in the Cursor, get the data,
		 *     * and display it.
		 * */
		return ""
	}

	private fun getDataColumn(
		context: Context, uri: Uri?,
		selection: String?, selectionArgs: Array<String>?
	): String? {
		var cursor: Cursor? = null
		val column = "_data"
		val projection = arrayOf(column)
		try {
			cursor = context.contentResolver.query(
				uri!!, projection,
				selection, selectionArgs, null
			)
			if (cursor != null && cursor.moveToFirst()) {
				val index: Int = cursor.getColumnIndexOrThrow(column)
				return cursor.getString(index)
			}
		} finally {
			if (cursor != null) cursor.close()
		}
		return null
	}

	/**
	 * @param uri - The Uri to check.
	 * @return - Whether the Uri authority is ExternalStorageProvider.
	 */
	private fun isExternalStorageDocument(uri: Uri): Boolean {
		return "com.android.externalstorage.documents" == uri.authority
	}

	/**
	 * @param uri - The Uri to check.
	 * @return - Whether the Uri authority is DownloadsProvider.
	 */
	private fun isDownloadsDocument(uri: Uri): Boolean {
		return "com.android.providers.downloads.documents" == uri.authority
	}

	/**
	 * @param uri - The Uri to check.
	 * @return - Whether the Uri authority is MediaProvider.
	 */
	private fun isMediaDocument(uri: Uri): Boolean {
		return "com.android.providers.media.documents" == uri.authority
	}

	/**
	 * @param uri - The Uri to check.
	 * @return - Whether the Uri authority is Google Photos.
	 */
	private fun isGooglePhotosUri(uri: Uri): Boolean {
		return "com.google.android.apps.photos.content" == uri.authority
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Drive.
	 */
	private fun isGoogleDriveUri(uri: Uri): Boolean {
		return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
	}
}

