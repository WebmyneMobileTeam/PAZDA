package com.xitij.adzap.helpers;

import com.android.volley.VolleyError;

/**
 * Created by Krishna on 19-04-2015.
 */
public interface IService {

    public void response(String response);

   public void error(VolleyError error);
}
