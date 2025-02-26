package com.example.sisteminformasimenejemensatpam

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun sendEmail(userEmail: String, otp:String) {
    val smtpHost = "smtp.gmail.com"
    val smtpPort = "587"  // Port untuk TLS (gunakan 465 jika ingin SSL)
    val smtpUsername = "hironemus.apriliano@gmail.com"  // Ganti dengan email pengirim
    val smtpPassword = "jcew mmzu giko xtjb"  // Ganti dengan App Password dari Google

    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true") // Aktifkan TLS
        put("mail.smtp.host", smtpHost)
        put("mail.smtp.port", smtpPort)
    }

    val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(smtpUsername, smtpPassword)
        }
    })

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(smtpUsername, "Support Team"))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail))
            subject = "Kode OTP untuk Reset Password"
            setText("Kode OTP anda adalah: $otp \nSilakan kembali ke aplikasi dan isikan kode OTP berikut")
        }
        Transport.send(message)
        println("Email reset password terkirim ke $userEmail")
    } catch (e: MessagingException) {
        e.printStackTrace()
        println("Gagal mengirim email: ${e.message}")
    }
}