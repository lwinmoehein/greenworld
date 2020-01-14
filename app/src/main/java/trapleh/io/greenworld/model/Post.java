package trapleh.io.greenworld.model;

public class Post {
    private String id,userid,username,profileurl,posttitle,imageurl;
    private int likes,comments;
    private Object posteddate;
    private boolean hasimage;

    public boolean isHasimage() {
        return hasimage;
    }

    public void setHasimage(boolean hasimage) {
        this.hasimage = hasimage;
    }


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Post(String id, String userid, String username, String userprofile, String posttitle, boolean hasimage, String imageurl, int likes, int comments, Object posteddate) {
        this.id = id;
        this.userid = userid;
        this.username = username;
        this.profileurl = userprofile;
        this.posttitle = posttitle;
        this.hasimage=hasimage;
        this.likes = likes;
        this.comments = comments;
        this.posteddate = posteddate;
        this.imageurl=imageurl;
    }
    public Post(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProfileUrl() {
        return profileurl;
    }

    public void setProfileUrl(String userprofile) {
        this.profileurl = userprofile;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Object getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(Object posteddate) {
        this.posteddate = posteddate;
    }
}
