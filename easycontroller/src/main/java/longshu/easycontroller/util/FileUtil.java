package longshu.easycontroller.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * FileUtil
 *
 * @author LongShu 2017/05/11
 */
public class FileUtil {
    /**
     * The Unix separator character.
     */
    public static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    public static final char WINDOWS_SEPARATOR = '\\';

    /**
     * 返回如: 2017/01/
     */
    public static String buildYearMonth() {
        Date date = new Date();
        return DateUtil.toStr(date, "yyyy/MM/");
    }

    /**
     * 按照日期打乱
     *
     * @param basePath 文件存储路径
     * @return 如 savePath/2017/01
     */
    public static File getSavePath(File basePath) {
        File dir = new File(basePath, buildYearMonth());
        if (!dir.exists()) {//如果文件夹不存在就创建
            mkdirs(dir);
        }
        return dir;
    }

    /**
     * 参数重载一个方法，由于保存是按照绝对路径的
     * 并且我们按照日期打乱
     *
     * @param basePath
     * @return
     */
    public static String getSavePath(String basePath) {
        return getSavePath(new File(basePath)).getAbsolutePath();
    }

    /**
     * 获取有后缀的文件名称
     * <pre>
     * abc.xyz -> abc
     * a/b/abc.xyz -> abc
     * </pre>
     */
    public static String getBaseName(String filename) {
        int lastSeparator = indexOfLastSeparator(filename);
        int extensionPos = indexOfExtension(filename);

        if (lastSeparator == -1) {// 没有路径
            if (extensionPos == -1) {// 没有后缀
                return filename;
            }
            return filename.substring(0, extensionPos);
        } else {// 有路径
            if (extensionPos == -1) {// 没有后缀
                return filename.substring(lastSeparator);
            }
            return filename.substring(lastSeparator, extensionPos);
        }
    }

    public static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return -1;
        }
        final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static int indexOfExtension(final String filename) {
        if (filename == null) {
            return -1;
        }
        final int extensionPos = filename.lastIndexOf('.');
        final int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? -1 : extensionPos;
    }

    /**
     * 判断文件是否有后缀
     */
    public static boolean hasExtension(String filename) {
        return null != getExtension(filename);
    }


    /**
     * 获取文件后缀,前提是有后缀
     * <pre>
     * abc.xyz -> .xyz
     * a/b/abc.xyz -> .xyz
     * a/b.txt/c    -> null
     * </pre>
     */
    public static String getExtension(String filename) {
        if (StringUtils.isBlank(filename)) {
            return null;
        }
        int extensionPos = indexOfExtension(filename);
        if (-1 == extensionPos) {
            return null;
        }
        return filename.substring(extensionPos);
    }


    /**
     * 使用UUID获取唯一的文件名
     *
     * @param filename 文件名
     * @return 返回经过UUID处理后的filename
     */
    public static String getUUIDName(String filename) {
        // 判断文件名是否为空
        if (StringUtils.isBlank(filename)) {
            return null;
        }
        String name = filename;
        String ext = getExtension(filename);
        if (ext != null) {// 有后缀
            name = getBaseName(filename);
        } else {
            ext = "";
        }
        StringBuilder sb = new StringBuilder(name);
        sb.append('_').append(UUID.randomUUID().toString().replaceAll("-", "")).append(ext);
        return sb.toString();
    }

    /**
     * 使用UUID和时间获取唯一的带有日期信息的文件名
     *
     * @param filename 文件名
     * @return 经过处理后的文件名
     */
    public static String getDateTimeName(String filename) {
        // 判断文件名是否为空
        if (StringUtils.isBlank(filename)) {
            return null;
        }
        String name = filename;
        String ext = getExtension(filename);
        if (ext != null) {// 有后缀
            name = getBaseName(filename);
        } else {
            ext = "";
        }
        String time = DateUtil.toStr(new Date(), "yyyyMMddhhmmss");
        StringBuilder sb = new StringBuilder(name);
        sb.append('_').append(time).append(ext);
        return sb.toString();
    }

    /**
     * 新建一个文件夹
     *
     * @param dirs 基于dirs构造文件夹
     * @return 文件夹路径(dirs[0]/dirs[1]/dirs[2]...)
     */
    public static File makeDir(String... dirs) {
        // 判断路径是否为空
        if (dirs == null || dirs.length < 1) {
            return null;
        }
        // 构建路径
        StringBuilder path = new StringBuilder(dirs[0]);
        for (int i = 1; i < dirs.length; i++) {
            path.append(File.separator).append(dirs[i]);
        }

        File dir = new File(path.toString());
        // 文件夹不存在则创建
        mkdirs(dir);
        return dir;
    }

    public static boolean mkdirs(File dir) {
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    /**
     * 批量删除文件
     *
     * @param filePaths 文件路径
     * @return 成功删除的文件数量
     */
    public static int deleteFile(String... filePaths) {
        int result = 0;
        File file;
        for (String filepath : filePaths) {
            file = new File(filepath);
            if (file.exists()) {
                if (file.delete()) {
                    result++;
                }
            }
        }
        return result;
    }

    public static boolean isAbsolutelyPath(String path) {
        return path.startsWith("/") || path.indexOf(":") == 1;
    }

}
