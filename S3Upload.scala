
import scala.util.Properties
import java.io.File
import java.util.{List, Map}
import $ivy.`com.amazonaws:aws-java-sdk-s3:1.11.445`
import $ivy.`com.amazonaws:aws-java-sdk-bom:1.11.445`
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.s3.model._

val S3_SECRET_KEY  = Properties.envOrElse("S3_SECRET_KEY", "")
val S3_ACCESS_KEY  = Properties.envOrElse("S3_ACCESS_KEY", "")
val S3_BUCKET_NAME = Properties.envOrElse("S3_BUCKET_NAME", "")
val S3_URL_PREFIX  = Properties.envOrElse("S3_URL_PREFIX", "https://s3.amazonaws.com")

val credentials = new BasicAWSCredentials(S3_ACCESS_KEY, S3_SECRET_KEY)
val s3: AmazonS3 = AmazonS3ClientBuilder.standard()
     .withCredentials(new AWSStaticCredentialsProvider(credentials)).build()

def uploadToS3(fileName: String, uploadPath: String): String = {
  s3.putObject(S3_BUCKET_NAME, uploadPath, new File(fileName))

  val acl = s3.getObjectAcl(S3_BUCKET_NAME, uploadPath)
  acl.grantPermission(GroupGrantee.AllUsers, Permission.Read)
  s3.setObjectAcl(S3_BUCKET_NAME, uploadPath, acl)

  s"$S3_URL_PREFIX/$S3_BUCKET_NAME/$uploadPath"
}

def getContentType(fileName: String): String = {
  if (fileName.endsWith(".png")) { "image/png" }
  else if (fileName.endsWith(".gif")) { "image/gif" }
  else if (fileName.endsWith(".jpg")) { "image/jpeg" }
  else if (fileName.endsWith(".jpeg")) { "image/jpeg" }
  else if (fileName.endsWith(".webp")) { "image/webp" }
  else if (fileName.endsWith(".bmp")) { "image/bmp" }
  else if (fileName.endsWith(".svg")) { "image/svg+xml" }
  else if (fileName.endsWith(".ico")) { "image/x-icon" }
  else if (fileName.endsWith(".flv")) { "video/x-flv" }
  else if (fileName.endsWith(".mov")) { "video/mp4" }
  else if (fileName.endsWith(".m3u8")) { "application/x-mpegURL" }
  else if (fileName.endsWith(".ts")) { "video/MP2T" }
  else if (fileName.endsWith(".3gp")) { "video/3gp" }
  else if (fileName.endsWith(".mov")) { "video/quicktime" }
  else if (fileName.endsWith(".avi")) { "video/x-msvideo" }
  else if (fileName.endsWith(".wmv")) { "video/x-ms-wmv" }
  else if (fileName.endsWith(".ogg")) { "video/ogg" }
  else if (fileName.endsWith(".webm")) { "video/webm" }
  else "application/octet-stream"
}




# upload
uploadToS3("D:/230220152974.png", "dir/230220152974.png")

# getting meta data:
val meta:ObjectMetadata = s3.getObjectMetadata(S3_BUCKET_NAME, "dir/230220152974.png")
val mapMeta:Map[String, String] = meta.getUserMetadata

# getting tag
val getTaggingRequest = new GetObjectTaggingRequest(S3_BUCKET_NAME, "dir/230220152974.png");
val tags:List[Tag] = s3.getObjectTagging(getTaggingRequest).getTagSet
tags.forEach(x => println(x.getKey, x.getValue))
