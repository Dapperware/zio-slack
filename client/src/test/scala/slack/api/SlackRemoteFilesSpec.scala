package slack.api

import com.github.dapperware.slack.models.File.{ FileMeta, Preview, Sharing }
import com.github.dapperware.slack.models.{ File, Shares }
import com.github.dapperware.slack.{ MockSttpBackend, Slack, SlackResponse }
import com.github.dapperware.slack.SttpStubbing.whenRequestMatches
import zio.Chunk
import zio.test.Assertion._
import zio.test._

object SlackRemoteFilesSpec extends ZIOSpecDefault with MockSttpBackend {

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

  private val expectedResponse = SlackResponse.Ok(
    File(
      id = "F0GDJ3XMH",
      meta = FileMeta(
        created = 1563919925,
        timestamp = 1563919925,
        name = Some("LeadvilleAndBackAgain"),
        title = Some("LeadvilleAndBackAgain"),
        mimetype = Some("application/vnd.slack-remote"),
        filetype = Some("remote"),
        pretty_type = Some("Remote"),
        user = Some("U0F8RBVNF"),
        editable = Some(false),
        size = Some(0),
        mode = Some("external"),
        username = Some("")
      ),
      sharing = Sharing(
        is_external = true,
        external_type = Some("app"),
        external_id = Some("1234"),
        external_url =
          Some("https://docs.google.com/document/d/1TA9fIaph4eSz2fC_1JGMuYaYUc4IvieIop0WqfCXw5Y/edit?usp=sharing"),
        display_as_bot = Some(false),
        public_url_shared = Some(false),
        is_public = Some(false),
        url_private =
          Some("https://docs.google.com/document/d/1TA9fIaph4eSz2fC_1JGMuYaYUc4IvieIop0WqfCXw5Y/edit?usp=sharing"),
        permalink = Some("https://kraneflannel.slack.com/files/U0F8RBVNF/F0GDJ3XMH/leadvilleandbackagain"),
        shares = Some(Shares(Map.empty, Map.empty)),
        channels = Some(Nil),
        groups = Some(Nil),
        ims = Some(Nil)
      ),
      preview = Preview(
        has_rich_preview = Some(false)
      )
    ),
    Nil
  )

  private def isExpectedContent(reqString: String, expectedBits: String*): Boolean =
    (expectedBits.toList ++ minimumExpectedContent).forall(reqString.contains)

  override def spec = suite("Remote Files")(
    test("sends parameters to add remote files - WITHOUT indexable file contents and preview image") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/files.remote.add" &&
          req.header("Authorization").contains("Bearer foo-access-token") &&
          req.body.show == "multipart: filetype,external_id,external_url,title" &&
          isExpectedContent(req.toString)
      ).thenRespond(response)

      val effect = stubEffect *> Slack.addRemoteFile("foo-external-id", "foo-external-url", "foo-title", Some("txt"))
      assertZIO(effect)(equalTo(expectedResponse))
    },
    test("sends parameters to add remote files - WITH indexable file contents") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/files.remote.add" &&
          req.header("Authorization").contains("Bearer foo-access-token") &&
          req.body.show == "multipart: filetype,indexable_file_contents,external_id,external_url,title" &&
          isExpectedContent(req.toString, "Part(indexable_file_contents,ByteArrayBody(")
      ).thenRespond(response)

      val effect = stubEffect *> Slack.addRemoteFile(
        "foo-external-id",
        "foo-external-url",
        "foo-title",
        Some("txt"),
        indexableFileContents = Some(Chunk.empty)
      )
      assertZIO(effect)(equalTo(expectedResponse))
    },
    test("sends parameters to add remote files - WITH indexable file contents and preview image") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/files.remote.add" &&
          req.header("Authorization").contains("Bearer foo-access-token") &&
          req.body.show == "multipart: filetype,indexable_file_contents,preview_image,external_id,external_url,title" &&
          isExpectedContent(
            req.toString,
            "Part(preview_image,ByteArrayBody(",
            "Part(indexable_file_contents,ByteArrayBody("
          )
      ).thenRespond(response)

      val effect = stubEffect *> Slack.addRemoteFile(
        "foo-external-id",
        "foo-external-url",
        "foo-title",
        Some("txt"),
        indexableFileContents = Some(Chunk.empty),
        previewImage = Some(Chunk.empty)
      )
      assertZIO(effect)(equalTo(expectedResponse))
    }
  ).provide(
    sttpBackEndStubLayer,
    Slack.http,
    accessTokenLayer("foo-access-token")
  )

}
