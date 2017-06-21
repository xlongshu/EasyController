package longshu.easycontroller.core.multipart;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MultipartRequest
 *
 * @author LongShu 2017/05/28
 */
public class MultipartRequest extends HttpServletRequestWrapper {

    protected static File baseUploadPath;// 默认的上传文件路径
    protected static int maxPostSize;
    protected static String encoding;
    protected static FileRenamePolicy fileRenamePolicy;

    protected boolean parsed;
    protected Map<String, List<String>> parameters = new HashMap<String, List<String>>();
    protected List<FilePart> files = new ArrayList<FilePart>();
    protected Map<String, String[]> parameterMap;


    public MultipartRequest(HttpServletRequest request) {
        super(request);

        if (request instanceof MultipartRequest) {
            return;
        }

        initQueryParameter();
    }

    public static void init(int maxPostSize, String encoding) {
        baseUploadPath = new File(Constants.me().getBaseUploadPath());
        FileUtil.mkdirs(baseUploadPath);

        // Check saveDirectory is writable
        if (!baseUploadPath.canWrite()) {
            throw new IllegalArgumentException("Not writable: " + baseUploadPath);
        }

        MultipartRequest.maxPostSize = maxPostSize;
        MultipartRequest.encoding = encoding;

        if (null == fileRenamePolicy) {
            fileRenamePolicy = new DefaultFileRenamePolicy();
        }
    }

    public static void setFileRenamePolicy(FileRenamePolicy fileRenamePolicy) {
        MultipartRequest.fileRenamePolicy = fileRenamePolicy;
    }

    /**
     * 文件下载路径
     */
    protected File getFinalUploadPath(String uploadPath) {
        if (StringUtils.isBlank(uploadPath)) {
            return baseUploadPath;
        }

        File uploadDir;
        if (FileUtil.isAbsolutelyPath(uploadPath)) {
            uploadDir = new File(uploadPath);
        } else {
            uploadDir = new File(Constants.me().getBaseUploadPath());
        }
        FileUtil.mkdirs(uploadDir);

        // Check saveDirectory is writable
        if (!uploadDir.canWrite()) {
            throw new IllegalArgumentException("Not writable: " + uploadDir);
        }

        return uploadDir;
    }

    protected void initQueryParameter() {
        // queryString
        @SuppressWarnings("unchecked")
        Enumeration<String> queryParameterNames = getRequest().getParameterNames();
        while (queryParameterNames.hasMoreElements()) {
            String queryName = queryParameterNames.nextElement();
            String[] queryValues = getRequest().getParameterValues(queryName);
            addParamValue(queryName, queryValues);
        }
    }

    protected void parserMultipartRequest(String uploadPath) {
        if (parsed) {
            return;
        }

        File uploadDir = getFinalUploadPath(uploadPath);

        try {
            MultipartParser multipartParser = new MultipartParser(getRequest(), maxPostSize, true, true, encoding);
            Part part;

            while ((part = multipartParser.readNextPart()) != null) {
                String name = part.getName();
                if (name == null) {
                    continue;
                }
                if (part.isParam()) {// string
                    ParamPart paramPart = (ParamPart) part;
                    String value = paramPart.getStringValue();
                    addParamValue(name, value);
                } else if (part.isFile()) {// file
                    com.oreilly.servlet.multipart.FilePart fp = (com.oreilly.servlet.multipart.FilePart) part;
                    String fileName = fp.getFileName();
                    if (fileName != null) {
                        fp.setRenamePolicy(fileRenamePolicy);  // null policy is OK
                        fp.writeTo(uploadDir);

                        FilePart filePart = new FilePart(name, new File(uploadDir, fp.getFileName()), fileName, fp.getContentType());
                        files.add(filePart);
                    }
                }// end file
            }// end while
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            parsed = true;
        }
    }

    protected void addParamValue(String name, String... values) {
        if (values == null || values.length == 0) {
            return;
        }

        List<String> existingValues = parameters.get(name);
        if (existingValues == null) {
            existingValues = new ArrayList<String>(2);
            parameters.put(name, existingValues);
        }
        for (String value : values) {
            existingValues.add(value);
        }
    }

    /**
     * the content type of the file.
     */
    public String getContentType(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        FilePart filePart = getFile(name);
        if (null != filePart) {
            return filePart.getContentType();
        }
        return null;
    }

    public List<FilePart> getFiles(String uploadPath) {
        parserMultipartRequest(uploadPath);

        return files;
    }

    public List<FilePart> getFiles() {
        return getFiles(null);
    }

    public FilePart getFile(String name, String uploadPath) {
        if (StringUtils.isBlank(name)) {
            return null;
        }

        parserMultipartRequest(uploadPath);

        for (int i = 0; i < files.size(); i++) {
            FilePart filePart = files.get(i);
            if (name.equals(filePart.getPartName())) {
                return filePart;
            }
        }
        return null;
    }

    public FilePart getFile(String name) {
        return getFile(name, null);
    }

    @Override
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) super.getRequest();
    }

    @Override
    public String getParameter(String name) {
        parserMultipartRequest(null);

        List<String> values = parameters.get(name);
        if (values == null || values.isEmpty()) {
            return null;
        }
        String value = values.get(values.size() - 1);
        return value;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        parserMultipartRequest(null);

        if (parameterMap == null) {
            Set<Map.Entry<String, List<String>>> entrySet = parameters.entrySet();
            Map<String, String[]> map = new HashMap<String, String[]>(entrySet.size());

            for (Map.Entry<String, List<String>> entry : entrySet) {
                List<String> valueList = entry.getValue();
                map.put(entry.getKey(), list2Array(valueList));
            }
            parameterMap = map;
        }
        return parameterMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        parserMultipartRequest(null);

        return Collections.enumeration(parameters.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        parserMultipartRequest(null);

        List<String> valueList = parameters.get(name);
        return list2Array(valueList);
    }

    protected String[] list2Array(List<String> valueList) {
        if (null == valueList || valueList.isEmpty()) {
            return null;
        }
        return valueList.toArray(new String[valueList.size()]);
    }

}
