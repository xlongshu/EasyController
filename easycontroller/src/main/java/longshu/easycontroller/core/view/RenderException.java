package longshu.easycontroller.core.view;

/**
 * RenderException
 *
 * @author LongShu 2017/05/11
 */
public class RenderException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RenderException() {
    }

    public RenderException(String message) {
        super(message);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenderException(Throwable cause) {
        super(cause);
    }

}
