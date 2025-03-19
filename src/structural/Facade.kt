package structural

data class Email(val subject: String, val body: String)
data class PdfFile(val name: String, val path: String, val content: String)

class PdfUploader {
    fun uploadPdf(file: PdfFile, siteUrl: String) = println("Uploading PDF file '${file.name}' to $siteUrl")
}

class EmailReceiver {
    fun receiveEmail(): Email? {
        val receivedEmail = true
        if (receivedEmail) {
            return Email("The system is working", "This email is a test email")
        }

        return null
    }
}

class EmailToPdfConverter {
    fun convert(email: Email): PdfFile = PdfFile(email.subject + ".pdf", "/home/emails", email.body)
}

class EmailUploader(
    private val site: String,
    private val emailReceiver: EmailReceiver = EmailReceiver(),
    private val emailToPdfConverter: EmailToPdfConverter = EmailToPdfConverter(),
    private val pdfUploader: PdfUploader = PdfUploader()
) {
    fun uploadEmails() {
        val email = emailReceiver.receiveEmail()
        if (email == null) {
            println("No emails today.")
            return
        }

        val pdfFile = emailToPdfConverter.convert(email)
        pdfUploader.uploadPdf(pdfFile, siteUrl = site)
    }
}

fun main() {
    val emailUploader = EmailUploader("email.storage.com")
    emailUploader.uploadEmails()
}