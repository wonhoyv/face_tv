package obria.com.videotest.listener;

import obria.com.videotest.entity.FaceRecognized;

/**
 * Created by ysj on 2018/2/27.
 */

public interface IWebSocketListener {

    void OnMessage(FaceRecognized recognize);

    void OnError();
}
