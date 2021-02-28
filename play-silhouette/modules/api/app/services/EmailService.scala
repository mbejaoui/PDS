package services
import akka.event.slf4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import com.sendgrid.SendGrid
import javax.inject._
import org.slf4j.LoggerFactory

@Singleton()
case class EmailService @Inject() () extends IEmailService {
  val sendGridApiKey = "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"
  override val sendGrid: SendGrid = new SendGrid(sendGridApiKey)

  def sendMessage(email: String) = {
    val mailObject = s"Bonjour Mr !"
    val template: play.twirl.api.HtmlFormat.Appendable = services.html.Message(email)
    sendMail("maher.bjaoui@sesame.com.tn", "Marhba fi garderie mahra", List(email), mailObject, template.body)
  }

}
