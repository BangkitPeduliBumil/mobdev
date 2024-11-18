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
        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })

        try {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(senderEmail))
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
            mimeMessage.subject = subject
            mimeMessage.setText(message)
            Transport.send(mimeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
