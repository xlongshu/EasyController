package longshu.easycontroller.core.config;

import lombok.NonNull;
import longshu.easycontroller.core.view.ViewType;
import longshu.easycontroller.json.Json;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constants
 *
 * @author LongShu 2017/05/09
 */
@lombok.Data
public class Constants implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_VIEW_EXTENSION = ".jsp";
    public static final String DEFAULT_BASE_UPLOAD_PATH = "upload";
    public static final String DEFAULT_BASE_PHOTOS_PATH = "photos";

    // -- 配置 --
    private boolean devMode = false;
    @NonNull
    private String encoding = DEFAULT_ENCODING;
    private String viewExtension = DEFAULT_VIEW_EXTENSION;
    @NonNull
    private String contextPathName = "base";
    @NonNull
    private String datePattern = "yyyy-MM-dd HH:mm:ss";// 日期格式
    @NonNull
    private ViewType viewType = ViewType.JSP;

    private Map<Integer, String> errorViewMapping = new HashMap<Integer, String>();

    @NonNull
    private String baseUploadPath = DEFAULT_BASE_UPLOAD_PATH;// 文件上传路径
    @NonNull
    private String baseDownloadPath = DEFAULT_BASE_UPLOAD_PATH;// 文件下载的路径(前缀,一般和上传路径一致)
    @NonNull
    private String basePhotoPathName = DEFAULT_BASE_PHOTOS_PATH;// 用户头像,默认在webapp/photos

    /**
     * 文件上传配置
     */
    private int sizeThreshold = 2 << 10;// 向硬盘写数据时所用的缓冲区大小, 2K
    private int formMaxSize = 1024 << 20;// 表单大小限制, 1024M
    private int fileMaxSize = 512 << 20;// 单文件大小限制, 512 << 20
    private List<String> allowFileType = new ArrayList<String>();// 允许上传的文件类型, 默认所有

    /**
     * 小文件下载缓存配置
     */
    private int fileCacheSize = 64;// 文件缓存数量(最多)
    private int maxFileCachetSize = 1 << 20;// 最大的缓存文件大小, 1M
    private long fileCachetimeout = 3600 * 24;// 缓存时间, 24h

    // ----
    @NonNull
    private File webRootPath;
    private static final Constants me = new Constants();

    private Constants() {
    }

    public static Constants me() {
        return me;
    }

    public void setSizeThreshold(int sizeThreshold) {
        if (sizeThreshold > (1 << 20)) {// 1M
            throw new IllegalArgumentException("fileCacheSize is to large.");
        }
        this.sizeThreshold = sizeThreshold;
    }

    public String getBaseUploadPath() {
        if (DEFAULT_BASE_UPLOAD_PATH.equals(baseUploadPath)) {// 默认在 webapp/upload
            baseUploadPath = new File(webRootPath, DEFAULT_BASE_UPLOAD_PATH).getAbsolutePath();
        }
        return baseUploadPath;
    }

    /**
     * 文件缓存数量
     */
    public void setFileCacheSize(int fileCacheSize) {
        if (fileCacheSize > 128) {
            throw new IllegalArgumentException("fileCacheSize is to large.");
        }
        this.fileCacheSize = fileCacheSize;
    }

    /**
     * 允许上传的文件类型, 多个用 ; 分隔
     */
    public void addAllowUploadFileType(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        if (string.contains(",")) {
            String[] types = string.split(";");
            for (String type : types) {
                type = type.trim();
                if (!allowFileType.contains(type)) {
                    allowFileType.add(type);
                }
            }
        } else {
            string = string.trim();
            if (!allowFileType.contains(string)) {
                allowFileType.add(string);
            }
        }
    }

    /**
     * 最大的缓存文件大小
     */
    public void setMaxFileCachetSize(int maxFileCachetSize) {
        if (maxFileCachetSize > 8 << 20) { // 不得超过8M
            throw new IllegalArgumentException("maxFileCachetSize is to large.");
        }
        this.maxFileCachetSize = maxFileCachetSize;
    }

    public void setErrorView(int errorCode, String errorView) {
        errorViewMapping.put(errorCode, errorView);
    }

    public String getErrorView(int errorCode) {
        return errorViewMapping.get(errorCode);
    }

    public void setDefaultJson(Json json) {
        Json.setDefaultJson(json);
    }

}
