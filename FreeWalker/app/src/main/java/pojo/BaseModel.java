package pojo;

import utils.CommonUtils;

/**
 * Created by zhangyanye on 2015/9/13.
 * Description: plain-old java object extends this model
 */
public abstract class BaseModel {
    public String toJsonString() {
        return CommonUtils.gson.toJson(this);
    }
}
