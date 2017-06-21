package longshu.easycontroller.core.view;

import longshu.easycontroller.core.Action;

/**
 * ViewResolver
 *
 * @author LongShu 2017/05/14
 * @see DefaultViewResolver
 */
public interface ViewResolver {

    View resolveView(Action action, Object returnValue);

}
