package hencoder.cxb.com.hencodercourse;

public class AuthorResp extends HttpResp {
    //{"name":"朱凯","nick_name":"扔物线","github":"https://github.com/rengwuxian"}
    public String name;
    public String nick_name;
    public String github;

    @Override
    public String toString() {
        return "AuthorResp{" +
                "name='" + name + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", github='" + github + '\'' +
                '}';
    }
}
