package com.example.mobileapptechnobit.ui.feature_history

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class PdfGenerator {
    suspend fun generatePresensiPDFPresensi(context: Context, nama: String?, tanggal: String, status: String, lokasi: String, shift: String, foto: String?): Uri? {
        if (nama == null) {
            Toast.makeText(context, "Nama tidak tersedia", Toast.LENGTH_SHORT).show()
            return null
        }
        val pageHeight = 1120
        val pageWidth = 792
        val pdfDocument = PdfDocument()
        val paint = Paint()

        val titlePaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 25f
            color = Color.Black.toArgb()
            textAlign = Paint.Align.CENTER
        }

        val subtitlePaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 18f
            color = Color.DarkGray.toArgb()
            textAlign = Paint.Align.CENTER
        }

        val labelPaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 12f
            color = Color.Gray.toArgb()
        }

        val sectionPaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 14f
            color = Color.Black.toArgb()
        }

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 50f

        // Title - sama seperti UI: "Detail Presensi"
        canvas.drawText("Detail Presensi", pageWidth / 2f, y, titlePaint)
        y += 60f

        // Nama Karyawan - format seperti UI: "Nama Karyawan : [nama]"
        canvas.drawText("Nama Karyawan : $nama", pageWidth / 2f, y, subtitlePaint)
        y += 30f

        // Format date for PDF
        val formattedDate = formatDateForPDF(tanggal)

        // Tanggal - format seperti UI: "Tanggal : [tanggal]"
        canvas.drawText("Tanggal : $formattedDate", pageWidth / 2f, y, subtitlePaint)
        y += 40f

        // Divider
        paint.color = Color.LightGray.toArgb()
        canvas.drawLine(56f, y, pageWidth - 56f, y, paint)
        y += 40f

        // Status
        labelPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("Status", 56f, y, labelPaint)
        y += 20f
        canvas.drawText(status, 56f, y, sectionPaint)
        y += 40f

        // Lokasi
        canvas.drawText("Lokasi", 56f, y, labelPaint)
        y += 20f
        canvas.drawText(lokasi, 56f, y, sectionPaint)
        y += 40f

        // Shift
        canvas.drawText("Shift", 56f, y, labelPaint)
        y += 20f
        canvas.drawText(shift, 56f, y, sectionPaint)
        y += 60f // Increased space before image sesuai UI

        // Image section - sesuai dengan UI DetailPresensiScreen
        if (!foto.isNullOrEmpty()) {
            // Clear logging for debugging
            Log.d("PDFUtils", "Attempting to load image from URL: $foto")

            try {
                val request = ImageRequest.Builder(context)
                    .data(foto)
                    .allowHardware(false) // Important for bitmap decoding
                    .size(400, 400) // Request appropriate size
                    .build()

                val result = context.imageLoader.execute(request)

                if (result is SuccessResult) {
                    Log.d("PDFUtils", "Image loaded successfully")
                    val bitmap = (result.drawable as BitmapDrawable).bitmap

                    // Calculate maximum available space for image
                    val availableHeight = pageHeight - y - 50f // 50px margin at bottom

                    // Calculate image dimensions while maintaining aspect ratio
                    val maxImageWidth = pageWidth - 112f // 56px margin on each side
                    val aspectRatio = bitmap.height.toFloat() / bitmap.width.toFloat()

                    // Calculate image dimensions ensuring it fits within available space
                    var imageWidth = maxImageWidth
                    var imageHeight = imageWidth * aspectRatio

                    // If the image height exceeds available height, scale it down
                    if (imageHeight > availableHeight) {
                        imageHeight = availableHeight
                        imageWidth = imageHeight / aspectRatio
                    }

                    // Center the image horizontally
                    val imageX = (pageWidth - imageWidth) / 2

                    // Draw image with proper dimensions
                    val destRect = RectF(imageX, y, imageX + imageWidth, y + imageHeight)
                    canvas.drawBitmap(bitmap, null, destRect, null)

                    // Update y position after drawing image
                    y += imageHeight + 40f

                    Log.d("PDFUtils", "Image drawn to PDF at x=$imageX, y=$y with width=$imageWidth, height=$imageHeight")
                } else {
                    Log.e("PDFUtils", "Failed to load image from URL: $foto")
                    canvas.drawText("Foto tidak dapat dimuat", 56f, y, sectionPaint)
                    y += 40f
                }
            } catch (e: Exception) {
                Log.e("PDFUtils", "Error drawing image from URL", e)
                canvas.drawText("Gagal memuat foto: ${e.message}", 56f, y, sectionPaint)
                y += 40f
            }
        } else {
            canvas.drawText("Foto tidak tersedia", 56f, y, sectionPaint)
            y += 40f
        }

        pdfDocument.finishPage(page)

        val fileName = "Presensi_${nama.replace(" ", "_")}_${
            tanggal.replace("-", "_").replace(" ", "_").replace(":", "_")
        }.pdf"

        val file: File
        var uri: Uri?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val insertedUri = resolver.insert(collection, contentValues)

            if (insertedUri != null) {
                resolver.openOutputStream(insertedUri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(insertedUri, contentValues, null, null)

                Toast.makeText(context, "PDF disimpan di folder Download", Toast.LENGTH_SHORT).show()
                uri = insertedUri
            } else {
                Toast.makeText(context, "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
                uri = null
            }
        } else {
            // Android 9 ke bawah
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            file = File(downloadsDir, fileName)

            try {
                val outputStream = FileOutputStream(file)
                pdfDocument.writeTo(outputStream)
                outputStream.close()
                Toast.makeText(context, "PDF disimpan di ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                uri = Uri.fromFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal menyimpan PDF", Toast.LENGTH_SHORT).show()
                uri = null
            }
        }

        // Try to open the PDF
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(Intent.createChooser(intent, "Buka dengan"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_SHORT).show()
        }

        pdfDocument.close()
        return uri
    }
    suspend fun generatePresensiPDFPatroli(context: Context, nama: String?, tanggal: String, status: String, lokasi: String, shift: String, catatan: String, foto: String?): Uri? {
        if (nama == null) {
            Toast.makeText(context, "Nama tidak tersedia", Toast.LENGTH_SHORT).show()
            return null
        }
        val pageHeight = 1120
        val pageWidth = 792
        val pdfDocument = PdfDocument()
        val paint = Paint()

        val titlePaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 25f
            color = Color.Black.toArgb()
            textAlign = Paint.Align.CENTER
        }

        val subtitlePaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 18f
            color = Color.DarkGray.toArgb()
            textAlign = Paint.Align.CENTER
        }

        val labelPaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 12f
            color = Color.Gray.toArgb()
        }

        val sectionPaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 14f
            color = Color.Black.toArgb()
        }

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 50f

        // Title - sama seperti UI: "Detail Presensi"
        canvas.drawText("Detail Presensi", pageWidth / 2f, y, titlePaint)
        y += 60f

        // Nama Karyawan - format seperti UI: "Nama Karyawan : [nama]"
        canvas.drawText("Nama Karyawan : $nama", pageWidth / 2f, y, subtitlePaint)
        y += 30f

        // Format date for PDF
        val formattedDate = formatDateForPDF(tanggal)

        // Tanggal - format seperti UI: "Tanggal : [tanggal]"
        canvas.drawText("Tanggal : $formattedDate", pageWidth / 2f, y, subtitlePaint)
        y += 40f

        // Divider
        paint.color = Color.LightGray.toArgb()
        canvas.drawLine(56f, y, pageWidth - 56f, y, paint)
        y += 40f

        // Status
        labelPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("Status", 56f, y, labelPaint)
        y += 20f
        canvas.drawText(status, 56f, y, sectionPaint)
        y += 40f

        // Lokasi
        canvas.drawText("Lokasi", 56f, y, labelPaint)
        y += 20f
        canvas.drawText(lokasi, 56f, y, sectionPaint)
        y += 40f

        // Shift
        canvas.drawText("Shift", 56f, y, labelPaint)
        y += 20f
        canvas.drawText(shift, 56f, y, sectionPaint)
        y += 40f

        // Catatan - ditambahkan sesuai urutan di UI
        canvas.drawText("Catatan", 56f, y, labelPaint)
        y += 20f

        // Handle multi-line catatan jika terlalu panjang
        val catatanText = catatan
        val maxWidth = pageWidth - 112f // 56px margin on each side
        val words = catatanText.split(" ")
        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val textWidth = sectionPaint.measureText(testLine)

            if (textWidth <= maxWidth) {
                currentLine = testLine
            } else {
                if (currentLine.isNotEmpty()) {
                    canvas.drawText(currentLine, 56f, y, sectionPaint)
                    y += 20f
                }
                currentLine = word
            }
        }

        // Draw remaining text
        if (currentLine.isNotEmpty()) {
            canvas.drawText(currentLine, 56f, y, sectionPaint)
            y += 40f
        } else {
            y += 20f // Adjust spacing if no remaining text
        }

        // Image section - label "Foto Presensi" sesuai UI
        if (!foto.isNullOrEmpty()) {
            canvas.drawText("Foto Presensi", 56f, y, labelPaint)
            y += 20f

            // Clear logging for debugging
            Log.d("PDFUtils", "Attempting to load image from URL: $foto")

            try {
                val request = ImageRequest.Builder(context)
                    .data(foto)
                    .allowHardware(false) // Important for bitmap decoding
                    .size(400, 400) // Request appropriate size
                    .build()

                val result = context.imageLoader.execute(request)

                if (result is SuccessResult) {
                    Log.d("PDFUtils", "Image loaded successfully")
                    val bitmap = (result.drawable as BitmapDrawable).bitmap

                    // Calculate maximum available space for image
                    val availableHeight = pageHeight - y - 50f // 50px margin at bottom

                    // Calculate image dimensions while maintaining aspect ratio
                    val maxImageWidth = pageWidth - 112f // 56px margin on each side
                    val aspectRatio = bitmap.height.toFloat() / bitmap.width.toFloat()

                    // Calculate image dimensions ensuring it fits within available space
                    var imageWidth = maxImageWidth
                    var imageHeight = imageWidth * aspectRatio

                    // If the image height exceeds available height, scale it down
                    if (imageHeight > availableHeight) {
                        imageHeight = availableHeight
                        imageWidth = imageHeight / aspectRatio
                    }

                    // Center the image horizontally
                    val imageX = (pageWidth - imageWidth) / 2

                    // Draw image with proper dimensions
                    val destRect = RectF(imageX, y, imageX + imageWidth, y + imageHeight)
                    canvas.drawBitmap(bitmap, null, destRect, null)

                    // Update y position after drawing image
                    y += imageHeight + 40f

                    Log.d("PDFUtils", "Image drawn to PDF at x=$imageX, y=$y with width=$imageWidth, height=$imageHeight")
                } else {
                    Log.e("PDFUtils", "Failed to load image from URL: $foto")
                    canvas.drawText("Foto tidak dapat dimuat", 56f, y, sectionPaint)
                    y += 40f
                }
            } catch (e: Exception) {
                Log.e("PDFUtils", "Error drawing image from URL", e)
                canvas.drawText("Gagal memuat foto: ${e.message}", 56f, y, sectionPaint)
                y += 40f
            }
        } else {
            canvas.drawText("Foto Presensi", 56f, y, labelPaint)
            y += 20f
            canvas.drawText("Foto tidak tersedia", 56f, y, sectionPaint)
            y += 40f
        }

        pdfDocument.finishPage(page)

        // Filename disesuaikan untuk Patroli
        val fileName = "Patroli_${nama.replace(" ", "_")}_${
            tanggal.replace("-", "_").replace(" ", "_").replace(":", "_")
        }.pdf"

        val file: File
        var uri: Uri?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val insertedUri = resolver.insert(collection, contentValues)

            if (insertedUri != null) {
                resolver.openOutputStream(insertedUri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(insertedUri, contentValues, null, null)

                Toast.makeText(context, "PDF disimpan di folder Download", Toast.LENGTH_SHORT).show()
                uri = insertedUri
            } else {
                Toast.makeText(context, "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
                uri = null
            }
        } else {
            // Android 9 ke bawah
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            file = File(downloadsDir, fileName)

            try {
                val outputStream = FileOutputStream(file)
                pdfDocument.writeTo(outputStream)
                outputStream.close()
                Toast.makeText(context, "PDF disimpan di ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                uri = Uri.fromFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal menyimpan PDF", Toast.LENGTH_SHORT).show()
                uri = null
            }
        }

        // Try to open the PDF
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(Intent.createChooser(intent, "Buka dengan"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_SHORT).show()
        }

        pdfDocument.close()
        return uri
    }

    fun sharePdf(context: Context, pdfUri: Uri?) {
        if (pdfUri == null) {
            Toast.makeText(context, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        } else {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, pdfUri)
                type = "application/pdf"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share PDF using"))
        }
    }

    fun formatDateForPDF(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm:ss a", Locale("id", "ID"))

            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }
}