package longshu.easycontroller.core.multipart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.Serializable;

/**
 * FilePart
 *
 * @author LongShu 2017/05/28
 */
@Data
@AllArgsConstructor
public class FilePart implements Serializable {
    private static final long serialVersionUID = 1L;

    private String partName;// 表单名称

    private File file;
    private String originalFileName; // 文件原名称
    private String contentType;

}
