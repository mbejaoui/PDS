package services
import com.sendgrid._

import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal

trait IEmailService {
  private final val log = LoggerFactory.getLogger(this.getClass)
  val sendGrid: SendGrid

  def sendMail(from: String, fromName: String, to: List[String], mailSubject: String, bodyHtml: String, bcc: Seq[String] = Seq.empty,
    cc: List[String] = List.empty, replyTo: Option[String] = None, attachments: Option[List[Attachments]] = None) = {
    Future {
      log.info(s"send mail about $mailSubject to $to")
      val sender = new Email(from, fromName)
      val content = new Content("text/html", bodyHtml)
      val mail = new Mail()
      mail.setFrom(sender)
      mail.setSubject(mailSubject)
      mail.addContent(content)
      if (replyTo.isDefined) mail.setReplyTo(new Email(replyTo.get))

      val persionalization = new Personalization
      to.distinct.foreach { toEmail => persionalization.addTo(new Email(toEmail)) }
      cc.distinct.foreach { toCc => persionalization.addCc(new Email(toCc)) }
      bcc.distinct.foreach { toBcc => persionalization.addBcc(new Email(toBcc)) }
      mail.addPersonalization(persionalization)

      attachments.getOrElse(List.empty).foreach { att => mail.addAttachments(att) }
      log.info(s"insert new mailAttachments: $attachments")

      val request = new Request()
      request.setMethod(Method.POST)
      request.setEndpoint("mail/send")
      request.setBody(mail.build())
      val result: Response = sendGrid.api(request)
      println("-------------------------------")
      println(result.getBody)
      println(result.getHeaders)
      println(result.getStatusCode)
      result
    }.recover {
      case NonFatal(e) =>
        log.error(s"could not send mail, ", e)
        e.printStackTrace()
    }
  }
}
