package com.bangkit.test.auth

import android.os.AsyncTask
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendMailTask(
    private val senderEmail: String,
    private val senderPassword: String,
    private val recipientEmail: String,
    private val subject: String,
    private val message: String
) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.ssl.protocols", "TLSv1.2") // Tambahkan untuk memastikan TLS modern digunakan
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })

        try {
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                subject = this@SendMailTask.subject
                setText(this@SendMailTask.message)
            }
            Transport.send(mimeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
