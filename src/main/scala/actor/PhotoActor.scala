package actor

import java.io.{File, InputStream}

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{ObjectMetadata, PutObjectRequest}

object PhotoActor {
  case class UploadPhoto(inputStream: InputStream, fileName: String, contentType: String)

  def props(client: AmazonS3, bucketName: String) = Props(new PhotoActor(client, bucketName))
}

class PhotoActor(client: AmazonS3, bucketName: String) extends Actor with ActorLogging {
  import PhotoActor._

  override def receive: Receive = {
    case UploadPhoto(inputStream, fileName, contentType) =>
      // Upload a file as a new object with ContentType and title specified.
      val metadata = new ObjectMetadata()

      val key = s"photos/$fileName"

      metadata.setContentType(contentType)
      val request = new PutObjectRequest(bucketName, key, inputStream, metadata)
      val result = client.putObject(request)

      sender() ! SuccessfulResponse(201, s"file version: ${result.getVersionId}")

      log.info("Successfully put objet with filename: {} to AWS S3", fileName)

      context.stop(self)
  }

}
