package com.mango.blog.User;

import com.mango.blog.Comment.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User user;
    @BeforeEach
    void setUp() {
        user = new User("testUser", "testPassword", "testEmail");
    }

    @Test
    public void getUserName() {
        assertEquals("testUser", user.getUserName(), "User name should be testUser");
    }

    @Test
    public void setUserName() {
        user.setUserName("newTestUser");
        assertEquals("newTestUser", user.getUserName(), "User name should be newTestUser");
    }

    @Test
    public void getUserPassword() {
        assertEquals("testPassword", user.getUserPassword(), "User password should be testPassword");
    }

    @Test
    public void setUserPassword() {
        user.setUserPassword("newTestPassword");
        assertEquals("newTestPassword", user.getUserPassword(), "User password should be newTestPassword");
    }

    @Test
    public void getEmail() {
        assertEquals("testEmail", user.getEmail(), "User email should be testEmail");
    }

    @Test
    public void setEmail() {
        user.setEmail("newTestEmail");
        assertEquals("newTestEmail", user.getEmail(), "User email should be newTestEmail");
    }

    @Test
    public void getPostsAndCreatePost() {
        user.createPost("testTitle", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        assertEquals("testTitle", user.getPosts().get(0).getPostName(), "Post title should be testTitle");
        assertEquals("testContent", user.getPosts().get(0).getText(), "Post content should be testContent");
        assertEquals(user.getUserName(), user.getPosts().get(0).getAuthor(), "Post author should be testUser");
        assertEquals("unitTesting", user.getPosts().get(0).getGenre(), "Post genre should be unitTesting");
    }

    @Test
    public void getUserGroupsAndCreateGroup() {
        user.addGroup("testGroup");
        assertEquals(1, user.getUserGroups().size(), "User should have 1 group");
        assertEquals("testGroup", user.getUserGroups().keySet().toArray()[0], "Group name should be testGroup");
    }



    @Test
    public void testCreatePost() {
        user.createPost("testTitle", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        assertEquals("testTitle", user.getPosts().get(0).getPostName(), "Post title should be testTitle");
        assertEquals("testContent", user.getPosts().get(0).getText(), "Post content should be testContent");
        assertEquals(user.getUserName(), user.getPosts().get(0).getAuthor(), "Post author should be testUser");
        assertEquals("unitTesting", user.getPosts().get(0).getGenre(), "Post genre should be unitTesting");
        user.createPost("testTitle2", "testContent2", user.getUserName(), "unitTesting", "BASE64", "image/png");
        assertEquals(2, user.getPosts().size(), "User should have 2 posts");
        assertEquals("testTitle2", user.getPosts().get(1).getPostName(), "Post title should be testTitle2");
        assertEquals("testContent2", user.getPosts().get(1).getText(), "Post content should be testContent2");
        assertEquals(user.getUserName(), user.getPosts().get(1).getAuthor(), "Post author should be testUser");
        assertEquals("unitTesting", user.getPosts().get(1).getGenre(), "Post genre should be unitTesting");
        assertEquals("BASE64", user.getPosts().get(1).getMedia(), "Post image should be BASE64");
        assertEquals("image/png", user.getPosts().get(1).getFileExtension(), "Post image type should be image/png");
    }

    // Postname, text, genre, author
    @Test
    public void deletePost() {
        user.createPost("testTitle", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        user.deletePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName(), user.getPosts().get(0).getText(), user.getPosts().get(0).getGenre(), user.getPosts().get(0).getAuthor());
        assertEquals(0, user.getPosts().size(), "User should have 0 posts");
        user.createPost("testTitle", "testContent", user.getUserName(), "unitTesting", "BASE64", "image/png");
        assertEquals(1, user.getPosts().size(), "User should have 1 multimedia post");
        user.deletePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName(), user.getPosts().get(0).getText(), user.getPosts().get(0).getGenre(), user.getPosts().get(0).getAuthor());
        assertEquals(0, user.getPosts().size(), "User should have 0 multimedia posts");
    }

    @Test
    public void updatePost() {
        user.createPost("testTitle", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        user.updatePost(user.getPosts().get(0).getPostID(), "newTestTitle", "newTestContent", "unitTesting", user.getUserName());
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        assertEquals("newTestTitle", user.getPosts().get(0).getPostName(), "Post title should be newTestTitle");
        assertEquals("newTestContent", user.getPosts().get(0).getText(), "Post content should be newTestContent");
        assertEquals(user.getUserName(), user.getPosts().get(0).getAuthor(), "Post author should be testUser");
        assertEquals("unitTesting", user.getPosts().get(0).getGenre(), "Post genre should be unitTesting");
        user.createPost("testTitle", "testContent", user.getUserName(), "unitTesting", "BASE64", "png");
        assertEquals(2, user.getPosts().size(), "User should have 2 posts");
        user.updatePost(user.getPosts().get(1).getPostID(), "newTestTitle", "newTestContent", "unitTesting", user.getUserName(), "newBASE64", "jpg");
        assertEquals(2, user.getPosts().size(), "User should have 2 multimedia posts");
        assertEquals("newTestTitle", user.getPosts().get(1).getPostName(), "Post title should be newTestTitle");
        assertEquals("newTestContent", user.getPosts().get(1).getText(), "Post content should be newTestContent");
        assertEquals(user.getUserName(), user.getPosts().get(1).getAuthor(), "Post author should be testUser");
        assertEquals("unitTesting", user.getPosts().get(1).getGenre(), "Post genre should be unitTesting");
        assertEquals("newBASE64", user.getPosts().get(1).getMedia(), "Post image should be BASE64");
        assertEquals("jpg", user.getPosts().get(1).getFileExtension(), "Post image type should be image/png");
    }

    @Test
    public void addFavoritePost() {
        user.createPost("testPost", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        user.addFavoritePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName());
        assertEquals(1, user.getFavoritePosts().size(), "User should have 1 favorite post");
        assertTrue(user.getFavoritePosts().get(0).containsKey(user.getPosts().get(0).getPostID()), "User should have testPost as a favorite post");
    }

    @Test
    public void unfavoritePost() {
        user.createPost("testPost", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        user.addFavoritePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName());
        assertEquals(1, user.getFavoritePosts().size(), "User should have 1 favorite post");
        assertTrue(user.getFavoritePosts().get(0).containsKey(user.getPosts().get(0).getPostID()), "User should have testPost as a favorite post");
        user.unfavoritePost(user.getPosts().get(0).getPostID());
        assertEquals(0, user.getFavoritePosts().size(), "User should have 0 favorite posts");
    }

    @Test
    public void getFavoritePosts() {
        user.createPost("testPost", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        user.addFavoritePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName());
        assertEquals(1, user.getFavoritePosts().size(), "User should have 1 favorite post");
        assertTrue(user.getFavoritePosts().get(0).containsKey(user.getPosts().get(0).getPostID()), "User should have testPost as a favorite post");
        user.createPost("testPost2", "testContent2", user.getUserName(), "unitTesting");
        assertEquals(2, user.getPosts().size(), "User should have 2 posts");
        user.addFavoritePost(user.getPosts().get(1).getPostID(), user.getPosts().get(1).getPostName());
        assertEquals(2, user.getFavoritePosts().size(), "User should have 2 favorite posts");
        assertTrue(user.getFavoritePosts().get(1).containsKey(user.getPosts().get(1).getPostID()), "User should have testPost2 as a favorite post");
    }

    @Test
    public void isFavorite() {
        user.createPost("testPost", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        user.addFavoritePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName());
        assertEquals(1, user.getFavoritePosts().size(), "User should have 1 favorite post");
        assertTrue(user.getFavoritePosts().get(0).containsKey(user.getPosts().get(0).getPostID()), "User should have testPost as a favorite post");
        assertTrue(user.isFavorite(user.getPosts().get(0).getPostID()), "User should have testPost as a favorite post");
        user.createPost("testPost2", "testContent2", user.getUserName(), "unitTesting");
        assertEquals(2, user.getPosts().size(), "User should have 2 posts");
        user.addFavoritePost(user.getPosts().get(1).getPostID(), user.getPosts().get(1).getPostName());
        assertEquals(2, user.getFavoritePosts().size(), "User should have 2 favorite posts");
        assertTrue(user.getFavoritePosts().get(1).containsKey(user.getPosts().get(1).getPostID()), "User should have testPost2 as a favorite post");
        assertTrue(user.isFavorite(user.getPosts().get(1).getPostID()), "User should have testPost2 as a favorite post");
    }

    @Test
    public void addComment() {
        user.createPost("testPost", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        Comment comment = new Comment(user.getUserName(), "testComment");
        user.addComment(user.getPosts().get(0).getPostID(), comment);
        assertEquals(1, user.getPosts().get(0).getComments().size(), "Post should have 1 comment");
        assertEquals("testComment", user.getPosts().get(0).getComments().get(0).getText(), "Comment should be testComment");
        assertFalse(user.addComment("fakePostID", comment), "Comment can't be added to a post that doesn't exist");
    }

    @Test
    public void removeComment() {
        user.createPost("testPost", "testContent", user.getUserName(), "unitTesting");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        Comment comment = new Comment(user.getUserName(), "testComment");
        user.addComment(user.getPosts().get(0).getPostID(), comment);
        assertEquals(1, user.getPosts().get(0).getComments().size(), "Post should have 1 comment");
        assertEquals("testComment", user.getPosts().get(0).getComments().get(0).getText(), "Comment should be testComment");
        user.removeComment(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getComments().get(0).getCommentID());
        assertEquals(0, user.getPosts().get(0).getComments().size(), "Post should have 0 comments");
    }


    @Test
    public void getPost() {
        user.createPost("testPost", "testContent", user.getUserName(), "unitTesting", "BASE64", "png");
        assertEquals(1, user.getPosts().size(), "User should have 1 post");
        assertEquals("testPost", user.getPosts().get(0).getPostName(), "Post should be testPost");
        assertEquals("testContent", user.getPosts().get(0).getText(), "Post content should be testContent");
        assertEquals(user.getUserName(), user.getPosts().get(0).getAuthor(), "Post author should be testUser");
        assertEquals("unitTesting", user.getPosts().get(0).getGenre(), "Post genre should be unitTesting");
        assertEquals("BASE64", user.getPosts().get(0).getMedia(), "Post image should be BASE64");
        assertEquals("png", user.getPosts().get(0).getFileExtension(), "Post image type should be png");
        user.createPost("generalPost", "generalContent", user.getUserName(), "general");
        assertEquals(2, user.getPosts().size(), "User should have 2 posts");
        assertEquals("generalPost", user.getPosts().get(1).getPostName(), "Post should be generalPost");
        assertEquals("generalContent", user.getPosts().get(1).getText(), "Post content should be generalContent");
        assertEquals(user.getUserName(), user.getPosts().get(1).getAuthor(), "Post author should be testUser");
        assertEquals("general", user.getPosts().get(1).getGenre(), "Post genre should be general");
        assertNull(user.getPost("fakePost"), "User should not be able to get post that doesn't exist");
    }

    @Test void userGroups(){
        user.addGroup("testGroup");
        User testUser = new User("testUser", "testPassword", "testEmail");
        user.addUserToGroup("testGroup", "1234" ,testUser.getUserName());
        assertEquals(1, user.getGroups().size(), "User should have 1 group");
        assertFalse(user.addUserToGroup("testGroup", "1234", testUser.getUserName()), "User should not be able to add user to group that they are already in");
        assertFalse(user.addUserToGroup("fakeGroup", "1234", testUser.getUserName()), "User should not be able to add user to group that doesn't exist");
        assertNull(user.getUsersInGroup("fakeGroup"), "User should not be able to get users in group that doesn't exist");

        assertFalse(user.addGroup("testGroup"), "User should not be able to add group that already exists");
        assertFalse(user.removeGroup("fakeGroup"), "User should not be able to remove group that doesn't exist");
        assertFalse(user.removeUserFromGroup("fakeGroup", "fakeUser"), "User should not be able to remove user from group that doesn't exist");


    }
}