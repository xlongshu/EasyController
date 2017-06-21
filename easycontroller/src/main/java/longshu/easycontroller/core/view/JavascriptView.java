package longshu.easycontroller.core.view;

import longshu.easycontroller.core.MediaType;

/**
 * JavascriptView
 *
 * @author LongShu 2017/05/11
 */
public class JavascriptView extends TextView {

    public JavascriptView(String jsText) {
        super(jsText, MediaType.APPLICATION_JAVASCRIPT);
    }

}
