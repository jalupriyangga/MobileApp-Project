package com.example.mobileapptechnobit.ui

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.data.remote.HistoryResponseItem
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DetailPresenceScreen(navCtrl: NavController) {
    var hasShownToast by remember { mutableStateOf(false) }

    val presenceItem = navCtrl.previousBackStackEntry!!
        ?.savedStateHandle?.get<HistoryResponseItem>("presenceItem")
    val formattedDate = navCtrl.previousBackStackEntry
        ?.savedStateHandle?.get<String>("formattedDate")

    if (presenceItem == null) {
        return
    }
    val nama = presenceItem.nama
    val tanggal = formattedDate!!
    val status = presenceItem.status
    val lokasi = presenceItem.lokasi
    val shift = presenceItem.shift ?: "Pagi"
    val foto = presenceItem.foto

    Scaffold(
        topBar = {
            Box {
                Column(Modifier.fillMaxWidth()) {
                    val linkfoto = "https://app.arunikaprawira.com$foto"
                    HistoryPresensiTitle(modifier = Modifier, navCtrl)
                    Column(Modifier.background(Color.White)) {
                        HistoryPresensi(nama, tanggal, status, lokasi, shift, linkfoto)
                        ActionButton(nama, tanggal, status, lokasi, shift, linkfoto)
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {}
    }
}


@Composable
fun HistoryPresensiTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    ) {
        IconButton(
            onClick = {
                navCtrl.popBackStack()
            },
            Modifier.padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun HistoryPresensi(nama: String, tanggal: String, status: String, lokasi: String, shift: String, foto: String?) {
    Column {
        Column(
            modifier = Modifier
                .padding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Detail Presensi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Nama Karyawan \t:\t$nama",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tanggal \t :\t$tanggal",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Status",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$status",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text ="Shift",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$shift",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = foto,
                contentDescription = "Foto Presensi",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ActionButton(nama: String, tanggal: String, status: String, lokasi: String, shift: String, foto: String?) {
    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        androidx.compose.material3. Button(
            onClick = {
                scope.launch {
                    val uri = generatePresensiPDF(context, nama, tanggal, status, lokasi, shift, foto)
                    if (uri != null) pdfUri = uri
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ){
            Icon(Icons.Default.SaveAlt, contentDescription = "Download", tint = Color.White)
        }

        androidx.compose.material3.Button(
            onClick = {
                if (pdfUri != null) {
                    sharePdf(context, pdfUri!!)
                } else {
                    Toast.makeText(context, "PDF belum dibuat", Toast.LENGTH_SHORT).show()
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
        }
    }
}

suspend fun generatePresensiPDF(context: Context, nama: String, tanggal: String, status: String, lokasi: String, shift: String, foto: String?): Uri? {
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

    // Title
    canvas.drawText("Detail Presensi", pageWidth / 2f, y, titlePaint)
    y += 60f

    // Nama Karyawan
    canvas.drawText(nama, pageWidth / 2f, y, subtitlePaint)
    y += 30f

    // Format date for PDF
    val formattedDate = formatDateForPDF(tanggal)

    // Tanggal
    canvas.drawText(formattedDate, pageWidth / 2f, y, subtitlePaint)
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
    y += 60f // Increased space before image

    // Image section
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

fun sharePdf(context: Context, pdfUri: Uri) {
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

private fun formatDateForPDF(dateString: String): String {
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


@Preview(showBackground = true)
@Composable
fun DetailPresenceScreenPreview() {
    DetailPresenceScreen(navCtrl = rememberNavController())
}