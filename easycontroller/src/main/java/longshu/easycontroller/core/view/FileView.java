package longshu.easycontroller.core.view;

import longshu.easycontroller.core.MediaType;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.util.Base64;
import lombok.Cleanup;
import longshu.easycontroller.cache.FileLFUCache;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * FileView(文件下载)
 *
 * @author LongShu 2017/05/11
 */
public class FileView extends AbstractView {

    private static String baseDownloadPath;
    private static ServletContext servletContext;
    private static FileLFUCache fileCache;

    private File file;

    public FileView(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file is null.");
        }
        this.file = file;
    }

    public FileView(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("fileName is blank.");
        }

        String fullFileName;
        fileName = fileName.trim();
        if (fileName.startsWith("/") || fileName.startsWith("\\")) {
            if (baseDownloadPath.equals("/")) {
                fullFileName = fileName;
            } else {
                fullFileName = baseDownloadPath + fileName;
            }
        } else {
            fullFileName = baseDownloadPath + File.separator + fileName;
        }

        this.file = new File(fullFileName);
    }

    static void initContext(String baseDownloadPath, ServletContext servletContext) {
        FileView.baseDownloadPath = baseDownloadPath;
        FileView.servletContext = servletContext;

        if (null == fileCache) {
            fileCache = new FileLFUCache(Constants.me().getFileCacheSize(),
                    Constants.me().getMaxFileCachetSize(),
                    Constants.me().getFileCachetimeout());
        }
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) {
        // 设置头
        response.setHeader("Accept-Ranges", "bytes");
        contentType = servletContext.getMimeType(file.getName());
        response.setContentType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM);

        String downFileName = encodeFileName(request, file.getName());
        response.setHeader("Content-Disposition", "attachment; filename=" + downFileName);

        // 断点下载支持
        String range = request.getHeader("Range");
        if (StringUtils.isBlank(range)) {
            normalRender(response);
        } else {
            rangeRender(request, response);
        }
    }

    /**
     * 对下载的文件名称进行编码
     */

    protected String encodeFileName(HttpServletRequest request, String fileName) {
        String agent = request.getHeader("User-Agent"); // 获取浏览器
        try {
            if (agent.equalsIgnoreCase("firefox")) {
                fileName = "=?" + encoding + "?B?" + Base64.encodeToString(fileName) + "?=";
            } else {
                fileName = URLEncoder.encode(fileName, encoding);
            }
            return fileName;
        } catch (UnsupportedEncodingException e) {
            return fileName;
        }
    }

    /**
     * 常规下载
     */
    private void normalRender(HttpServletResponse response) {
        response.setHeader("Content-Length", String.valueOf(file.length()));
        try {
            @Cleanup OutputStream os = response.getOutputStream();
            byte[] fileBytes = fileCache.getFileBytes(file);// 缓存小文件
            if (null == fileBytes) {
                FileUtils.copyFile(file, os);
            } else {
                os.write(fileBytes);
                os.flush();
            }
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }

    /**
     * 断点下载
     */
    private void rangeRender(HttpServletRequest request, HttpServletResponse response) {
        Long[] range = {null, null};
        processRange(request, range);

        String contentLength = String.valueOf(range[1].longValue() - range[0].longValue() + 1);
        response.setHeader("Content-Length", contentLength);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);// status = 206

        // Content-Range: bytes 0-499/10000
        StringBuilder contentRange = new StringBuilder("bytes ").append(String.valueOf(range[0])).append("-")
                .append(String.valueOf(range[1])).append("/").append(String.valueOf(file.length()));
        response.setHeader("Content-Range", contentRange.toString());

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            long start = range[0];
            long end = range[1];

            inputStream = new BufferedInputStream(new FileInputStream(file));
            if (inputStream.skip(start) != start) {
                throw new IOException("File skip error");
            }
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            long position = start;
            for (int len; position <= end && (len = inputStream.read(buffer)) != -1; ) {
                if (position + len <= end) {
                    outputStream.write(buffer, 0, len);
                    position += len;
                } else {
                    for (int i = 0; i < len && position <= end; i++) {
                        outputStream.write(buffer[i]);
                        position++;
                    }
                }
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RenderException(e);
        } finally {
            IOUtils.closeQuietly(inputStream, outputStream);
        }

    }

    /**
     * Examples of byte-ranges-specifier values (assuming an entity-body of length 10000):
     * The first 500 bytes (byte offsets 0-499, inclusive): bytes=0-499
     * The second 500 bytes (byte offsets 500-999, inclusive): bytes=500-999
     * The final 500 bytes (byte offsets 9500-9999, inclusive): bytes=-500
     * Or bytes=9500-
     */
    private void processRange(HttpServletRequest request, Long[] range) {
        String rangeStr = request.getHeader("Range");
        int index = rangeStr.indexOf(',');
        if (index != -1) {
            rangeStr = rangeStr.substring(0, index);
        }
        rangeStr = rangeStr.replace("bytes=", "");

        String[] arr = rangeStr.split("-", 2);
        if (arr.length < 2) {
            throw new RuntimeException("Range error");
        }

        long fileLength = file.length();
        for (int i = 0; i < range.length; i++) {
            if (StringUtils.isNotBlank(arr[i])) {
                range[i] = Long.parseLong(arr[i].trim());
                if (range[i] >= fileLength) {
                    range[i] = fileLength - 1;
                }
            }
        }

        // Range format like: 9500-
        if (range[0] != null && range[1] == null) {
            range[1] = fileLength - 1;
        }
        // Range format like: -500
        else if (range[0] == null && range[1] != null) {
            range[0] = fileLength - range[1];
            range[1] = fileLength - 1;
        }

        // check final range
        if (range[0] == null || range[1] == null || range[0].longValue() > range[1].longValue()) {
            throw new RuntimeException("Range error " + Arrays.toString(range));
        }
    }

}
