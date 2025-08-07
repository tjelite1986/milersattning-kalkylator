package com.milersattning.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class ExportService(private val context: Context) {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val fileNameDateFormat = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault())
    
    fun exportTripsToPDF(trips: List<Trip>, rate: Double): File? {
        try {
            val fileName = "milersattning_${fileNameDateFormat.format(Date())}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            
            val pdfWriter = PdfWriter(file.absolutePath)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)
            
            // Title
            document.add(
                Paragraph("Milersättning - Reserapport")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18f)
                    .setBold()
            )
            
            // Generation date and summary
            document.add(Paragraph("Genererad: ${dateFormat.format(Date())}"))
            document.add(Paragraph("Antal resor: ${trips.size}"))
            
            val totalDistance = trips.sumOf { it.distance }
            val totalAmount = trips.sumOf { it.amount }
            
            document.add(Paragraph("Total distans: ${"%.1f".format(totalDistance)} km"))
            document.add(Paragraph("Totalbelopp: ${"%.2f".format(totalAmount)} SEK"))
            document.add(Paragraph("Aktuell milersättning: ${"%.2f".format(rate)} SEK/km"))
            document.add(Paragraph(" ")) // Empty line
            
            // Trips table
            if (trips.isNotEmpty()) {
                val table = Table(UnitValue.createPercentArray(floatArrayOf(15f, 40f, 15f, 15f, 15f)))
                    .useAllAvailableWidth()
                
                // Headers
                table.addHeaderCell("Datum")
                table.addHeaderCell("Beskrivning")
                table.addHeaderCell("Distans (km)")
                table.addHeaderCell("Belopp (SEK)")
                table.addHeaderCell("Syfte")
                
                // Data rows
                trips.forEach { trip ->
                    table.addCell(trip.date)
                    table.addCell(trip.description.ifEmpty { "Ej specificerad" })
                    table.addCell("%.1f".format(trip.distance))
                    table.addCell("%.2f".format(trip.amount))
                    table.addCell(trip.purpose)
                }
                
                document.add(table)
            }
            
            document.close()
            return file
            
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    fun exportTripsToCSV(trips: List<Trip>, rate: Double): File? {
        try {
            val fileName = "milersattning_${fileNameDateFormat.format(Date())}.csv"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            
            FileWriter(file).use { writer ->
                // Header
                writer.append("# Milersättning - Reserapport\n")
                writer.append("# Genererad: ${dateFormat.format(Date())}\n")
                writer.append("# Antal resor: ${trips.size}\n")
                writer.append("# Total distans: ${"%.1f".format(trips.sumOf { it.distance })} km\n")
                writer.append("# Totalbelopp: ${"%.2f".format(trips.sumOf { it.amount })} SEK\n")
                writer.append("# Aktuell milersättning: ${"%.2f".format(rate)} SEK/km\n")
                writer.append("\n")
                
                // CSV Headers
                writer.append("Datum,Beskrivning,Distans (km),Belopp (SEK),Syfte,Starttid,Sluttid,Fordon,Anteckningar,Status\n")
                
                // Data rows
                trips.forEach { trip ->
                    writer.append("${trip.date},")
                    writer.append("\"${trip.description.replace("\"", "\"\"")}\",")
                    writer.append("${"%.1f".format(trip.distance)},")
                    writer.append("${"%.2f".format(trip.amount)},")
                    writer.append("${trip.purpose},")
                    writer.append("${trip.startTime},")
                    writer.append("${trip.endTime},")
                    writer.append("${trip.vehicleType},")
                    writer.append("\"${trip.notes.replace("\"", "\"\"")}\",")
                    writer.append("${trip.status}\n")
                }
                
                writer.flush()
            }
            
            return file
            
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    fun shareFile(file: File) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = when (file.extension.lowercase()) {
                    "pdf" -> "application/pdf"
                    "csv" -> "text/csv"
                    else -> "*/*"
                }
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            context.startActivity(Intent.createChooser(shareIntent, "Dela reserapport"))
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}