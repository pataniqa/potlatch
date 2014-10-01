package com.pataniqa.coursera.potlatch.server;

import static org.junit.Assert.assertEquals;
import lombok.Getter;

import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi;
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder;

public class AutoGradingTest {

    private class ErrorRecorder implements ErrorHandler {

        @Getter private RetrofitError error;

        @Override
        public Throwable handleError(RetrofitError cause) {
            error = cause;
            return error.getCause();
        }

    }

    private final String TEST_URL = "https://localhost:8443";

    private final String USERNAME1 = "admin";
    private final String USERNAME2 = "user0";
    private final String PASSWORD = "pass";
    private final String CLIENT_ID = "mobile";

    private RemoteGiftApi readWriteVideoSvcUser1 = new SecuredRestBuilder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL).loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
            .setLogLevel(LogLevel.FULL).username(USERNAME1).password(PASSWORD).clientId(CLIENT_ID)
            .build().create(RemoteGiftApi.class);

    private RemoteGiftApi readWriteVideoSvcUser2 = new SecuredRestBuilder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL).loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
            // .setLogLevel(LogLevel.FULL)
            .username(USERNAME2).password(PASSWORD).clientId(CLIENT_ID).build()
            .create(RemoteGiftApi.class);

    private Gift gift = TestData.randomGift();

    @Test
    public void testAddVideoMetadata() throws Exception {
        Gift received = readWriteVideoSvcUser1.insert(gift);
        assertEquals(gift.getTitle(), received.getTitle());
        assertEquals(gift.getDescription(), received.getDescription());
        assertEquals(gift.getGiftChainID(), received.getGiftChainID());
        assertEquals(gift.getUserID(), received.getUserID());
    }

    // @Test
    // public void testAddGetVideo() throws Exception {
    // readWriteVideoSvcUser1.addVideo(video);
    // Collection<Video> stored = readWriteVideoSvcUser1.getVideoList();
    // assertTrue(stored.contains(video));
    // }
    //
    // @Test
    // public void testDenyVideoAddWithoutOAuth() throws Exception {
    // ErrorRecorder error = new ErrorRecorder();
    //
    // // Create an insecure version of our Rest Adapter that doesn't know how
    // // to use OAuth.
    // RemoteGiftApi insecurevideoService = new RestAdapter.Builder()
    // .setClient(
    // new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
    // .setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
    // .setErrorHandler(error).build().create(VideoSvcApi.class);
    // try {
    // // This should fail because we haven't logged in!
    // insecurevideoService.addVideo(video);
    //
    // fail("Yikes, the security setup is horribly broken and didn't require the user to authenticate!!");
    //
    // } catch (Exception e) {
    // // Ok, our security may have worked, ensure that
    // // we got a 401
    // assertEquals(HttpStatus.SC_UNAUTHORIZED, error.getError()
    // .getResponse().getStatus());
    // }
    //
    // // We should NOT get back the video that we added above!
    // Collection<Video> videos = readWriteVideoSvcUser1.getVideoList();
    // assertFalse(videos.contains(video));
    // }
    //
    // @Test
    // public void testLikeCount() throws Exception {
    //
    // // Add the video
    // Video v = readWriteVideoSvcUser1.addVideo(video);
    //
    // // Like the video
    // readWriteVideoSvcUser1.likeVideo(v.getId());
    //
    // // Get the video again
    // v = readWriteVideoSvcUser1.getVideoById(v.getId());
    //
    // // Make sure the like count is 1
    // assertTrue(v.getLikes() == 1);
    //
    // // Unlike the video
    // readWriteVideoSvcUser1.unlikeVideo(v.getId());
    //
    // // Get the video again
    // v = readWriteVideoSvcUser1.getVideoById(v.getId());
    //
    // // Make sure the like count is 0
    // assertTrue(v.getLikes() == 0);
    // }
    //
    // @Test
    // public void testLikedBy() throws Exception {
    //
    // // Add the video
    // Video v = readWriteVideoSvcUser1.addVideo(video);
    //
    // // Like the video
    // readWriteVideoSvcUser1.likeVideo(v.getId());
    //
    // Collection<String> likedby =
    // readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());
    //
    // // Make sure we're on the list of people that like this video
    // assertTrue(likedby.contains(USERNAME1));
    //
    // // Have the second user like the video
    // readWriteVideoSvcUser2.likeVideo(v.getId());
    //
    // // Make sure both users show up in the like list
    // likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());
    // assertTrue(likedby.contains(USERNAME1));
    // assertTrue(likedby.contains(USERNAME2));
    //
    // // Unlike the video
    // readWriteVideoSvcUser1.unlikeVideo(v.getId());
    //
    // // Get the video again
    // likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());
    //
    // // Make sure user1 is not on the list of people that liked this video
    // assertTrue(!likedby.contains(USERNAME1));
    //
    // // Make sure that user 2 is still there
    // assertTrue(likedby.contains(USERNAME2));
    // }
    //
    // )
    // @Test
    // public void testLikingTwice() throws Exception {
    //
    // // Add the video
    // Video v = readWriteVideoSvcUser1.addVideo(video);
    //
    // // Like the video
    // readWriteVideoSvcUser1.likeVideo(v.getId());
    //
    // // Get the video again
    // v = readWriteVideoSvcUser1.getVideoById(v.getId());
    //
    // // Make sure the like count is 1
    // assertEquals(1, v.getLikes());
    //
    // try {
    // // Like the video again.
    // readWriteVideoSvcUser1.likeVideo(v.getId());
    //
    // fail("The server let us like a video twice without returning a 400");
    // } catch (RetrofitError e) {
    // // Make sure we got a 400 Bad Request
    // assertEquals(400, e.getResponse().getStatus());
    // }
    //
    // // Get the video again
    // v = readWriteVideoSvcUser1.getVideoById(v.getId());
    //
    // // Make sure the like count is still 1
    // assertEquals(1, v.getLikes());
    // }
    //
    // @Test
    // public void testLikingNonExistantVideo() throws Exception {
    //
    // try {
    // // Like the video again.
    // readWriteVideoSvcUser1.likeVideo(getInvalidVideoId());
    //
    // fail("The server let us like a video that doesn't exist without returning a 404.");
    // } catch (RetrofitError e) {
    // // Make sure we got a 400 Bad Request
    // assertEquals(404, e.getResponse().getStatus());
    // }
    // }
    //
    // @Test
    // public void testFindByName() {
    //
    // // Create the names unique for testing.
    // String[] names = new String[3];
    // names[0] = "The Cat";
    // names[1] = "The Spoon";
    // names[2] = "The Plate";
    //
    // // Create three random videos, but use the unique names
    // ArrayList<Video> videos = new ArrayList<Video>();
    //
    // for (int i = 0; i < names.length; ++i) {
    // videos.add(TestData.randomVideo());
    // videos.get(i).setName(names[i]);
    // }
    //
    // // Add all the videos to the server
    // for (Video v : videos){
    // readWriteVideoSvcUser1.addVideo(v);
    // }
    //
    // // Search for "The Cat"
    // Collection<Video> searchResults =
    // readWriteVideoSvcUser1.findByTitle(names[0]);
    // assertTrue(searchResults.size() > 0);
    //
    // // Make sure all the returned videos have "The Cat" for their title
    // for (Video v : searchResults) {
    // assertTrue(v.getName().equals(names[0]));
    // }
    // }
    //
    // @Test
    // public void testFindByDurationLessThan() {
    //
    // // Create the durations unique for testing.
    // long[] durations = new long[3];
    // durations[0] = 1;
    // durations[1] = 5;
    // durations[2] = 9;
    //
    // // Create three random videos, but use the unique durations
    // ArrayList<Video> videos = new ArrayList<Video>();
    //
    // for (int i = 0; i < durations.length; ++i) {
    // videos.add(TestData.randomVideo());
    // videos.get(i).setDuration(durations[0]);
    // }
    //
    // // Add all the videos to the server
    // for (Video v : videos){
    // readWriteVideoSvcUser1.addVideo(v);
    // }
    //
    // // Search for "The Cat"
    // Collection<Video> searchResults =
    // readWriteVideoSvcUser1.findByDurationLessThan(6L);
    // // Make sure that we have at least two videos
    // assertTrue(searchResults.size() > 1);
    //
    // for (Video v : searchResults) {
    // // Make sure that all of the videos are of the right duration
    // assertTrue(v.getDuration() < 6);
    // }
    // }
    //
    // private long getInvalidVideoId() {
    // Set<Long> ids = new HashSet<Long>();
    // Collection<Video> stored = readWriteVideoSvcUser1.getVideoList();
    // for (Video v : stored) {
    // ids.add(v.getId());
    // }
    //
    // long nonExistantId = Long.MIN_VALUE;
    // while (ids.contains(nonExistantId)) {
    // nonExistantId++;
    // }
    // return nonExistantId;
    // }

}
