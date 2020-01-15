package trapleh.io.greenworld.model;


public class Comment {
    private String id,postid,userid,username,profileurl,commenttitle;
    private int likes;
    private Object posteddate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getCommenttitle() {
        return commenttitle;
    }

    public void setCommenttitle(String commenttitle) {
        this.commenttitle = commenttitle;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Object getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(Object posteddate) {
        this.posteddate = posteddate;
    }

    public Comment(String id, String postid, String userid, String username, String userprofile, String commenttitle , int likes, Object posteddate) {
        this.id = id;
        this.userid = userid;
        this.username = username;
        this.profileurl = userprofile;
        this.postid=postid;
        this.commenttitle = commenttitle;
        this.likes = likes;
        this.posteddate = posteddate;
    }
    public Comment(){}

}