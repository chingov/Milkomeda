package com.github.yizzuide.milkomeda.util;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * IOUtils
 *
 * @author yizzuide
 * @since 3.3.1
 * @version 3.7.0
 * Create at 2020/05/07 14:17
 */
public class IOUtils {

    /**
     * 加载lua脚本
     * @param path      所在路径
     * @param filename  文件名（包含扩展名）
     * @return          lua脚本文本
     * @throws IOException 读取异常
     * @since 3.3.1
     */
    public static String loadLua(String path, String filename) throws IOException {
        if (path == null || filename == null) {
            return "";
        }
        InputStream inputStream = IOUtils.class.getResourceAsStream(path + "/" + filename);
        if (inputStream == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            // 去注释
            if (line.matches("\\s*-{2,}.*")) {
                continue;
            }
            // 去空行
            if (line.matches("\\s+")) {
                continue;
            }
            // 代码缩减空白
            line = StringUtils.trimLeadingWhitespace(line);
            line = StringUtils.trimLeadingWhitespace(line);
            line = line.replaceAll("[\n\r\t]", "");
            out.append(line);
            // 忽略有分号的行，说明已有结束符
            if (line.endsWith(";")) {
                continue;
            }
            // 指令行，添加空格
            if (line.startsWith("if") || line.startsWith("then") || line.endsWith("else") || line.startsWith("for") || line.startsWith("do")
                || line.startsWith("while") || line.startsWith("function")) {
                out.append(" ");
            } else {
                // 语句行，添加结束符
                out.append("; ");
            }
        }
        return out.toString();
    }
}
