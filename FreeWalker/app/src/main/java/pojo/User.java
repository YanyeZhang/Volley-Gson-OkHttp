package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangyanye on 2015/9/13.
 * Description:user model
 */
public class User extends BaseModel {

    @SerializedName("uid")
    @Expose
    public int id;

    @Expose
    public String nickName;

    @Expose
    public String password;

    @Expose
    public String account;

    @Expose
    public String phone;
}