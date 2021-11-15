package slack.api

import com.github.dapperware.slack.api.web
import com.github.dapperware.slack.SlackClient
import com.github.dapperware.slack.models.RemoteFile
import zio.test._
import zio.test.Assertion
import Assertion._
import sttp.client3.asynchttpclient.zio.stubbing._
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.asynchttpclient.zio.SttpClientStubbing
import zio.{ Chunk, Has, Layer }
import com.github.dapperware.slack.models.Shares

object SlackRemoteFilesSpec extends DefaultRunnableSpec with MockSttpBackend {

  private val response               = """
                       {
                        "ok": true,
                        "file": {
                            "id": "F0GDJ3XMH",
                            "created": 1563919925,
                            "timestamp": 1563919925,
                            "name": "LeadvilleAndBackAgain",
                            "title": "LeadvilleAndBackAgain",
                            "mimetype": "application/vnd.slack-remote",
                            "filetype": "remote",
                            "pretty_type": "Remote",
                            "user": "U0F8RBVNF",
                            "editable": false,
                            "size": 0,
                            "mode": "external",
                            "is_external": true,
                            "external_type": "app",
                            "is_public": false,
                            "public_url_shared": false,
                            "display_as_bot": false,
                            "username": "",
                            "url_private": "https://docs.google.com/document/d/1TA9fIaph4eSz2fC_1JGMuYaYUc4IvieIop0WqfCXw5Y/edit?usp=sharing",
                            "permalink": "https://kraneflannel.slack.com/files/U0F8RBVNF/F0GDJ3XMH/leadvilleandbackagain",
                            "comments_count": 0,
                            "is_starred": false,
                            "shares": {
                              "public": {},
                              "private": {}
                            },
                            "channels": [],
                            "groups": [],
                            "ims": [],
                            "external_id": "1234",
                            "external_url": "https://docs.google.com/document/d/1TA9fIaph4eSz2fC_1JGMuYaYUc4IvieIop0WqfCXw5Y/edit?usp=sharing",
                            "has_rich_preview": false
                        }
                    }
                """
  private val minimumExpectedContent = List(
    "Part(filetype,StringBody(txt,utf-8,text/plain)",
    "Part(external_id,StringBody(foo-external-id,utf-8,text/plain)",
    "Part(external_url,StringBody(foo-external-url,utf-8,text/plain)",
    "Part(title,StringBody(foo-title,utf-8,text/plain)"
  )

  private val expectedResponse = RemoteFile(
    id = "F0GDJ3XMH",
    created = 1563919925,
    timestamp = 1563919925,
    name = "LeadvilleAndBackAgain",
    title = "LeadvilleAndBackAgain",
    mimetype = "application/vnd.slack-remote",
    filetype = "remote",
    pretty_type = "Remote",
    user = "U0F8RBVNF",
    editable = false,
    size = 0,
    mode = "external",
    is_external = true,
    external_type = "app",
    is_public = false,
    public_url_shared = false,
    display_as_bot = false,
    username = "",
    url_private = "https://docs.google.com/document/d/1TA9fIaph4eSz2fC_1JGMuYaYUc4IvieIop0WqfCXw5Y/edit?usp=sharing",
    permalink = "https://kraneflannel.slack.com/files/U0F8RBVNF/F0GDJ3XMH/leadvilleandbackagain",
    comments_count = 0,
    is_starred = false,
    shares = Shares(Map.empty, Map.empty),
    channels = List.empty,
    groups = List.empty,
    ims = List.empty,
    external_id = "1234",
    external_url = "https://docs.google.com/document/d/1TA9fIaph4eSz2fC_1JGMuYaYUc4IvieIop0WqfCXw5Y/edit?usp=sharing",
    has_rich_preview = false
  )

  private val stubLayer: Layer[Nothing, SttpClient with Has[SttpClientStubbing.Service]] = sttbBackEndStubLayer

  private def isExpectedContent(reqString: String, expectedBits: String*): Boolean =
    (expectedBits.toList ++ minimumExpectedContent).forall(reqString.contains)

  override def spec: ZSpec[Environment, Failure] = suite("Remote Files")(
    testM("sends parameters to add remote files - WITHOUT indexable file contents and preview image") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/files.remote.add" &&
          req.header("Authorization") == Some("Bearer foo-access-token") &&
          req.body.show == "multipart: filetype,external_id,external_url,title" &&
          isExpectedContent(req.toString)
      ).thenRespond(response)

      val effect = stubEffect *> web.addRemoteFile("foo-external-id", "foo-external-url", "foo-title", Some("txt"))
      assertM(effect)(equalTo(expectedResponse))
    },
    testM("sends parameters to add remote files - WITH indexable file contents") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/files.remote.add" &&
          req.header("Authorization") == Some("Bearer foo-access-token") &&
          req.body.show == "multipart: filetype,indexable_file_contents,external_id,external_url,title" &&
          isExpectedContent(req.toString, "Part(indexable_file_contents,ByteArrayBody(")
      ).thenRespond(response)

      val effect = stubEffect *> web.addRemoteFile(
        "foo-external-id",
        "foo-external-url",
        "foo-title",
        Some("txt"),
        indexableFileContents = Some(Chunk.empty)
      )
      assertM(effect)(equalTo(expectedResponse))
    },
    testM("sends parameters to add remote files - WITH indexable file contents and preview image") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/files.remote.add" &&
          req.header("Authorization") == Some("Bearer foo-access-token") &&
          req.body.show == "multipart: filetype,indexable_file_contents,preview_image,external_id,external_url,title" &&
          isExpectedContent(
            req.toString,
            "Part(preview_image,ByteArrayBody(",
            "Part(indexable_file_contents,ByteArrayBody("
          )
      ).thenRespond(response)

      val effect = stubEffect *> web.addRemoteFile(
        "foo-external-id",
        "foo-external-url",
        "foo-title",
        Some("txt"),
        indexableFileContents = Some(Chunk.empty),
        previewImage = Some(Chunk.empty)
      )
      assertM(effect)(equalTo(expectedResponse))
    }
  ).provideLayer((stubLayer >>> SlackClient.live) ++ accessTokenLayer("foo-access-token") ++ stubLayer)

}
